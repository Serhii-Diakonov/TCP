package metroBank.gui;

import metroBank.MetroServer;

import javax.swing.*;
import java.awt.*;

public class ServerPanel extends JPanel {

    private MetroServer server;

    ServerPanel(int port){
        super();

        JButton startBtn=new JButton("Запустити");
        JButton stopBtn=new JButton("Зупинити");

        startBtn.setBackground(new Color(144, 238, 144));
        stopBtn.setBackground(new Color(233, 150, 122));

        JLabel lbl=new JLabel("Метро Сервер");
        lbl.setFont(new Font("Arial", Font.BOLD, 16));
        JPanel var=new JPanel();
        var.add(lbl);

        JPanel var1=new JPanel();
        var1.add(startBtn);
        var1.add(stopBtn);

        JTextPane txt=new JTextPane();
        txt.setPreferredSize(new Dimension(120,250));
        JScrollPane scroll = new JScrollPane(txt);
        txt.setDisabledTextColor(Color.BLACK);
        txt.setEditable(false);

        JPanel var2=new JPanel(new GridLayout(0,1));
        JLabel lbl1=new JLabel("XML");
        lbl1.setFont(new Font("Arial", Font.BOLD, 13));
        JButton saveBtn=new JButton("Зберегти");
        JButton loadBtn=new JButton("Завантажити");
        saveBtn.setBackground(new Color(220, 105,236));
        loadBtn.setBackground(new Color(220, 105,236));
        var2.add(lbl1);
        var2.add(saveBtn);
        var2.add(loadBtn);

        JPanel var3=new JPanel(new GridLayout(0,1));
        JLabel lbl2=new JLabel("Показати");
        lbl2.setFont(new Font("Arial", Font.BOLD, 13));
        JButton clientsBtn=new JButton("Клієнти");
        JButton cardsBtn=new JButton("Картки");
        clientsBtn.setBackground(new Color(95, 225, 227));
        cardsBtn.setBackground(new Color(95, 225, 227));
        var3.add(lbl2);
        var3.add(clientsBtn);
        var3.add(cardsBtn);

        JPanel var4=new JPanel(new GridLayout(0,1));
        JLabel lbl3=new JLabel("Звільнити");
        lbl3.setFont(new Font("Arial", Font.BOLD, 13));
        JButton clearConsoleBtn=new JButton("Консоль");
        JButton clearBankBtn=new JButton("Сховище карток");
        clearConsoleBtn.setBackground(new Color(236, 171, 101));
        clearBankBtn.setBackground(new Color(236, 171, 101));
        var4.add(lbl3);
        var4.add(clearBankBtn);
        var4.add(clearConsoleBtn);

        GroupLayout l1 = new GroupLayout(this);
        this.setLayout(l1);
        l1.setAutoCreateGaps(true);
        l1.setAutoCreateContainerGaps(true);
        l1.setHorizontalGroup(l1.createParallelGroup()
                .addComponent(var)
                .addComponent(var1)
                .addComponent(scroll)
                .addGroup(l1.createSequentialGroup()
                        .addComponent(var2)
                        .addComponent(var3)
                        .addComponent(var4)));
        l1.setVerticalGroup(l1.createSequentialGroup()
                .addComponent(var)
                .addComponent(var1)
                .addComponent(scroll).
                        addGroup(l1.createParallelGroup()
                                .addComponent(var2)
                                .addComponent(var3)
                                .addComponent(var4)));

        startBtn.addActionListener(event->{
            if(server!=null&&server.isFinished())server=null;
            if(server==null){
                server=new MetroServer(port);
                server.start();
            }else if(!server.isFinished())
                JOptionPane.showMessageDialog(this, "Сервер працює");
            else JOptionPane.showMessageDialog(this, "Неможливо запустити сервер");
        });

        stopBtn.addActionListener(event->{
            if(server!=null&&!server.isFinished()){
                if(JOptionPane.showConfirmDialog(this, "Зупинити сервер? Незбережені дані буде втрачено")==0){
                    server.finish();
                    JOptionPane.showMessageDialog(this, "Сервер зупинено");
                }
            }else JOptionPane.showMessageDialog(this, "Неможливо зупинити сервер");
        });

        cardsBtn.addActionListener(e -> {
            if(server!=null&&!server.isFinished()){
                server.showCards();
            }else JOptionPane.showMessageDialog(this, "Спочатку потрібно запустити сервер");
        });

        clearConsoleBtn.addActionListener(e->{
            if(server!=null){
                if(txt.getText()==null||txt.getText().isEmpty())
                    JOptionPane.showMessageDialog(this, "Консоль порожня");
                else{
                    if(JOptionPane.showConfirmDialog(this, "Очистити консоль?")==0){
                        txt.setText("");
                        server.resetOutput();
                    }
                }
            }else JOptionPane.showMessageDialog(this, "Консоль порожня");
        });

        clearBankBtn.addActionListener(e->{
            if(server!=null&&!server.isFinished()){
                if(JOptionPane.showConfirmDialog(this, "Ви певні, що хочете видалити усі картки?")==0){
                    server.freeBank();
                    JOptionPane.showMessageDialog(this, "Сховище звільнено");
                }
            }else JOptionPane.showMessageDialog(this, "Вносити зміни можна лише у робочий сервер");
        });

        clientsBtn.addActionListener(e->{
            if(server!=null&&!server.isFinished()){
                if(server.getClientsNum()>0){
                    server.outputClients();
                }else JOptionPane.showMessageDialog(this, "Список клієнтів порожній");
            }else JOptionPane.showMessageDialog(this, "Спочатку потрібно запустити сервер");
        });

        saveBtn.addActionListener(e->{
            if(server!=null&&!server.isFinished()){
                if(JOptionPane.showConfirmDialog(this, "Ви певні, що хочете перезаписати файл?")==0){
                    server.writeXML();
                    JOptionPane.showMessageDialog(this, "Стан сховища збережено");
                }
            }else JOptionPane.showMessageDialog(this, "Спочатку потрібно запустити сервер");
        });

        loadBtn.addActionListener(e->{
            if(server!=null&&!server.isFinished()){
                if(JOptionPane.showConfirmDialog(this, "Завантаження перезапише існуюче сховище. Продовжити?")==0){
                   server.readXML();
                   JOptionPane.showMessageDialog(this, "Дані завантажено");
                }
            }else JOptionPane.showMessageDialog(this, "Спочатку потрібно запустити сервер");
        });

        this.add(var1);
        this.add(scroll);
        this.add(var2);
        this.add(var3);
        this.add(var4);

        class RefreshConsole extends Thread{
            String prevStr="";

            @Override
            public void run() {
                try{
                    while(true){
                        sleep(100);
                        if(server!=null&&!prevStr.equals(server.getOutput())){
                            txt.setText(server.getOutput());
                            prevStr=txt.getText();
                        }
                    }
                }catch (InterruptedException e){
                }
            }
        }

        RefreshConsole rc=new RefreshConsole();
        rc.start();
    }

    public synchronized MetroServer getServer() {
        return server;
    }
}
