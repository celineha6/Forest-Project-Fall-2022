public final class Viewport {
    private int row;
    private int col;
    private int numRows;
    private int numCols;
    private WorldModel world;

    public Viewport(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
    }


    public int clamp(int value, int low, int high) {
        return Math.min(high, Math.max(value, low));
    }
    public void shift(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public boolean contains(Point p) {
        return p.y >= this.row && p.y < this.row + this.numRows && p.x >= this.col && p.x < this.col + this.numCols;
    }
    public Point viewportToWorld(int col, int row) {
        return new Point(col + this.col, row + this.row);
    }
    public Point worldToViewport(int col, int row) {
        return new Point(col - this.col, row - this.row);
    }

    public int getnumRows(){
        return this.numRows;
    }
    public int getnumCols(){
        return this.numCols;
    }

    public int getcol() {
        return this.col;
    }

    public int getrow() {
        return this.row;
    }

}