import java.util.List;
import java.util.Optional;

import processing.core.PImage;

/**
 * Represents a background for the 2D world.
 */
public final class Background {
    private final String id;
    private final List<PImage> images;
    private int imageIndex;
    private WorldModel world;

    public Background(String id, List<PImage> images) {
        this.id = id;
        this.images = images;
    }


    public PImage getCurrentImage(Background background) {

        return background.images.get(background.imageIndex);
    }

    public int getImageIndex() {
        return this.imageIndex;
    }
    public List<PImage> getimages(){
        return this.images;
    }



}