import processing.core.PImage;

import java.util.List;

public interface Entities extends ScheduleActions{
    public EntityKind getentitykind();
    public Point getposition();
    public PImage getCurrentImage(Entities entity);
    public String log();

    public int gethealth();

    public void sethealth(int health);

    String getid();

    public Point setposition(Point point);
    public void nextImage();

    default Action createAnimationAction(Entities entity, int repeatCount)
    {
        return new AnimationAction(entity, repeatCount);
    }

    public double getAnimationPeriod();

}
