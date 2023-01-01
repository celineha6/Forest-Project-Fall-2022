import javax.swing.text.html.parser.Entity;

public interface ScheduleActions
{
    default void scheduleAction(EntityActivityAction entity, WorldModel world, ImageStore imageStore, EventScheduler scheduler){
        if (entity instanceof Obstacle){

            scheduler.scheduleEvent(entity, entity.createAnimationAction(entity, 0), entity.getAnimationPeriod());
        }
        else
        {

            scheduler.scheduleEvent(entity, entity.createActivityAction(entity, world, imageStore), entity.getActionPeriod());
            scheduler.scheduleEvent(entity, entity.createAnimationAction(entity, 0), entity.getAnimationPeriod());
        }
    }
}
