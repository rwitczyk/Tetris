import javax.swing.*;
import java.awt.*;

public class Tetris extends JFrame {

    private JLabel statusBar;

    public Tetris() {
     //   statusBar = new JLabel("Ilosc punktow: 0");
 //       GridBagLayout gridbag = new GridBagLayout();
 //       GridBagConstraints c = new GridBagConstraints();
  //      c.fill = GridBagConstraints.HORIZONTAL;
       setLayout(new GridLayout(1, 2));
       // add(statusBar, BorderLayout.SOUTH);
/*
        Board board = new Board(this);
        c.gridy = 0;
        c.gridx = 0;
        c.gridwidth = 2;
        gridbag.setConstraints(board,c);
        add(board);


        Legend legenda = new Legend();
        c.gridy = 0;
        c.gridx = 2;
        gridbag.setConstraints(legenda,c);
        add(legenda);
*/
        Board board = new Board();
        add(board);
        Legend legenda = new Legend();
        add(legenda);
        board.start();
        setSize(600, 600);
        setTitle("Tetris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Tetris myTetris = new Tetris();
        myTetris.setLocationRelativeTo(null);
        myTetris.setVisible(true);
    }

}