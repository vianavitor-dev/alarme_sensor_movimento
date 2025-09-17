# Alarme com Sensor de Movimento

## Project Model Canvas
O arquivo **Project Model CANVAS.xlsx** contém os detalhes do projeto:  
1. **Project Model Canvas**: visão geral do projeto;  
2. **Algoritmo**: funcionalidades do sistema;  
3. **Hardware Envolvido**: componentes necessários para o funcionamento do alarme.  

## Pasta ESP32
Esta pasta contém o script carregado no ESP32, esquemas de ligação no protoboard e bibliotecas necessárias para o funcionamento.

## Pasta Backend_Frontend
Contém o projeto Spring responsável por gerenciar o estado do alarme.

## Como Usar 

### Alarme
1. Siga o esquema elétrico disponível na pasta **ESP32** para a montagem correta do circuito eletrônico.  
2. Abra o código em **ESP32/alarme_sensor_movimento/** com a Arduino IDE.  
3. Vá em *Sketch > Include Library > Add .ZIP Library* e selecione as bibliotecas presentes em **ESP32/Bibliotecas**.  

### Back-end (Spring)
1. Abra o projeto **Backend_Frontend/HiveMQCloudConnectionWithSpring** em seu editor/IDE preferido e execute a classe **App.java**.  
2. Crie o arquivo **application.properties** (localizado em **src/main/resources**) para configurar a conexão com o seu MQTT Broker, seguindo o exemplo abaixo:  

~~~properties
mqtt.HOST=seu-host
mqtt.PORT=sua-porta
mqtt.USERNAME=seu-usuario
mqtt.PASSWORD=sua-senha
~~~

### Front-end
Após iniciar o back-end, abra o navegador e acesse: **http://localhost:8080**.  

## Programas Utilizados
- **Arduino IDE**: [Download](https://downloads.arduino.cc/arduino-ide/arduino-ide_2.3.6_Windows_64bit.exe)  
- **Java 24.0.1**: [Download](https://www.oracle.com/java/technologies/javase/jdk24-archive-downloads.html)  
- **IntelliJ IDEA Community (IDE utilizada)**: [Download](https://www.jetbrains.com/idea/download/?section=windows)  
- **Wokwi (simulação do ESP32)**: [Acessar](https://wokwi.com/)
- **HiveMQ Cloud (MQTT Broker utilizado)**: [Acessar](https://www.hivemq.com/)
