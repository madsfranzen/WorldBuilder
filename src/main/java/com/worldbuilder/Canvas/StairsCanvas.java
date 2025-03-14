package com.worldbuilder.Canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class StairsCanvas extends Canvas {
    private final GraphicsContext gc;
    private final StairsTile[][] tileMap;

    public StairsCanvas(int width, int height) {
        super(width, height);
        this.gc = getGraphicsContext2D();
        this.tileMap = new StairsTile[width][height];
    }

    private static record StairsTile() {}

    private static record TileVariant(String name, int x, int y) {}

    private static final TileVariant[] VARIANTS = {
        new TileVariant("TOP_LEFT", 0, 0),
        new TileVariant("TOP", 1, 0),
        new TileVariant("TOP_RIGHT", 2, 0),
        new TileVariant("LEFT", 0, 1),
        new TileVariant("CENTER", 1, 1),
    };
    
}
