/**
 * An event is made up of an Entity that is taking an
 * Action a specified time.
 */
public final class Event {
    public Action action;
    public double time;
    public Entities entity;

    public Event(Action action, double time, Entities entity) {
        this.action = action;
        this.time = time;
        this.entity = entity;
    }
}