#include <WiFi.h>
#include <PubSubClient.h>
#include <WiFiClientSecure.h>
#include <cstring>

// ====== CONFIG WIFI & MQTT ======
const char* WIFI_SSID = "Wokwi-GUEST";
const char* WIFI_PASS = "";

const char* MQTT_SERVER = "5232a6122a3a401aaeaa51b71372284c.s1.eu.hivemq.cloud";
const int   MQTT_PORT   = 8883;
const char* MQTT_USER   = "Wokwi123";
const char* MQTT_PASSWD = "Wokwi123";

const char* TOPIC_PUB   = "home/sensor/movement";
const char* TOPIC_SUB   = "home/action/";

WiFiClientSecure espClient;
PubSubClient client(espClient);

// ====== PINS ======
#define LED        12
#define LED_ON     14
#define BTN_STOP   22
#define BTN_TURNON 16
#define MOTION     23
#define BUZZER     15

// ====== STATE VARIABLES ======
bool btn_pressed = false;
bool command_alarm_off = false;
String state = "stopped";
int alarm_pattern = 0;
unsigned long last_alarm = 0;
bool alarm_has_rang = false;
bool is_on = false;
String last_message = "";

// ====== ALARM FUNCTION ======
int alarm(int pattern) {
  if (pattern == 0) {
    noTone(BUZZER);
    digitalWrite(LED, HIGH);
    return 1;
  }
  tone(BUZZER, 100); // frequência 100 Hz
  digitalWrite(LED, LOW);
  return 0;
}

// ====== MQTT CALLBACK ======
void callback(char* topic, byte* payload, unsigned int length) {
  String msg;
  for (unsigned int i = 0; i < length; i++) {
    msg += (char)payload[i];
  }

  Serial.printf("[MQTT] Received message on %s: %s\n", topic, msg.c_str());

  if (String(topic) == TOPIC_SUB) {
    if (msg == "alarm.off") {
      if (state == "alarm") {
        Serial.println("* turning alarm off");
        command_alarm_off = true;
      }
    }
    else if (msg == "turn-off") {
      Serial.println("* turning off");
      is_on = false;
    }
    else if (msg == "turn-on") {
      Serial.println("* turning on");
      is_on = true;
    }
  }
}

// ====== WIFI & MQTT SETUP ======
void setup_wifi() {
  Serial.print("Connecting to WiFi");
  WiFi.begin(WIFI_SSID, WIFI_PASS);
  while (WiFi.status() != WL_CONNECTED) {
    delay(200);
    Serial.print(".");
  }
  Serial.println(" Connected!");
}

void reconnect() {
  while (!client.connected()) {
    Serial.print("Connecting to MQTT...");
    if (client.connect("esp32-client", MQTT_USER, MQTT_PASSWD)) {
      Serial.println(" Connected!");
      client.subscribe(TOPIC_SUB);
    } else {
      Serial.print(" failed, rc=");
      Serial.print(client.state());
      delay(2000);
    }
  }
}

// ====== SETUP ======
void setup() {
  Serial.begin(115200);

  pinMode(LED, OUTPUT);
  pinMode(LED_ON, OUTPUT);
  pinMode(BTN_STOP, INPUT_PULLUP);
  pinMode(BTN_TURNON, INPUT_PULLUP);
  pinMode(MOTION, INPUT);

  noTone(BUZZER); // garante que começa desligado

  setup_wifi();
  espClient.setInsecure(); // broker TLS sem certificado local
  client.setServer(MQTT_SERVER, MQTT_PORT);
  client.setCallback(callback);
}

void send_message(char* message) {
  Serial.println("[ESP32] Sending message to "+ String(TOPIC_PUB) + ": " + String(message));

  client.publish(TOPIC_PUB, message);
}

// ====== LOOP ======
void loop() {
  if (!client.connected()) {
    reconnect();
  }
  client.loop();

  String message = "";

  // Botão liga/desliga
  if (digitalRead(BTN_TURNON) == LOW) {
    message = "* toggle power (on/off)";
    is_on = !is_on;
    delay(200);
  }

  if (!is_on) {
    if (digitalRead(LED_ON) == HIGH) {
      message = "* turning off (loop)";
      // client.publish(TOPIC_PUB, "Alarme desligado");
      send_message("Alarme desligado");
      digitalWrite(LED_ON, LOW);
    }
    return;
  }

  if (digitalRead(LED_ON) == LOW) {
    // client.publish(TOPIC_PUB, "Alarme ligado");
    send_message("Alarme ligado");
    digitalWrite(LED_ON, HIGH);
  }

  // Sensor de movimento
  if (digitalRead(MOTION) == LOW) {
    btn_pressed = false;
    command_alarm_off = false;
    state = "stopped";
  } else {
    if (btn_pressed || command_alarm_off) {
      state = "stopped";
    } else {
      btn_pressed = false;
      state = "alarm";
    }
  }

  // Controle do estado
  if (state == "alarm") {
    message = "movement detected";

    if (digitalRead(BTN_STOP) == LOW) {
      Serial.println("* button pressed");
      btn_pressed = true;
      delay(100);
      return;
    }

    unsigned long now = millis();
    if (now - last_alarm >= 500) {
      if (!alarm_has_rang) {
        // client.publish(TOPIC_PUB, "Movimento detectado");
        send_message("Movimento detectado");
        alarm_has_rang = true;
      }

      alarm_pattern = alarm(alarm_pattern);
      last_alarm = now;
    }
  } else if (state == "stopped") {
    message = "no movement detected";

    if (alarm_has_rang) {
      // client.publish(TOPIC_PUB, "Alarme parou");
      send_message("Alarme parou");
      alarm_has_rang = false;
      btn_pressed = false;
    }

    noTone(BUZZER);
    digitalWrite(LED, LOW);
  }

  if (last_message != message) {
    Serial.println(message);
    last_message = message;
  }
}
