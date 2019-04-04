import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {

    private static final int BOARD_WIDTH = 12; //ile klockow na szerokosc
    private static final int BOARD_HEIGHT = 22; //ile klockow na wysokosc
    private Timer timer;
    private boolean isFallingFinished = false; //czy usunieto wszystkie linie do dolu
    private boolean isStarted = false; //czy gra rozpoczeta
    private boolean isPaused = false; //czy zapauzowane
    private int numLinesRemoved = 0; //ilosc lini usunietych
    private int curX = 0;
    private int curY = 0;
    private Shape curPiece; //ksztalt
    private Blocks.Tetrominoes[] board;


    public Board() {
        setFocusable(true);
        curPiece = new Shape();
        timer = new Timer(400, this); // timer for lines down

        board = new Blocks.Tetrominoes[BOARD_WIDTH * BOARD_HEIGHT];
        clearBoard();
        addKeyListener(new MyTetrisAdapter());
    }


    public int squareWidth() { //szerokosc pojedynczego kwadracika
        return (int) (getSize().getWidth() / BOARD_WIDTH); //szerokosc ekranu / szerokosc
    }

    public int squareHeight() {
        return (int) getSize().getHeight() / BOARD_HEIGHT;
    }

    public Blocks.Tetrominoes shapeAt(int x, int y) { //przyjmuje 2 wymiary w tablicy, a zwraca nam jeden jako pozycje na planszy
        return board[y * BOARD_WIDTH + x]; //zwraca klocek, wysokosc * ilosc kwadratow na szerokosc + szerokosc, czyli jedna kolumna + szerokosc
    }

    private void clearBoard() { //przechodzimy po wszystkim komorkach i czyscimy
        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; i++) {
            board[i] = Blocks.Tetrominoes.NoShape;
        }
    }

    private void pieceDropped() { //przesuwamy wszystkie ksztalty o jeden w dol
        for (int i = 0; i < 4; i++) {
            int x = curX + curPiece.x(i);
            int y = curY - curPiece.y(i);
            board[y * BOARD_WIDTH + x] = curPiece.getShape(); //nowa pozycja w tablicy(na planszy)
        }

        removeFullLines();

        if (!isFallingFinished) { //jak dojdze do dolu to nowy ksztalt
            newPiece();
        }
    }

    public void newPiece() { //wybieramy nowy ksztalt
        curPiece.setRandomShape();
        //pozycje startowe
        curX = BOARD_WIDTH / 2 + 1;
        curY = BOARD_HEIGHT - 1 + curPiece.minY();

        if (!tryMove(curPiece, curX, curY - 1)) { //jezeli nie mozna dostawic juz klocka, koniec gry
            curPiece.setShape(Blocks.Tetrominoes.NoShape);
            timer.stop();
            isStarted = false;
            Legend.statusBar.setText("Game Over");
        }
    }

    private void oneLineDown() { //jedna linia w dol skasowana
        if (!tryMove(curPiece, curX, curY - 1))
            pieceDropped();
    }

    @Override
    public void actionPerformed(ActionEvent ae) { //
        if (isFallingFinished) {   //czy dolecialo do dolu
            isFallingFinished = false;
            newPiece();
        } else {
            oneLineDown();
        }
    }

    private void drawSquare(Graphics g, int x, int y, Blocks.Tetrominoes shape) { //rysujemy kwadraty z kolorow i kreski
        Color color = shape.color;
        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2); // rozmiary kwadratow

        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);
        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1, x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1, x + squareWidth() - 1, y + 1);
    }

    @Override
    public void paint(Graphics g) { //rysujemy je na planszy
        super.paint(g);
        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - BOARD_HEIGHT * squareHeight();

        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; ++j) {
                Blocks.Tetrominoes shape = shapeAt(j, BOARD_HEIGHT - i - 1);

                if (shape != Blocks.Tetrominoes.NoShape) { //jezeli ksztalt nie jest pustym kwadratem to rysujemy
                   drawSquare(g, j * squareWidth(), boardTop + i * squareHeight(), shape); //rysujemy kwadracik
                }
            }
        }

