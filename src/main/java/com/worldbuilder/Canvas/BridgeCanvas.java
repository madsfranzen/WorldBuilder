package com.worldbuilder.Canvas;

import com.worldbuilder.SpriteLoader;
import com.worldbuilder.debug.DebugInfo;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BridgeCanvas extends Canvas {
    private final GraphicsContext gc = getGraphicsContext2D();
    private final Image TILESET = SpriteLoader.getBridgeTileset();
    private final int TILE_SIZE = 64;
    private final BridgeTile[][] tileMap;

    public BridgeCanvas(int width, int height) {
        super(width, height);
        this.tileMap = new BridgeTile[width][height];
    }

    private static record BridgeTile(TileVariant variant) {
    }

    private static record TileVariant(String name, int x, int y) {
    }

    private static final TileVariant[] VARIANTS = {
            new TileVariant("HOR_LEFT", 0, 0),
            new TileVariant("HOR_CENTER", 1, 0),
            new TileVariant("HOR_RIGHT", 2, 0),

            new TileVariant("VER_TOP", 0, 1),
            new TileVariant("VER_CENTER", 0, 2),
            new TileVariant("VER_BOTTOM", 0, 3),

            new TileVariant("BROKEN1", 1, 1),
            new TileVariant("BROKEN2", 1, 2),
            new TileVariant("BROKEN3", 2, 1),

    };

    public void drawBridge(int x, int y, boolean updateNeighbors) {

        TileVariant variant = determineVariant(x, y);
        tileMap[x][y] = new BridgeTile(variant);

        gc.clearRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        if (variant.name().equals("HOR_CENTER")) {
            gc.drawImage(TILESET,
                variant.x() * TILE_SIZE, variant.y() * TILE_SIZE,
                TILE_SIZE, TILE_SIZE,
                x * TILE_SIZE-2, y * TILE_SIZE,
                TILE_SIZE + 2, TILE_SIZE);
        }
        else if (variant.name().equals("VER_CENTER")) {
            gc.drawImage(TILESET,
                variant.x() * TILE_SIZE, variant.y() * TILE_SIZE,
                TILE_SIZE, TILE_SIZE,
                x * TILE_SIZE, y * TILE_SIZE - 2,
                TILE_SIZE, TILE_SIZE + 2);
        }
        else {
            gc.drawImage(TILESET,
                variant.x() * TILE_SIZE, variant.y() * TILE_SIZE,
                TILE_SIZE, TILE_SIZE,
                x * TILE_SIZE, y * TILE_SIZE,
                TILE_SIZE, TILE_SIZE);
        }

        if (updateNeighbors) {
            updateNeighbors(x, y);
        }

        DebugInfo.setLastAction("Painted bridge at (" + x + ", " + y + ")");
    }

    private TileVariant determineVariant(int x, int y) {
        boolean hasTop = y > 0 && tileMap[x][y - 1] != null;
        boolean hasBottom = y < tileMap[0].length - 1 && tileMap[x][y + 1] != null;
        boolean hasLeft = x > 0 && tileMap[x - 1][y] != null;
        boolean hasRight = x < tileMap.length - 1 && tileMap[x + 1][y] != null;

        if (!hasTop && !hasBottom && !hasLeft && hasRight)
            return VARIANTS[0]; // HOR_LEFT
        if (!hasTop && !hasBottom && hasLeft && hasRight)
            return VARIANTS[1]; // HOR_CENTER
        if (!hasTop && !hasBottom && hasLeft && !hasRight)
            return VARIANTS[2]; // HOR_RIGHT

        if (!hasTop && hasBottom && !hasLeft && !hasRight)
            return VARIANTS[3]; // VER_TOP
        if (hasTop && hasBottom && !hasLeft && !hasRight)
            return VARIANTS[4]; // VER_CENTER
        if (hasTop && !hasBottom && !hasLeft && !hasRight)
            return VARIANTS[5]; // VER_BOTTOM
        return VARIANTS[(int) (Math.random() * 3) + 6]; // Returns BROKEN1, BROKEN2
        // or BROKEN3
    }

    public void deleteBridge(int x, int y) {
        if (tileMap[x][y] != null) {
            tileMap[x][y] = null;
            gc.clearRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            updateNeighbors(x, y);
            DebugInfo.setLastAction("Deleted bridge at (" + x + ", " + y + ")");
        }
    }
    
    public void deleteBridgeNotNull(int x, int y) {
            gc.clearRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    public void updateNeighbors(int x, int y) {
        if (x > 0 && tileMap[x - 1][y] != null) {
            deleteBridgeNotNull(x - 1, y);
            drawBridge(x - 1, y, false);
        }
        if (x < tileMap.length - 1 && tileMap[x + 1][y] != null) {
            deleteBridgeNotNull(x + 1, y);
            drawBridge(x + 1, y, false);
        }
        if (y > 0 && tileMap[x][y - 1] != null) {
            deleteBridgeNotNull(x, y - 1);
            drawBridge(x, y - 1, false);
        }
        if (y < tileMap[0].length - 1 && tileMap[x][y + 1] != null) {
            deleteBridgeNotNull(x, y + 1);
            drawBridge(x, y + 1, false);
        }
    }

    public BridgeTile[][] getTileMap() {
        return tileMap;
    }
}
