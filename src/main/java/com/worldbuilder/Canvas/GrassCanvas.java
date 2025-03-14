package com.worldbuilder.Canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import com.worldbuilder.debug.DebugInfo;
public class GrassCanvas extends Canvas {
    private final GraphicsContext gc = getGraphicsContext2D();

    public GrassCanvas(int width, int height) {
        super(width, height);
    }

    public void drawGrass(int x, int y) {
        gc.setFill(Color.GREEN);
        gc.fillRect(x * 64, y * 64, 64, 64);  // Multiply by 64 to match tile size
        DebugInfo.setLastAction("Painted GRASS at (" + x + ", " + y + ")");
    }

    public void deleteGrass(int x, int y) {
        gc.clearRect(x * 64, y * 64, 64, 64);
        DebugInfo.setLastAction("Deleted GRASS at (" + x + ", " + y + ")");
    }
}
