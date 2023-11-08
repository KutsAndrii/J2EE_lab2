package Сервер;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer implements Runnable {
    private Map<Integer, Socket> mapClient = new TreeMap<>();

    @Override
    public void run() {
        ServerSocket server = null;
        try {
            server = new ServerSocket(8887);
            System.out.println("Server started. Waiting for clients.");
            int numberClient = 1;
            Socket client = null;

            while (true) {
                try {
                    client = server.accept();
                    Thread clientThread = new Thread(new ClientThread(client, this, numberClient));
                    clientThread.setDaemon(true);
                    clientThread.start();
                    mapClient.put(numberClient, client);
                    numberClient++;
                } catch (IOException e) {
                    System.err.println("Помилка при очікуванні клієнта.");
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("Помилка при створенні ServerSocket.");
            e.printStackTrace();
        } finally {
            if (server != null && !server.isClosed()) {
                try {
                    server.close();
                } catch (IOException e) {
                    System.err.println("Помилка при закритті ServerSocket.");
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendMessageForAllClient(int numberClient, String clientMessage) {
        List<Integer> clientsToRemove = new ArrayList<>();

        for (Map.Entry<Integer, Socket> entry : mapClient.entrySet()) {
            int clientNumber = entry.getKey();
            Socket clientSocket = entry.getValue();

            if (clientNumber != numberClient) {
                try {
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    out.println("Client №" + numberClient + ": " + clientMessage);
                } catch (IOException e) {
                    System.err.println("Помилка при відправці повідомлення клієнту №" + clientNumber);
                    e.printStackTrace();
                    clientsToRemove.add(clientNumber);
                }
            }
        }

        // Видалення клієнтів, які не доступні
        for (int clientNumberToRemove : clientsToRemove) {
            mapClient.remove(clientNumberToRemove);
        }
    }
}
