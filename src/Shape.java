import java.util.Random;

public class Shape {
    private Blocks.Tetrominoes pieceShape;
    private int[][] coords;

    public Shape() {
        coords = new int[4][2];
        setShape(Blocks.Tetrominoes.NoShape);
    }

    public void setShape(Blocks.Tetrominoes shape) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; ++j) {
                coords[i][j] = shape.coords[i][j];
            }
        }

        pieceShape = shape; //ustawiamy nasz wybrany klocek
    }

    private void setX(int index, int x) {
        coords[index][0] = x;
    }

    private void setY(int index, int y) {
        coords[index][1] = y;
    }

    public int x(int index) {
        return coords[index][0];
    }

    public int y(int index) {
        return coords[index][1];
    }

    public Blocks.Tetrominoes getShape() {
        return pieceShape;
    }

    public void setRandomShape() { //losujemy nowy ksztalt
        Random r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;
        Blocks.Tetrominoes[] values = Blocks.Tetrominoes.values();
        setShape(values[x]);
    }
/*
    public int minX() { //minimum z 0,0 i coords
        int m = coords[0][0];

        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coords[i][0]);
        }

        return m;
    }*/

    public int minY() {
        int m = coords[0][1];

        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coords[i][1]);
        }

        return m;
    }

    public Shape rotateLeft() {
        if (pieceShape == Blocks.Tetrominoes.SquareShape) //jezeli kwadrat to nie musimy nic robic
            return this;

        Shape result = new Shape();
        result.pieceShape = pieceShape; // nasz nowy klocek przypisujemy do obecnego

        for (int i = 0; i < 4; i++) {
            result.setX(i, y(i));
            result.setY(i, -x(i));
        }

        return result;
    }

    public Shape rotateRight() {
        if (pieceShape == Blocks.Tetrominoes.SquareShape) //jezeli kwadrat to nie musimy nic robic
            return this;

        Shape result = new Shape();
        result.pieceShape = pieceShape; // nasz nowy klocek przypisujemy do obecnego

        for (int i = 0; i < 4; i++) {
            result.setX(i, -y(i));
            result.setY(i, x(i));
        }

        return result;
    }

}