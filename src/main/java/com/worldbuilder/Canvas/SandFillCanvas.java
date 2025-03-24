package com.worldbuilder.Canvas;

import com.worldbuilder.SpriteLoader;
import com.worldbuilder.debug.DebugInfo;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SandFillCanvas extends Canvas {
    private final GraphicsContext gc = getGraphicsContext2D();

    private final Image sandTile = SpriteLoader.getGroundTileset();
    private final String[][] tileMap;


    public SandFillCanvas(int width, int height) {
        super(width, height);
        tileMap = new String[width][height];
    }

    public void drawSandFill(int x, int y) {
        gc.drawImage(sandTile, 9 * 64, 0 * 64, 64, 64, x * 64, y * 64, 64, 64);
        tileMap[x][y] = "SANDFILL";
        DebugInfo.setLastAction("Painted SANDFILL at (" + x + ", " + y + ")");
    }

    public void deleteSandFill(int currentTileX, int currentTileY) {
        gc.clearRect(currentTileX * 64, currentTileY * 64, 64, 64);
        tileMap[currentTileX][currentTileY] = null;
        DebugInfo.setLastAction("Deleted SANDFILL at (" + currentTileX + ", " + currentTileY + ")");
    }

    public String[][] getTileMap() {
        return tileMap;
    }
}
