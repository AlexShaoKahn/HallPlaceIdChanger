import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangerWindow implements ActionListener {
    private JPanel rootPanel;
    private JTextField startIdText;
    private JTextField reserveText;
    private JButton getRowsButton;
    private JButton changeIDsButton;
    private JLabel startId;
    private JLabel reserve;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JLabel info;
    private JTextField hallInfo;
    private JButton clearButton;

    public ChangerWindow() {
        JFrame jFrame = new JFrame("Hall PlaceId Changer");
        jFrame.setContentPane(rootPanel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setSize(800, 600);
        jFrame.setVisible(true);

        changeIDsButton.addActionListener(this);
        getRowsButton.addActionListener(this);
        clearButton.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Hall hall = new Hall();
        try {
            hall.parseJsonHall(textArea1.getText());
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        changerPlacesIds(hall);
        if (e.getActionCommand().equals("Change IDs")) {
            textArea2.setText(hall.toJsonString());
        } else if (e.getActionCommand().equals("Get raws")) {
            textArea2.setText(hall.extractSector());
        } else {
            textArea1.setText("");
            textArea2.setText("");
            hallInfo.setText("");
        }
        hallInfo.setText(hall.getIDsRange());
    }

    private void changerPlacesIds(Hall hall) {
        System.out.println(startId.getText());
        System.out.println(reserve.getText());
        if (startIdText.getText().toLowerCase().equals("auto") && reserveText.getText().toLowerCase().equals("auto"))
            hall.changePlacesIds();
        else if (!startIdText.getText().equals("auto") && reserveText.getText().equals("auto"))
            hall.changePlacesIds(Integer.parseInt(startIdText.getText()));
        else if (startIdText.getText().equals("auto") && !reserveText.getText().equals("auto"))
            hall.changePlacesIds(1001, Integer.parseInt(reserveText.getText()));
        else hall.changePlacesIds(Integer.parseInt(startIdText.getText()), Integer.parseInt(reserveText.getText()));
    }
}
