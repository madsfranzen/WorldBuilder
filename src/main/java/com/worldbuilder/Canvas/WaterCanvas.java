package com.worldbuilder.Canvas;

import com.worldbuilder.SpriteLoader;
import com.worldbuilder.debug.DebugInfo;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class WaterCanvas extends Canvas {
    private final GraphicsContext gc = getGraphicsContext2D();

    private final Image waterTile = SpriteLoader.getWaterTile();
    private final boolean[][] tileMap;


    public WaterCanvas(int width, int height) {
        super(width, height);
        tileMap = new boolean[width][height];
    }

    public void drawWater(int x, int y) {
        gc.drawImage(waterTile, x * 64, y * 64, 64, 64);
        tileMap[x][y] = true;
        DebugInfo.setLastAction("Painted WATER at (" + x + ", " + y + ")");
    }

    public void deleteWater(int currentTileX, int currentTileY) {
        gc.clearRect(currentTileX * 64, currentTileY * 64, 64, 64);
        tileMap[currentTileX][currentTileY] = false;
        DebugInfo.setLastAction("Deleted WATER at (" + currentTileX + ", " + currentTileY + ")");
    }

    public boolean[][] getTileMap() {
        return tileMap;
    }
}
