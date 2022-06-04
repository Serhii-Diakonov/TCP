package metroBank;

import metroBank.operations.*;
import metroBank.stores.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
    private int port = -1;
    private String server;
    private Socket socket = null;
    private ObjectInputStream is = null;
    private ObjectOutputStream os = null;
    private StringBuilder output = new StringBuilder();

    public Client(String server, int port) {
        if (port >= 1024 && port <= 65535) this.port = port;
        else this.port = 2000;
        this.server = server;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(server, port), 1000);
            os = new ObjectOutputStream(socket.getOutputStream());
            is = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client cl = new Client("localhost", 7898);
        AddMetroCardOperation op = new AddMetroCardOperation();
        op.getCard().setUser(new User("Іван", "Петров", "чоловік", "25.12.1999"));
        op.getCard().setId("Qd215413");
        op.getCard().setCollege("ХНУ ім. Каразіна");
        op.getCard().setBalance(25);
        cl.applyOperation(op);
        cl.applyOperation(new ShowInfoOperation("Qd215413"));
        System.out.println(cl.getOutput());
        cl.finish();
        System.out.println();

        cl = new Client("localhost", 7898);
        cl.applyOperation(new ShowInfoOperation("Qd215413"));
        cl.applyOperation(new AddMoneyOperation("Qd215413", 100));
        cl.applyOperation(new ShowBalanceOperation("Qd215413"));
        cl.applyOperation(new ShowInfoOperation("Qd215413"));
        cl.applyOperation(new ShowBalanceOperation("Qd215413"));
        System.out.println(cl.getOutput());
        cl.finish();
        System.out.println();

        cl = new Client("localhost", 7898);
        op = new AddMetroCardOperation();
        op.getCard().setUser(new User("Сесілія", "Гомез", "жінка", "02.02.2001"));
        op.getCard().setId("Qd215413");
        cl.applyOperation(op);
        System.out.println(cl.getOutput());
        cl.finish();
    }

    public void finish() {
        try {
            os.writeObject(new StopOperation());
            os.flush();
            Object o = is.readObject() + "\n";
            output.append(o);
            System.out.println(o);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void applyOperation(CardOperation op) {
        try {
            os.writeObject(op);
            os.flush();
            Object o = is.readObject() + "\n";
            output.append(o);
            System.out.println(o);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public int getPort() {
        return port;
    }

    public String getOutput() {
        return output.toString();
    }
}
