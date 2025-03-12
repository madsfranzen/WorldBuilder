package com.worldbuilder.Canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class WaterCanvas extends Canvas {
    private final Image waterTile = new Image(getClass().getResourceAsStream("/assets/Terrain/Water/Water.png"));

    public WaterCanvas(int width, int height) {
        super(width, height);
    }

    public void drawWater(int x, int y) {
        GraphicsContext gc = getGraphicsContext2D();
        gc.drawImage(waterTile, x * 64, y * 64, 64, 64);
    }
}
