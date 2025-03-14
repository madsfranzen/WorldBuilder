package com.worldbuilder.Canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class StairsCanvas extends Canvas {
    private final GraphicsContext gc;
    private final StairsTile[][] tileMap;
    private final Image TILESET = new Image(
            GroundCanvas.class.getResourceAsStream("/assets/Terrain/Ground/Tilemap_Elevation.png"));

    public StairsCanvas(int width, int height) {
        super(width, height);
        this.gc = getGraphicsContext2D();
        this.tileMap = new StairsTile[width][height];
    }

    private static record StairsTile() {
    }

    private static record TileVariant(String name, int x, int y) {
    }

    private static final TileVariant[] VARIANTS = {
            new TileVariant("LEFT", 0, 1),
            new TileVariant("CENTER", 1, 1),
            new TileVariant("RIGHT", 2, 1),
            new TileVariant("SOLO", 0, 0)
    };

    public void drawStairs(int x, int y, TileVariant variant) {
        // TODO: STEAL FROM GroundCanvas
    }

    public void deleteStairs(int x, int y) {
        // TODO: STEAL FROM GroundCanvas
    }

    public void updateNeighbors(int x, int y) {
        // TODO: STEAL FROM GroundCanvas
    }
}
