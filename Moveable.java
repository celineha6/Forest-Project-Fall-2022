import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;


public interface Moveable extends EntityActivityAction{

    default Optional<Entities> findNearest(WorldModel world, Point pos, List<EntityKind> kinds) {
        List<Entities> ofType = new LinkedList<>();
        for (EntityKind kind : kinds) {
            for (Entities entity : world.getentities()) {
                if (entity.getentitykind() == kind) {
                    ofType.add(entity);
                }
            }
        }

        return pos.nearestEntity(ofType);
    }
    default boolean moveTo(Entities entity, WorldModel world, Entities target, EventScheduler scheduler) {
        if (entity.getposition().adjacent(target.getposition()))
        { //if the dude is adjacent to the target return true
            return true;
        }
        else
        {
            List<Point> nextPosList = nextPosition(entity, world, target.getposition());
            if (nextPosList.size() == 0)
            {             //if compute path cannot find a path return true
                return true;
            }
            if (!(entity.getposition().equals(nextPosList.get(0))))
            {          //if the next position is not equal to the dude's current position
                world.moveEntity(scheduler, this, nextPosList.get(0));
            }
            return false;
        }
    }
    default List<Point> nextPosition(Entities entity, WorldModel world, Point destPos) {
        PathingStrategy ps = new SingleStepPathingStrategy();
        //parameters
        Predicate<Point> canPassThrough = p->(!p.isOccupied(world) || p.getOccupancyCell(world) instanceof Stump); //dudes can pass through cells that are unocuppied or cells that are stymps
        BiPredicate<Point, Point> withinReach = Point::adjacent;
        Function<Point, Stream<Point>> potentialneighbors = ps.CARDINAL_NEIGHBORS;
        //method call
        List<Point> path = ps.computePath(entity.getposition(), destPos, canPassThrough,
                withinReach, potentialneighbors); //returns a List of one point since one step is taken
        return path;

    }



}
