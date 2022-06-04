package metroBank.gui;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

public class InputFrame extends JPanel {

    private JButton okButton;
    private boolean ok;
    private JDialog dialog;
    ArrayList<JTextField> fields;
    ArrayList<JLabel> lbls;

    public InputFrame(ArrayList<String> labels) {
        setLayout(new BorderLayout());
        fields=new ArrayList<>();
        lbls=new ArrayList<>();
        int max=0;
        this.setLayout(new GridLayout(0,1));
        for(int i=0; i<labels.size(); i++){
            JPanel p=new JPanel();
            JLabel lbl=new JLabel(labels.get(i));
            lbl.setFont(new Font("Arial", Font.BOLD,13));
            if(max<lbl.getPreferredSize().width)max=lbl.getPreferredSize().width;
            lbls.add(lbl);
            p.add(lbl);
            fields.add(new JTextField(15));
            p.add(fields.get(i));
            add(p);
        }
        for(JLabel l:lbls)l.setPreferredSize(new Dimension(max, lbls.get(0).getPreferredSize().height));
        okButton = new JButton("Ok");
        okButton.addActionListener(event -> {
            ok = true;
            dialog.setVisible(false);
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(event -> dialog.setVisible(false));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public boolean showDialog(Component parent, String title) {
        ok = false;
        Frame owner;
        if (parent instanceof Frame)
            owner = (Frame) parent;
        else
            owner = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, parent);
        if (dialog == null || dialog.getOwner() != owner) {
            dialog = new JDialog(owner, true);
            dialog.add(this);
            dialog.getRootPane().setDefaultButton(okButton);
            dialog.pack();
        }
        dialog.setTitle(title);
        dialog.setVisible(true);
        return ok;
    }

    public String getField(int ind){
        ind--;
        if(ind<=fields.size()&&!fields.get(ind).getText().isEmpty()){
            return fields.get(ind).getText();
        }return null;
    }
}