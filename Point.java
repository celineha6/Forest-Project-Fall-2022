import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * A simple class representing a location in 2D space.
 */
public final class Point {
    public final int x;
    public final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean equals(Object other) {
        return other instanceof Point && ((Point) other).x == this.x && ((Point) other).y == this.y;
    }

    public int hashCode() {
        int result = 17;
        result = result * 31 + x;
        result = result * 31 + y;
        return result;
    }

    public Optional<Entities> nearestEntity(List<Entities> entities) {
        if (entities.isEmpty()) {
            return Optional.empty();
        } else {
            Entities nearest = entities.get(0);
            int nearestDistance = distanceSquared(nearest.getposition());

            for (Entities other : entities) {
                int otherDistance = distanceSquared(other.getposition());

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }

    }
    public boolean withinBounds(WorldModel world) {
        return this.y >= 0 && this.y < world.getnumrows() && this.x >= 0 && this.x < world.getnumcols();
    }
    public boolean adjacent(Point p2) {
        return (this.x == p2.x && Math.abs(this.y - p2.y) == 1) || (this.y == p2.y && Math.abs(this.x - p2.x) == 1);
    }
    public boolean isOccupied(WorldModel world) { //returns true if the cell is within the bounds of the world and the cell is occupied by an entity
        return this.withinBounds(world) && getOccupancyCell(world) != null;
    }
    public Entities getOccupancyCell(WorldModel world) {
        return world.getoccupancy()[this.y][this.x];
    }
    private int distanceSquared(Point p2) {
        int deltaX = this.x - p2.x;
        int deltaY = this.y - p2.y;

        return deltaX * deltaX + deltaY * deltaY;
    }

}