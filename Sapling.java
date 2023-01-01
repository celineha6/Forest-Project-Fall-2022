import processing.core.PImage;

import java.util.List;

public class Sapling implements EntityActivityAction {
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

    public Sapling(EntityKind kind, String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit) {
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
        System.out.println("Sapling health increases");
        this.health++;

        if (!transform(world, scheduler, imageStore)) {
            scheduler.scheduleEvent( this, createActivityAction(this, world, imageStore), this.actionPeriod);
        }
    }

    public boolean transform( WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.health <= 0) {
            System.out.println("Sapling turning into stump");
            System.out.println(this.healthLimit);
            Entities stump = Functions.createStump(Functions.STUMP_KEY + "_" + this.id, this.position, Functions.getImageList(imageStore, Functions.STUMP_KEY));

            world.removeEntity( scheduler, this);

            world.addEntity( stump);

            return true;
        }
        else if (this.health >= this.healthLimit) {
            System.out.println("Sapling turning into tree");
            EntityActivityAction tree = (EntityActivityAction) Functions.createTree(Functions.TREE_KEY + "_" + this.id, this.position, Functions.getNumFromRange(Functions.TREE_ACTION_MAX, Functions.TREE_ACTION_MIN), Functions.getNumFromRange(Functions.TREE_ANIMATION_MAX, Functions.TREE_ANIMATION_MIN), Functions.getIntFromRange(Functions.TREE_HEALTH_MAX, Functions.TREE_HEALTH_MIN), Functions.getImageList(imageStore, Functions.TREE_KEY));

            world.removeEntity( scheduler, this);

            world.addEntity( tree);
            scheduleAction(tree,  world, imageStore, scheduler);

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