//rysowanie kwadratow przy przesuwaniu
        if (curPiece.getShape() != Blocks.Tetrominoes.NoShape) { //jezeli pobierany ksztalt nie jest pustym kwadratem
            for (int i = 0; i < 4; ++i) {
                int x = curX + curPiece.x(i);
                int y = curY - curPiece.y(i);
                drawSquare(g, x * squareWidth(), boardTop + (BOARD_HEIGHT - y - 1) * squareHeight(), curPiece.getShape());
            }
       }
    }

    public void start() {
        if (isPaused)
            return;

        isStarted = true;
        isFallingFinished = false;
        numLinesRemoved = 0;
        clearBoard();
        newPiece();
        timer.start();
    }

    public void pause() {
        if (!isStarted)
            return;

        isPaused = !isPaused;

        if (isPaused) {
            timer.stop();
            Legend.statusBar.setText("Paused");
        } else {
            timer.start();
            Legend.statusBar.setText(String.valueOf("Ilosc punktow: " + numLinesRemoved));
        }

        repaint();
    }

    private boolean tryMove(Shape newPiece, int newX, int newY) { //probuj przesunac, jezeli zmiesci sie na planszy
        for (int i = 0; i < 4; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);

            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT)
                return false;

            if (shapeAt(x, y) != Blocks.Tetrominoes.NoShape)
                return false;
        }

        curPiece = newPiece;
        curX = newX;
        curY = newY;
        repaint();

        return true;
    }

    private void removeFullLines() { //usuwamy cala linie
        int numFullLines = 0;

        for (int i = BOARD_HEIGHT - 1; i >= 0; --i) {
            boolean lineIsFull = true;

            for (int j = 0; j < BOARD_WIDTH; ++j) {
                if (shapeAt(j, i) == Blocks.Tetrominoes.NoShape) {
                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {
                ++numFullLines;

                for (int k = i; k < BOARD_HEIGHT - 1; ++k) {
                    for (int j = 0; j < BOARD_WIDTH; ++j) {
                        board[k * BOARD_WIDTH + j] = shapeAt(j, k + 1);
                    }
                }
            }

            if (numFullLines > 0) {
                numLinesRemoved += numFullLines;
                Legend.statusBar.setText(String.valueOf("Ilosc punktow: " + numLinesRemoved));
                isFallingFinished = true;
                curPiece.setShape(Blocks.Tetrominoes.NoShape);
                repaint();
            }
        }
    }

    private void dropDown() { //opuszczenie klocka o 1
        int newY = curY;

        while (newY > 0) {
            if (!tryMove(curPiece, curX, newY - 1))
                break;

            --newY;
        }

        pieceDropped();
    }

    class MyTetrisAdapter extends KeyAdapter { //obsluga przyciskow
        @Override
        public void keyPressed(KeyEvent ke) {
            if (!isStarted || curPiece.getShape() == Blocks.Tetrominoes.NoShape)
                return;

            int keyCode = ke.getKeyCode();

            if (keyCode == 'p' || keyCode == 'P')
                pause();

            if (isPaused)
                return;

            switch (keyCode) {
                case KeyEvent.VK_LEFT:
                    tryMove(curPiece, curX - 1, curY);
                    break;
                case KeyEvent.VK_RIGHT:
                    tryMove(curPiece, curX + 1, curY);
                    break;
                case KeyEvent.VK_DOWN:
                    tryMove(curPiece.rotateRight(), curX, curY);
                    break;
                case KeyEvent.VK_UP:
                    tryMove(curPiece.rotateLeft(), curX, curY);
                    break;
                case KeyEvent.VK_SPACE:
                    dropDown();
                    break;
                case 'd':
                case 'D':
                    oneLineDown();
                    break;
            }

        }
    }

}