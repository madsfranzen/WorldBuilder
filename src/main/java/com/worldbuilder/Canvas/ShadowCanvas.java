package com.worldbuilder.Canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import com.worldbuilder.debug.DebugInfo;

public class ShadowCanvas extends Canvas {

    private final GraphicsContext gc = getGraphicsContext2D();
    private final ShadowTile[][] tileMap;

    public class ShadowTile {
        private final Image shadowTileImage = new Image(
                getClass().getResourceAsStream("/assets/Terrain/Ground/Shadows.png"));
    }

    public ShadowCanvas(int width, int height) {
        super(width, height);
        tileMap = new ShadowTile[width][height];
    }

    public void drawShadow(int x, int y) {
        // Offset to center the 3x3 shadow image on the tile
        int offsetX = 64;
        int offsetY = 64;

        // Paint the shadow if it is not painted yet
        if (tileMap[x][y] == null) {
            ShadowTile shadowTile = new ShadowTile();
            tileMap[x][y] = shadowTile;
            gc.drawImage(shadowTile.shadowTileImage, x * 64 - offsetX, y * 64 - offsetY, 64 * 3, 64 * 3);
            DebugInfo.setLastAction("Painted SHADOW at (" + x + ", " + y + ")");
        } else {
            DebugInfo.setError(("Shadow already painted at (" + x + ", " + y + ")"));
        }
    }

    public void deleteShadow(int x, int y) {
        gc.clearRect(x * 64 - 64, y * 64 - 64, 64 * 3, 64 * 3);
        DebugInfo.setLastAction("Deleted SHADOW at (" + x + ", " + y + ")");
    }
}