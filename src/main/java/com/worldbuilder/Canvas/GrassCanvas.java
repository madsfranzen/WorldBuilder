package com.worldbuilder.Canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GrassCanvas extends Canvas {

    public GrassCanvas(int width, int height) {
        super(width, height);
    }

    public void drawGrass(int x, int y) {
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        gc.fillRect(x * 64, y * 64, 64, 64);  // Multiply by 64 to match tile size
    }
}
