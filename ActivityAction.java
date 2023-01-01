public class ActivityAction implements Action{

    EntityActivityAction entity;
    WorldModel world;
    ImageStore imagestore;

    public ActivityAction(EntityActivityAction entity, WorldModel world, ImageStore imagestore) {

        this.world = world;
        this.imagestore = imagestore;
        this.entity = entity;
    }

    public void executeAction(EventScheduler scheduler)
    {
        entity.executeActivity(this.entity, this.world, this.imagestore, scheduler);
    }
}
