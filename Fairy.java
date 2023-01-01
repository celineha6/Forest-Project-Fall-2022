import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Fairy implements Entities, EntityActivityAction, Moveable{
    private EntityKind kind;
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private double actionPeriod;
    private double animationPeriod;
    private int health;
    private int healthLimit;

    public Fairy(EntityKind kind, String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit) {
        this.kind = kind;
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.health = health;
        this.healthLimit = healthLimit;
    }

    @Override
    public EntityKind getentitykind() {
        return this.kind;
    }
    @Override
    public Point getposition() {
        return this.position;
    }
    @Override
    public PImage getCurrentImage(Entities entity) {
        return this.images.get(this.imageIndex % this.images.size());
    }
    public String log(){
        return this.id.isEmpty() ? null :
                String.format("%s %d %d %d", this.id, this.position.x, this.position.y, this.imageIndex);
    }
    @Override
    public int gethealth() {
        return this.health;
    }
    @Override
    public void sethealth(int health) {
        this.health = health;
    }
    @Override
    public String getid() {
        return this.id;
    }
    public Point setposition(Point position){
        this.position = position;
        return this.position;
    }
    public void nextImage() {
        this.imageIndex = this.imageIndex + 1;
    }

    public void executeActivity(Entities entity, WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entities> fairyTarget = findNearest(world, this.position, new ArrayList<>(List.of(EntityKind.STUMP)));
//        System.out.println("target stump found");

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getposition();
//            System.out.println("is present");

            if (moveToFairy(this, world, fairyTarget.get(), scheduler)) {

                EntityActivityAction sapling = (EntityActivityAction) Functions.createSapling(Functions.SAPLING_KEY + "_" + fairyTarget.get().getid(), tgtPos, Functions.getImageList(imageStore, Functions.SAPLING_KEY), 0);

                world.addEntity(sapling);


                scheduleAction(sapling, world, imageStore, scheduler);

            }
        }

        scheduler.scheduleEvent( this, createActivityAction( this, world, imageStore), this.actionPeriod);
    }

    public double getAnimationPeriod() {
        return this.animationPeriod;

    }
    public double getActionPeriod(){
        return this.actionPeriod;
    }
    public boolean moveToFairy(Entities entity, WorldModel world, Entities target, EventScheduler scheduler) {
        //if the fairy's position is adjacent to the target
        if (entity.getposition().adjacent(target.getposition()))
        {
            //remove the target
            world.removeEntity(scheduler, target);
            //return true (basically signifies the end of the path)
            return true;
        }
        else
        {
            //computes the next point
            List<Point> nextPosList = nextPositionFairy(this, world, target.getposition());
            //if no path can be computed return true
            if (nextPosList.size() == 0){
                return true;
            }
            System.out.println("Fairy's position: "+""+"("+nextPosList.get(0).x +","+ nextPosList.get(0).y+")");
            //need to check if the next point is adjacent to the target. if it is adjacent to the target return true
//            if(nextPosList.get(0).adjacent(target.getposition()))
//            {
//                return true;
//            }

            if (!entity.getposition().equals(nextPosList.get(0)))
            { //if the entity's current position is not equal to the next position to move
                //move the entity into this position
                world.moveEntity(scheduler, this, nextPosList.get(0));
            }
            return false;
        }
    }
    public List<Point> nextPositionFairy(Entities entity, WorldModel world, Point destPos) {
        PathingStrategy ps = new SingleStepPathingStrategy();
        //PARAMETERS
        Predicate<Point> canPassThrough = p->(!p.isOccupied(world)); //a fairy can pass through if the cell is not occupied
        BiPredicate<Point, Point> withinReach = Point::adjacent;
        Function<Point, Stream<Point>> potentialneighbors = ps.CARDINAL_NEIGHBORS;
        //METHOD CALL
        List<Point> path = ps.computePath(entity.getposition(), destPos, canPassThrough,
                withinReach, potentialneighbors); //returns a List of one point since one step is taken
        //returns a list of one point since only one step is taken
        return path; //returns a list of one Point


    }



}



