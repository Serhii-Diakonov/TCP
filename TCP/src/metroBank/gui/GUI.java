package metroBank.gui;

import javax.swing.*;
import java.awt.*;

public class GUI {
    public static void main(String[]args){

        JFrame frame=new JFrame("Метро");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ServerPanel server=new ServerPanel(7898);
        ClientPanel clients=new ClientPanel(server);

        JPanel panel = new JPanel();
        JRadioButton btn1 = new JRadioButton("Сервер");
        JRadioButton btn2 = new JRadioButton("Клієнти");
        ButtonGroup btn_group = new ButtonGroup();
        btn1.setSelected(true);
        btn_group.add(btn1);
        btn_group.add(btn2);

        panel.add(btn1);
        panel.add(btn2);
        frame.add(panel, BorderLayout.NORTH);
        frame.add(server);

        btn1.addActionListener(event -> {
            frame.remove(clients);
            server.setVisible(true);
            frame.repaint();
        });

        btn2.addActionListener(event -> {
            server.setVisible(false);
            frame.add(clients);
            frame.repaint();
        });

        String plaf = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
        try {
            UIManager.setLookAndFeel(plaf);
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception e) { e.printStackTrace (); }
        frame.setSize(720, 480);
        frame.setVisible(true);
    }
}
