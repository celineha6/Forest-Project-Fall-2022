import processing.core.PImage;

import java.util.List;

public class Obstacle implements Entities, EntityActivityAction {
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

    public Obstacle(EntityKind kind, String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit) {

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

    public double getAnimationPeriod() {
        return this.animationPeriod;

    }


    @Override
    public void executeActivity(Entities entity, WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

    }

    @Override
    public double getActionPeriod() {
        return this.actionPeriod;
    }
    public Action createAnimationAction(Entities entity, int repeatCount)
    {
        return new AnimationAction(entity, repeatCount);
    }
}



