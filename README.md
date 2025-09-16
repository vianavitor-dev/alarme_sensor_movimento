## Project Model CANVAS 
O arquivo "Project Model CANVAS.xlsx" possui os detalhes do projeto: 
 1. Project Model Canvas: Sobre o projeto;
 2. Algorítimo: Funcionalidades do projeto; 
 3. Hardware Envolvido: componentes necessários para o funcionamento do alarme; 

## Pasta ESP32
Esta pasta contém o script carregado no ESP32, esquemas de ligação no protoboard e bibliotecas necessárias ao funcionamento.

## Como Usar 
### Alarme
1. Siga o esquema elétrico disponível na pasta **ESP32** para montagem correta do circuito eletronico.
2. Abra o código com Arduino IDE, vá em "Sketch > Include Library > Add .ZIP Library" e selecione as bibliotecas presentes em **ESP32/Bibliotecas**.

### Back-end (Spring)
1. Abra o arquivo **./Backend_Frontend/HiveMQCloudConnectionWithSpring** com o editor de texto do seu agrado e execute o arquivo **main** (App.java).
2. Crie o arquivo **application.properties** (encontrado no diretório **src/main/resources**) para se conectar com o seu MQTT Broker, seguindo o exemplo a baixo.
~~~ Java
mqtt.HOST = seu-host
mqtt.PORT = sua-porta
mqtt.USERNAME = seu-usuario
mqtt.PASSWORD = sua-senha
~~~

### Front-end
Abra o arquivo **index.html** disponível em **./Backend_Frontend/HiveMQCloudConnectionFrontend/index.html**.

## Instaladores e Depuradores:
Arduino IDE: https://downloads.arduino.cc/arduino-ide/arduino-ide_2.3.6_Windows_64bit.exe <br>
Java 24.0.1: https://www.oracle.com/java/technologies/javase/jdk24-archive-downloads.html <br>
IntelliJ IDEA Community (IDE Utilizada): https://www.jetbrains.com/idea/download/?section=windows
Wokwi (Website pra simular o ESP32): https://wokwi.com/
