package com.mqtt.hivemqcloud.helper;

import com.mqtt.hivemqcloud.controller.ApiResponse;

import java.io.*;

public class TxtHelper {
    public static void saveInTxtFile(ApiResponse object) {
        String filePath = "src/main/resources/static/history.txt";

        if (object.getMessage() == null || object.getMessage().isBlank()) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader buffer = new BufferedReader(new FileReader(filePath))){
            String line;

            while((line = buffer.readLine()) != null) {
                if (line.length() < 17) continue;

                boolean sameMessage = line.toLowerCase().contains(object.getMessage().toLowerCase().trim());
                boolean sameMinute = Integer.parseInt(line.substring(15, 17)) == object.getReceivedAt().getMinute();

                if (sameMinute && sameMessage) {
                    System.out.println("* Registro já salvado!");
                    return;
                }

                sb.append(line.concat("\n"));
            }

        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler arquivo: " + e.getMessage());
        }

        try (FileWriter fileWriter = new FileWriter(filePath);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            sb.append(object.toString().concat("\n"));
            printWriter.println(sb.toString());

            System.out.println("* Registro salvo em history.txt");

        } catch (IOException e) {
            System.err.println("Erro ao salvar registro no arquivo: " + e.getMessage());
        }
    }
}
