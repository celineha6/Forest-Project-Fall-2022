

/**
 * An action that can be taken by an entity
 */
public class AnimationAction implements Action {

    private Entities entity;
    private int repeatCount;

    public AnimationAction(Entities entity, int repeatCount) {

        this.entity = entity;
        this.repeatCount = repeatCount;
    }



    public void executeAction(EventScheduler scheduler) {
            this.entity.nextImage();

            if (this.repeatCount != 1) {
                scheduler.scheduleEvent( this.entity, this.entity.createAnimationAction(this.entity,Math.max(this.repeatCount - 1, 0)), this.entity.getAnimationPeriod());
            }
    }


}

