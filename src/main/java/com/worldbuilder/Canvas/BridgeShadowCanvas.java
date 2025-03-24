package com.worldbuilder.Canvas;

import com.worldbuilder.SpriteLoader;
import com.worldbuilder.debug.DebugInfo;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BridgeShadowCanvas extends Canvas {
    private final GraphicsContext gc = getGraphicsContext2D();

    private final Image TILESET = SpriteLoader.getBridgeTileset();
    private final boolean[][] tileMap;


    public BridgeShadowCanvas(int width, int height) {
        super(width, height);
        tileMap = new boolean[width][height];
    }

    public void drawBridgeShadow(int x, int y) {
        if (tileMap[x][y]) {
            return;
        }
        gc.drawImage(TILESET, 2 * 64, 3 * 64, 64, 64, x * 64, y * 64, 64, 64);
        tileMap[x][y] = true;
        DebugInfo.setLastAction("Painted BRIDGE SHADOW at (" + x + ", " + y + ")");
    }

    public void deleteBridgeShadow(int currentTileX, int currentTileY) {
        gc.clearRect(currentTileX * 64, currentTileY * 64, 64, 64);
        tileMap[currentTileX][currentTileY] = false;
        DebugInfo.setLastAction("Deleted BRIDGE SHADOW at (" + currentTileX + ", " + currentTileY + ")");
    }

    public boolean[][] getTileMap() {
        return tileMap;
    }
}

