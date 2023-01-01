import java.util.Comparator;

public class WorldNodeComparator implements Comparator<WorldNode> {


    @Override
    public int compare(WorldNode wn1, WorldNode wn2) {
        return (wn1.g+wn1.h)-(wn2.g+wn2.h);
        /*
        if this worldnode has a better f: compareto>0
        if this worldnode is equal in f: compareto=0
        if this worldnode has a worse f: compareto<0
         */
    }
}
