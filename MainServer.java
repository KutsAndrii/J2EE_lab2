package Сервер;

public class MainServer {
    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        Thread tChatServer = new Thread((Runnable) chatServer);
        tChatServer.start();
    }
}