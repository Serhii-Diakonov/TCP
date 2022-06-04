package metroBank.gui;

import metroBank.Client;
import metroBank.operations.*;
import metroBank.stores.User;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ClientPanel extends JPanel {

    ArrayList<ClientGUI> clientGUIS;
    boolean firstTime = true;

    ClientPanel(ServerPanel server) {

        clientGUIS = new ArrayList<>();

        JButton addBtn = new JButton("Add");
        addBtn.setBackground(new Color(144, 238, 144));
        this.add(addBtn);

        JPanel var1 = new JPanel(new GridLayout(0, 1));
        JScrollPane scroll = new JScrollPane(var1);
        this.add(scroll);

        GroupLayout l1 = new GroupLayout(this);
        this.setLayout(l1);
        l1.setAutoCreateGaps(true);
        l1.setAutoCreateContainerGaps(true);
        l1.setHorizontalGroup(l1.createParallelGroup().addComponent(addBtn).addComponent(scroll));
        l1.setVerticalGroup(l1.createSequentialGroup().addComponent(addBtn).addComponent(scroll));

        addBtn.addActionListener(e -> {
            if (firstTime) {
                RemoverThread remover = new RemoverThread(var1);
                remover.start();
                firstTime = false;
            }
            if (server.getServer() != null) {
                clientGUIS.add(new ClientGUI(this, new Client("localhost", 7898)));
                var1.add(clientGUIS.get(clientGUIS.size() - 1));
                revalidate();
            } else JOptionPane.showMessageDialog(this, "Спочатку потрібно запустити сервер");
        });

        String plaf = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
        try {
            UIManager.setLookAndFeel(plaf);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class RemoverThread extends Thread {

        JPanel panel;

        RemoverThread(JPanel parentPanel) {
            panel = parentPanel;
        }

        @Override
        public void run() {
            super.run();
            synchronized (clientGUIS) {
                try {
                    while (true) {
                        sleep(50);
                        if (clientGUIS.size() > 0)
                            for (int i = 0; i < clientGUIS.size(); i++) {
                                if (clientGUIS.get(i).isFinished()) {
                                    panel.remove(clientGUIS.get(i));
                                    clientGUIS.remove(clientGUIS.get(i));
                                    repaint();
                                    revalidate();
                                }
                            }
                    }
                } catch (InterruptedException e) {
                }
            }
        }
    }

    class ClientGUI extends JPanel{

        Client client;
        boolean isFinished = false;
        JPanel panel;
        JTextPane txt;

        ClientGUI(JPanel panel, Client cl)  {
            this.panel = panel;
            client = cl;
            JLabel info = new JLabel(client.getSocket().toString());

            JButton addCardBtn = new JButton("Додати картку");
            JButton removeBtn = new JButton("Видалити картку");
            JButton addMoneyBtn = new JButton("Поповнити картку");
            JButton payMoneyBtn = new JButton("Сплатити з картки");
            JButton showBalanceBtn = new JButton("Баланс");
            JButton showInfoBtn = new JButton("Інфо картки");
            JButton stopBtn = new JButton("Завершити сеанс");
            stopBtn.setBackground(new Color(233, 150, 122));

            txt = new JTextPane();
            txt.setMaximumSize(new Dimension(20, 20));
            txt.setEditable(false);
            JScrollPane scroll = new JScrollPane(txt);
            scroll.setPreferredSize(new Dimension(600, 100));

            addMoneyBtn.addActionListener(e -> {
                ArrayList<String> lbls = new ArrayList<>();
                lbls.add("ID");
                lbls.add("Сума");
                InputFrame fr = new InputFrame(lbls);
                fr.showDialog(this, "Поповнення картки");
                if (fr.getField(1) != null && isParsableDouble(fr.getField(2)))
                    client.applyOperation(new AddMoneyOperation(fr.getField(1), Double.parseDouble(fr.getField(2))));
            });

            payMoneyBtn.addActionListener(e -> {
                ArrayList<String> lbls = new ArrayList<>();
                lbls.add("ID");
                lbls.add("Сума");
                InputFrame fr = new InputFrame(lbls);
                fr.showDialog(this, "Зняття коштів");
                if (fr.getField(1) != null && isParsableDouble(fr.getField(2)))
                    client.applyOperation(new PayOperation(fr.getField(1), Double.parseDouble(fr.getField(2))));
            });

            showBalanceBtn.addActionListener(e -> {
                ArrayList<String> lbls = new ArrayList<>();
                lbls.add("ID");
                InputFrame fr = new InputFrame(lbls);
                fr.showDialog(this, "Перевірка балансу");
                if (fr.getField(1) != null)
                    client.applyOperation(new ShowBalanceOperation(fr.getField(1)));
            });

            showInfoBtn.addActionListener(e -> {
                ArrayList<String> lbls = new ArrayList<>();
                lbls.add("ID");
                InputFrame fr = new InputFrame(lbls);
                fr.showDialog(this, "Інформація по картці");
                if (fr.getField(1) != null)
                    client.applyOperation(new ShowInfoOperation(fr.getField(1)));
            });

            addCardBtn.addActionListener(e -> {
                ArrayList<String> lbls = new ArrayList<>();
                lbls.add("ID");
                lbls.add("Ім'я");
                lbls.add("Фамілія");
                lbls.add("Стать");
                lbls.add("Дата народження");
                lbls.add("Навчальний заклад");
                lbls.add("Баланс");
                InputFrame fr = new InputFrame(lbls);
                fr.showDialog(this, "Нова картка");
                AddMetroCardOperation op = new AddMetroCardOperation();
                if (fr.getField(1) != null)
                    op.getCard().setId(fr.getField(1));
                User user = new User();
                if (fr.getField(2) != null)
                    user.setName(fr.getField(2));
                if (fr.getField(3) != null)
                    user.setSurName(fr.getField(3));
                if (fr.getField(4) != null)
                    user.setSex(fr.getField(4));
                if (isParsableDate(fr.getField(5)))
                    user.setBirthday(fr.getField(5));
                op.getCard().setUser(user);
                if (fr.getField(6) != null)
                    op.getCard().setCollege(fr.getField(6));
                if (isParsableDouble(fr.getField(7)))
                    op.getCard().setBalance(Double.parseDouble(fr.getField(7)));
                client.applyOperation(op);
            });

            removeBtn.addActionListener(e -> {
                ArrayList<String> lbls = new ArrayList<>();
                lbls.add("ID");
                InputFrame fr = new InputFrame(lbls);
                fr.showDialog(this, "Видалення картки");
                if (fr.getField(1) != null)
                    client.applyOperation(new RemoveCardOperation(fr.getField(1)));
            });

            stopBtn.addActionListener(e -> {
                if (JOptionPane.showConfirmDialog(this, "Завершити сеанс?") == 0) {
                    client.finish();
                    isFinished = true;
                }
            });

            GroupLayout l1 = new GroupLayout(this);
            this.setLayout(l1);
            l1.setAutoCreateGaps(true);
            l1.setAutoCreateContainerGaps(true);
            l1.setHorizontalGroup(l1.createParallelGroup()
                    .addComponent(info)
                    .addComponent(scroll)
                    .addGroup(l1.createSequentialGroup()
                            .addComponent(addCardBtn)
                            .addComponent(addMoneyBtn)
                            .addComponent(showBalanceBtn))
                    .addGroup(l1.createSequentialGroup()
                            .addComponent(removeBtn)
                            .addComponent(payMoneyBtn)
                            .addComponent(showInfoBtn)
                            .addComponent(stopBtn)));

            l1.setVerticalGroup(l1.createSequentialGroup()
                    .addComponent(info)
                    .addComponent(scroll)
                    .addGroup(l1.createParallelGroup()
                            .addComponent(addCardBtn)
                            .addComponent(addMoneyBtn)
                            .addComponent(showBalanceBtn))
                    .addGroup(l1.createParallelGroup()
                            .addComponent(removeBtn)
                            .addComponent(payMoneyBtn)
                            .addComponent(showInfoBtn)
                            .addComponent(stopBtn)));

            class RefreshConsole extends Thread {
                String prevStr = "";

                @Override
                public void run() {
                    try {
                        while (true) {
                            synchronized (client){
                                sleep(100);
                                if (client != null && !prevStr.equals(client.getOutput())) {
                                    txt.setText(client.getOutput());
                                    prevStr = txt.getText();
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }
            RefreshConsole rc = new RefreshConsole();
            rc.start();
        }

        public void setOutput(String s) {
            if (txt != null) {
                txt.setText(txt.getText() + "\n" + s);
            }
        }

        public synchronized boolean isFinished() {
            return isFinished;
        }

        public boolean isParsableDouble(String num) {
            if (num != null && !num.isEmpty())
                try {
                    Double.parseDouble(num);
                    return true;
                } catch (NumberFormatException e) {
                }
            return false;
        }

        public boolean isParsableDate(String num) {
            if (num != null && !num.isEmpty())
                try {
                    new SimpleDateFormat().parse(num);
                    return true;
                } catch (ParseException e) {
                }
            return false;
        }
    }

    public boolean isAvailable(String server, int port) {
        try {
            Socket s = new Socket();
            s.connect(new InetSocketAddress(server, port), 1000);
            s.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }


}
