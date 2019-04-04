import javax.swing.*;
import java.awt.*;

public class Legend extends JPanel {

    public static JLabel statusBar;

    public Legend() {
        setSize(300, 100);
        setBackground(Color.lightGray);
        JLabel text = new JLabel ("             Legenda:");
        JLabel left = new JLabel (" <-  -  ruch w lewo");
        JLabel right = new JLabel (" ->  -  ruch w prawo");
        JLabel obrot = new JLabel (" ^ - Obrót ");
        JLabel pause = new JLabel (" P  -  pauza");
        JLabel faster = new JLabel (" D  -  szybciej");
        JLabel drop = new JLabel (" Spacja  -  natychmiastowe opuszczenie");
        JLabel empty1 = new JLabel ("");
        JLabel empty2 = new JLabel ("");
        statusBar = new JLabel(" Ilosc punktow: 0");
        statusBar = getStatusBar();

        setLayout(new GridLayout(10, 1));
        add(text);
        add(left);
        add(right);
        add(obrot);
        add(pause);
        add(faster);
        add(drop);
        add(empty1);
        add(empty2);
        add(statusBar);
    }

    public JLabel getStatusBar() {
        return statusBar;
    }
}
