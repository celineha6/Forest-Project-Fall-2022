import static java.lang.Math.abs;

public class WorldNode {
    public int h;
    public int g;
    public Point position;
    public WorldNode prev;

    public WorldNode(int h, int g,Point position,WorldNode prev ){
        this.h = h;
        this.g = g;
        this.position = position;
        this.prev = prev;
    }
    public static WorldNode pointtoworldnode(Point startpos, Point currpos, Point destpos, WorldNode prev){
        int hval = abs(currpos.x-destpos.x) + abs(currpos.y+destpos.y);
        int gval = prev.g+1; //prevnode dist from start+1
        return new WorldNode(hval, gval, currpos, prev);

    }

}
