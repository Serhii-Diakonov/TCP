package metroBank;

import metroBank.stores.MetroCard;
import metroBank.stores.MetroCardBank;
import metroBank.xml.XMLReader;
import metroBank.xml.XMLWriter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class MetroServer extends Thread {
    MetroCardBank cards;
    private ServerSocket socket = null;
    private int port;
    private StringBuilder output = new StringBuilder();
    private boolean isFinished = false;
    private ArrayList<Socket> sockets = new ArrayList<>();

    public MetroServer(int port) {
        cards = new MetroCardBank();
        if (port >= 1024 && port <= 65535) this.port = port;
        else this.port = 2000;
    }

    public static void main(String[] args) {
        MetroServer server = new MetroServer(7898);
        server.start();
    }

    public MetroCardBank getCards() {
        return cards;
    }

    @Override
    public void run() {
        isFinished = false;
        try {
            socket = new ServerSocket(port);
            System.out.println("Сервер розпочав роботу");
            output.append("Сервер розпочав роботу\n");
            while (!isFinished) {
                output.append("Очікування нового клієнта...\n");
                System.out.println("Очікування нового клієнта...");
                Socket clientSocket = socket.accept();
                sockets.add(clientSocket);
                output.append("Новий клієнт: ").append(clientSocket).append("\n");
                System.out.println("Новий клієнт: " + clientSocket);
                ClientHandler ch = new ClientHandler(cards, clientSocket, this);
                ch.start();
            }
        }catch (SocketException s){
            output.append("З'єднання розірвано\n");
            System.out.println("З'єднання розірвано");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void finish() {
        isFinished = true;
        try {
            socket.close();
            output.append("Сервер зупинив роботу\n");
            System.out.println("Сервер зупинив роботу");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getOutput() {
        return output.toString();
    }

    public void addToOutput(String s){
        output.append(s);
    }

    public void resetOutput() {
        output = new StringBuilder();
    }

    public void showCards() {
        if (!cards.getStore().isEmpty()) {
            for (MetroCard c : cards.getStore()) {
                output.append("\n").append(c.toString()).append("\n");
                System.out.println(c.toString());
            }
        } else {
            output.append("Карток нема\n");
            System.out.println("Карток нема");
        }
    }

    public void freeBank() {
        cards = new MetroCardBank();
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void outputClients() {
        int i = 1;
        StringBuilder buf = new StringBuilder();
        sockets.removeIf(Socket::isClosed);
        for (Socket s : sockets) {
            buf.append("\nКлієнт ").append(i++).append(": ").append(s).append("\n");
        }
        output.append(buf);
    }

    public int getClientsNum() {
        sockets.removeIf(Socket::isClosed);
        return sockets.size();
    }

    public void writeXML() {
        new XMLWriter(cards.getStore());
    }

    public void readXML() {
        cards.setStore(new XMLReader().getCards());
    }
}
