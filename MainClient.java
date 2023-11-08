package Клієнт;

public class MainClient {
    public static void main(String[] args) {
        try {
            ConnectInputMessage connectWithServer = new ConnectInputMessage();
            Thread tConnectInputMessage = new Thread(connectWithServer);
            tConnectInputMessage.start();

            ReceiveMessageFromServer receiveMessage = new ReceiveMessageFromServer(connectWithServer.getInputStreamServer());
            Thread tReceiveMessage = new Thread(receiveMessage);
            tReceiveMessage.start();
        } catch (Exception e) {
            System.err.println("Помилка в головному клієнтському потоці.");
            e.printStackTrace();
        }
    }
}
