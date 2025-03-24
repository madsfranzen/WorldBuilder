package com.worldbuilder.Canvas;

import com.worldbuilder.SpriteLoader;
import com.worldbuilder.debug.DebugInfo;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class GrassFillCanvas extends Canvas {
    private final GraphicsContext gc = getGraphicsContext2D();

    private final Image grassTile = SpriteLoader.getGroundTileset();
    private final String[][] tileMap;


    public GrassFillCanvas(int width, int height) {
        super(width, height);
        tileMap = new String[width][height];
    }

    public void drawGrassFill(int x, int y) {
        gc.drawImage(grassTile, 4 * 64, 0 * 64, 64, 64, x * 64, y * 64, 64, 64);
        tileMap[x][y] = "GRASSFILL";
        DebugInfo.setLastAction("Painted GRASSFILL at (" + x + ", " + y + ")");
    }

    public void deleteGrassFill(int currentTileX, int currentTileY) {
        gc.clearRect(currentTileX * 64, currentTileY * 64, 64, 64);
        tileMap[currentTileX][currentTileY] = null;
        DebugInfo.setLastAction("Deleted GRASSFILL at (" + currentTileX + ", " + currentTileY + ")");
    }

    public String[][] getTileMap() {
        return tileMap;
    }
}
