package metroBank;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import metroBank.operations.*;
import metroBank.stores.MetroCardBank;

public class ClientHandler extends Thread {
    private ObjectOutputStream os = null;
    private ObjectInputStream is = null;
    private boolean work;
    private MetroCardBank cards;
    private final Socket s;
    private ArrayList<Class> classes;
    private MetroServer server;

    public ClientHandler(MetroCardBank bnk, Socket socket, MetroServer server) {
        this.server=server;
        classes = new ArrayList<>();
        classes.add(StopOperation.class);
        classes.add(AddMetroCardOperation.class);
        classes.add(AddMoneyOperation.class);
        classes.add(PayOperation.class);
        classes.add(RemoveCardOperation.class);
        classes.add(ShowBalanceOperation.class);
        classes.add(ShowInfoOperation.class);

        cards = bnk;
        s = socket;
        work = true;
        try {
            os = new ObjectOutputStream(s.getOutputStream());
            is = new ObjectInputStream(s.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if (cards != null) {
            System.out.println("Обслуговується клієнт на порті " + s);
            while (work) {
                Object o;
                try {
                    o = is.readObject();
                    sleep(500);
                    synchronized (cards) {
                        processOperation(o);
                    }
                } catch (IOException | ClassNotFoundException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                System.out.println("Завершено обслуговування на порті " + s);
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Сховище карток не ініціалізовано");
        }
    }

    public void processOperation(Object o) throws
            IOException, ClassNotFoundException {
        for (int i = 0; i < classes.size(); i++) {
            if (o.getClass().equals(classes.get(i)))
                switch (i) {
                    default -> error();
                    case 0 -> finish();
                    case 1 -> addCard(o);
                    case 2 -> addMoney(o);
                    case 3 -> pay(o);
                    case 4 -> remove(o);
                    case 5 -> showBalance(o);
                    case 6 -> showInfo(o);
                }
        }
    }

    private void finish() throws IOException {
        os.writeObject("Робота завершена " + s);
        os.flush();
        server.addToOutput("Робота завершена " + s+"\n");
        work = false;
    }

    private void addCard(Object o)
            throws IOException {
        cards.addCard(((AddMetroCardOperation) o).getCard());
        int index = cards.getStore().size() - 1;
        os.writeObject("Картку додано з ID: " + cards.getStore().get(index).getId());
        os.flush();
    }

    private void addMoney(Object o)
            throws IOException {
        AddMoneyOperation op = (AddMoneyOperation) o;
        boolean res = cards.addMoney(op.getId(), op.getMoney());
        if (res) {
            os.writeObject("Рахунок картки " + op.getId() + " поповнено на " + op.getMoney());
            os.flush();
        } else {
            os.writeObject("Неможливо поповнити картку " + op.getId());
            os.flush();
        }
    }

    private void pay(Object o)
            throws IOException {
        PayOperation op = (PayOperation) o;
        boolean res = cards.getMoney(op.getId(), op.getMoney());
        if (res) {
            os.writeObject("Гроші знято з картки " + op.getId());
            os.flush();
        } else {
            os.writeObject("Неможливо виконати оплату з картки " + op.getId());
            os.flush();
        }
    }

    private void remove(Object o)
            throws IOException {
        RemoveCardOperation op = (RemoveCardOperation) o;
        boolean res = cards.removeCard(op.getId());
        if (res) {
            os.writeObject("Картку " + op.getId() + " видалено");
            os.flush();
        } else {
            os.writeObject("Неможливо видалити картку " + op.getId());
            os.flush();
        }
    }

    private void showBalance(Object o)
            throws IOException {
        ShowBalanceOperation op = (ShowBalanceOperation) o;
        int index = cards.searchID(op.getId());
        if (index >= 0) {
            os.writeObject("Картка: " + op.getId() + " Баланс: " + cards.getStore().get(index).getBalance());
            os.flush();
        } else {
            os.writeObject("Неможливо показати баланс картки " + op.getId());
            os.flush();
        }
    }

    private void showInfo(Object o)
            throws IOException {
        ShowInfoOperation op = (ShowInfoOperation) o;
        int index = cards.searchID(op.getId());
        if (index >= 0) {
            os.writeObject(cards.getStore().get(index).toString());
            os.flush();
        } else {
            os.writeObject("Картку " + op.getId() + " не знайдено");
            os.flush();
        }
    }

    private void error() throws IOException {
        os.writeObject("Невірна операція");
        os.flush();
    }
}
