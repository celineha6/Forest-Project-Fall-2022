import processing.core.PImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the world.
 */
public final class WorldModel {
    private int numRows;
    private int numCols;
    private Background[][] background;
    private Entities[][] occupancy;
    private Set<Entities> entities;

    public WorldModel() {

    }

    /**
     * Helper method for testing. Don't move or modify this method.
     */
    public List<String> log(){
        List<String> list = new ArrayList<>();
        for (Entities entity : entities) {
            String log = entity.log();
            if(log != null) list.add(log);
        }
        return list;
    }


    private void removeEntityAt(Point pos) {
        if (pos.withinBounds(this) && pos.getOccupancyCell(this) != null) {
            Entities entity = pos.getOccupancyCell(this);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            Point point =  new Point(-1, -1);
            entity.setposition(point);
            this.entities.remove(entity);
            setOccupancyCell(pos, null);
        }
    }
    public Background getBackgroundCell(Point pos) {
        return this.background[pos.y][pos.x];
    }

    private void setOccupancyCell(Point pos, Entities entity) {
        this.occupancy[pos.y][pos.x] = entity;
    }
    private Optional<Entities> getOccupant(Point pos) {
        if (pos.isOccupied(this)) {
            return Optional.of(pos.getOccupancyCell(this));
        } else {
            return Optional.empty();
        }
    }
    public void addEntity( Entities entity) {
        if (entity.getposition().withinBounds(this)) {
            this.setOccupancyCell(entity.getposition(), entity);
            this.entities.add(entity);
        }
    }

    public void moveEntity(EventScheduler scheduler, Entities entity, Point pos) {
        Point oldPos = entity.getposition();
        if (pos.withinBounds(this) && !pos.equals(oldPos)) {
            setOccupancyCell(oldPos, null);
            Optional<Entities> occupant = getOccupant(pos);
            occupant.ifPresent(target -> removeEntity(scheduler, target));
            setOccupancyCell(pos, entity);
            entity.setposition(pos);
        }
    }
    public void removeEntity(EventScheduler scheduler, Entities entity) {
        scheduler.unscheduleAllEvents(entity);
        removeEntityAt(entity.getposition());
    }
    public void load(Scanner saveFile, ImageStore imageStore, Background defaultBackground){
        parseSaveFile(this, saveFile, imageStore, defaultBackground);
        if(this.background == null){
            this.background = new Background[this.numRows][this.numCols];
            for (Background[] row : this.background)
                Arrays.fill(row, defaultBackground);
        }
        if(this.occupancy == null){
            this.occupancy = new Entities[this.numRows][this.numCols];
            this.entities = new HashSet<>();
        }
    }
    public void tryAddEntity(Entities entity) {
        if (entity.getposition().isOccupied(this)) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        this.addEntity(entity);
    }

    public static void parseSaveFile(WorldModel world, Scanner saveFile, ImageStore imageStore, Background defaultBackground){
        String lastHeader = "";
        int headerLine = 0;
        int lineCounter = 0;
        while(saveFile.hasNextLine()){
            lineCounter++;
            String line = saveFile.nextLine().strip();
            if(line.endsWith(":")){
                headerLine = lineCounter;
                lastHeader = line;
                switch (line){
                    case "Backgrounds:" -> world.background = new Background[world.numRows][world.numCols];
                    case "Entities:" -> {
                        world.occupancy = new Entities[world.numRows][world.numCols];
                        world.entities = new HashSet<>();
                    }
                }
            }else{
                switch (lastHeader){
                    case "Rows:" -> world.numRows = Integer.parseInt(line);
                    case "Cols:" -> world.numCols = Integer.parseInt(line);
                    case "Backgrounds:" -> parseBackgroundRow(world, line, lineCounter-headerLine-1, imageStore);
                    case "Entities:" -> Functions.parseEntity(world, line, imageStore);
                }
            }
        }
    }

    public static void parseBackgroundRow(WorldModel world, String line, int row, ImageStore imageStore) {
        String[] cells = line.split(" ");
        if(row < world.numRows){
            int rows = Math.min(cells.length, world.numCols);
            for (int col = 0; col < rows; col++){
                world.background[row][col] = new Background(cells[col], Functions.getImageList(imageStore, cells[col]));
            }
        }
    }
    public int getnumrows(){
        return this.numRows;
    }
    public int getnumcols(){
        return this.numCols;
    }
    public Entities[][] getoccupancy(){
        return this.occupancy;
    }
    public Set<Entities> getentities(){
        return this.entities;
    }

    public Optional<PImage> getBackgroundImage(Point pos) {
        if (pos.withinBounds(this)) {
            return Optional.of(this.getCurrentImage(getBackgroundCell(pos)));
        } else {
            return Optional.empty();
        }

    }
    public static PImage getCurrentImage(Background background) {

        return background.getimages().get(background.getImageIndex());
    }

}