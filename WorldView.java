import processing.core.PApplet;
import processing.core.PImage;

import java.util.Optional;

public final class WorldView {
    private PApplet screen;
    private WorldModel world;
    private int tileWidth;
    private int tileHeight;
    public Viewport viewport;
    private Background background;

    public WorldView(int numRows, int numCols, PApplet screen, WorldModel world, int tileWidth, int tileHeight) {
        this.screen = screen;
        this.world = world;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.viewport = new Viewport(numRows, numCols);
    }
    private void drawEntities() {
        for (Entities entity : this.world.getentities()) {
            Point pos = entity.getposition();

            if (this.viewport.contains(pos)) {
                Point viewPoint = this.viewport.worldToViewport(pos.x, pos.y);
                this.screen.image(entity.getCurrentImage(entity), viewPoint.x * this.tileWidth, viewPoint.y * this.tileHeight);
            }
        }
    }
    public Background getbackground(){
        return this.background;
    }
    private void drawBackground() {
        for (int row = 0; row < this.viewport.getnumRows(); row++) {
            for (int col = 0; col < this.viewport.getnumCols(); col++) {
                Point worldPoint = this.viewport.viewportToWorld(col, row);
                Optional<PImage> image = this.world.getBackgroundImage(worldPoint);
                if (image.isPresent()) {
                    this.screen.image(image.get(), col *this.tileWidth, row * this.tileHeight);
                }
            }
        }
    }

    public void drawViewport() {
        drawBackground();
        drawEntities();
    }


}