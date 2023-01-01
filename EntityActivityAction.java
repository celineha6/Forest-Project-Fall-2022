public interface EntityActivityAction extends Entities{


    public void executeActivity(Entities entity, WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    public double getActionPeriod();

    default Action createActivityAction(EntityActivityAction entity, WorldModel world, ImageStore imageStore)
    {
        return new ActivityAction(entity, world, imageStore);
    }


}
