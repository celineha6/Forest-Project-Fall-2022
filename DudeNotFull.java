import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeNotFull implements Entities, EntityActivityAction, Moveable{
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

    public DudeNotFull(EntityKind kind, String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit) {
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
        Optional<Entities> target = findNearest(world, this.position, new ArrayList<>(Arrays.asList(EntityKind.TREE, EntityKind.SAPLING)));

        if (target.isEmpty() || !moveTofull(world, target.get(), scheduler) || !transform(world, scheduler, imageStore)) {
            scheduler.scheduleEvent( this, createActivityAction( this, world, imageStore), this.actionPeriod);
        }
    }
    public boolean moveTofull(WorldModel world, Entities target, EventScheduler scheduler) {
        if (this.getposition().adjacent(target.getposition()))  //if the dudenotfull is adjacent to the target return true
        {
            this.resourceCount += 1;
            int healthcurrent = target.gethealth();
            int health = healthcurrent-1;
            target.sethealth(health);
            return true;
        }
        else
        {
            List<Point> nextPosList = nextPosition(this, world, target.getposition());
            if (nextPosList.size() == 0)
            {
                return true;

            }
            if (!this.position.equals(nextPosList.get(0))) {
                world.moveEntity(scheduler, this, nextPosList.get(0));
            }
            return false;
        }
    }
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.resourceCount >= this.resourceLimit) {
            DudeFull dudefull = new DudeFull(EntityKind.DUDE_FULL, id, position, images, resourceLimit, 0, actionPeriod, animationPeriod, 0, 0);

            world.removeEntity( scheduler, this);
            scheduler.unscheduleAllEvents( this);

            world.addEntity( dudefull);
            scheduleAction(dudefull, world, imageStore, scheduler);

            return true;
        }

        return false;
    }
    public double getAnimationPeriod() {
        return this.animationPeriod;

    }
    public double getActionPeriod(){
        return this.actionPeriod;
    }




}



