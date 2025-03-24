package com.worldbuilder.Canvas;

import com.worldbuilder.SpriteLoader;
import com.worldbuilder.debug.DebugInfo;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class StairsCanvas extends Canvas {
    private final GraphicsContext gc;
    private final int TILE_SIZE = 64;

    private final StairsTile[][] tileMap;
    private final Image TILESET = SpriteLoader.getElevationTileset();

    public StairsCanvas(int width, int height) {
        super(width, height);
        this.gc = getGraphicsContext2D();
        this.tileMap = new StairsTile[width][height];
    }

    private static record TileVariant(String name, int x, int y) {
    }

    private static record StairsTile(TileVariant variant) {
    }

    private static final TileVariant[] VARIANTS = {
            new TileVariant("LEFT", 0, 7),
            new TileVariant("CENTER", 1, 7),
            new TileVariant("RIGHT", 2, 7),
            new TileVariant("SOLO", 3, 7)
    };

    public void drawStairs(int x, int y, boolean updateNeighbors) {
        TileVariant variant = determineVariant(x, y);
        tileMap[x][y] = new StairsTile(variant);

        gc.drawImage(TILESET,
                variant.x() * TILE_SIZE, variant.y() * TILE_SIZE,
                TILE_SIZE, TILE_SIZE,
                x * TILE_SIZE, y * TILE_SIZE,
                TILE_SIZE, TILE_SIZE);

        if (updateNeighbors) {
            updateNeighbors(x, y);
        }

        DebugInfo.setLastAction("Painted stairs at (" + x + ", " + y + ")");
    }

    private TileVariant determineVariant(int x, int y) {
        boolean hasLeft = x > 0 && tileMap[x - 1][y] != null;
        boolean hasRight = x < tileMap.length - 1 && tileMap[x + 1][y] != null;

        if (hasLeft && hasRight)
            return VARIANTS[1]; // CENTER
        else if (hasLeft)
            return VARIANTS[2]; // RIGHT
        else if (hasRight)
            return VARIANTS[0]; // LEFT

        return VARIANTS[3]; // SOLO
    }

    public void deleteStairs(int x, int y) {
        if (tileMap[x][y] != null) {
            tileMap[x][y] = null;
            gc.clearRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            updateNeighbors(x, y);
            DebugInfo.setLastAction("Deleted stairs at (" + x + ", " + y + ")");
        }
    }

    public void updateNeighbors(int x, int y) {
        if (x > 0 && tileMap[x - 1][y] != null)
            drawStairs(x - 1, y, false);
        if (x < tileMap.length - 1 && tileMap[x + 1][y] != null)
            drawStairs(x + 1, y, false);
        if (y > 0 && tileMap[x][y - 1] != null)
            drawStairs(x, y - 1, false);
        if (y < tileMap[0].length - 1 && tileMap[x][y + 1] != null)
            drawStairs(x, y + 1, false);
    }

    public StairsTile[][] getTileMap() {
        return tileMap;
    }
}
