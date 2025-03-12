package com.worldbuilder;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Tile {



    private final Image image;
    private final TileType tileType;

    public Tile(Image image, TileType tileType) {
        this.image = image;
        this.tileType = tileType;
    }

    public void draw(GraphicsContext gc, int x, int y) {
        gc.drawImage(image, x, y);
    }

    public TileType getTileType() {
        return tileType;
    }

}
