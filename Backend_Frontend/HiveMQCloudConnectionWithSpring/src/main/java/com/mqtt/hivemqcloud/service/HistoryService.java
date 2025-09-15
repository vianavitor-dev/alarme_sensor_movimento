package com.mqtt.hivemqcloud.service;

import com.mqtt.hivemqcloud.controller.ApiResponse;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class HistoryService {
    private final String filePath = "src/main/resources/static/history.txt";

    public String getHistory() {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader buffer = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                if (line.isBlank()) continue;

                sb.append(line.concat("\n"));
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        return sb.toString();
    }

    public void saveIntoHistory(ApiResponse object) {
        if (object.getMessage() == null || object.getMessage().isBlank()) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader buffer = new BufferedReader(new FileReader(filePath))){
            String line;

            while((line = buffer.readLine()) != null) {
                if (line.length() < 17) continue;

                boolean sameMessage = line.toLowerCase().contains(object.getMessage().toLowerCase().trim());
                boolean sameHour = Integer.parseInt(line.substring(12, 14)) == object.getReceivedAt().getHour();

                if (sameHour && sameMessage) {
                    System.out.println("* Registro já salvado!");
                    return;
                }

                sb.append(line.concat("\n"));
            }

        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo: " + e.getMessage());
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
