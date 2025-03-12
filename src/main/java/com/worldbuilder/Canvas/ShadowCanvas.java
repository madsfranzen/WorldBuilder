package com.worldbuilder.Canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ShadowCanvas extends Canvas {

    private final Image shadowTile = new Image(getClass().getResourceAsStream("/assets/Terrain/Ground/Shadows.png"));

    public ShadowCanvas(int width, int height) {
        super(width, height);
    }

    public void drawShadow(int x, int y) {
        GraphicsContext gc = getGraphicsContext2D();
        int offsetX = 64;
        int offsetY = 64;
        gc.drawImage(shadowTile, x * 64 - offsetX, y * 64 - offsetY, 64 * 3, 64 * 3);
    }
}
