package com.worldbuilder.Canvas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import com.worldbuilder.SpriteLoader;

public class BridgeCanvas extends Canvas {
    private final GraphicsContext gc = getGraphicsContext2D();
    private final Image TILESET = SpriteLoader.getBridgeTileset();
    private final int TILE_SIZE = 64;
    private final BridgeTile[][] tileMap;

    public BridgeCanvas(int width, int height) {
        super(width, height);
        this.tileMap = new BridgeTile[width][height];
    }
    
    private static record BridgeTile(TileVariant variant) {}

    private static record TileVariant(String name, int x, int y) {}

    private static final TileVariant[] VARIANTS = {
        new TileVariant("TOP_LEFT", 0, 0),
        new TileVariant("TOP_RIGHT", 1, 0),
        new TileVariant("BOTTOM_LEFT", 0, 1),
        new TileVariant("BOTTOM_RIGHT", 1, 1),
        new TileVariant("TOP", 0, 0),
        new TileVariant("BOTTOM", 0, 0),
        new TileVariant("LEFT", 0, 0),
        new TileVariant("RIGHT", 0, 0)
    };

    public void drawBridge(int x, int y) {
        TileVariant variant = determineVariant(x, y);
        tileMap[x][y] = new BridgeTile(variant);

        gc.drawImage(TILESET, 
            variant.x() * TILE_SIZE, variant.y() * TILE_SIZE,
            TILE_SIZE, TILE_SIZE,
            x * TILE_SIZE, y * TILE_SIZE,
            TILE_SIZE, TILE_SIZE
        );
    }

    private TileVariant determineVariant(int x, int y) {
        boolean hasTop = y > 0 && tileMap[x][y-1] != null;
        boolean hasBottom = y < tileMap[0].length-1 && tileMap[x][y+1] != null;
        boolean hasLeft = x > 0 && tileMap[x-1][y] != null;
        boolean hasRight = x < tileMap.length-1 && tileMap[x+1][y] != null;

        if (hasTop && hasBottom) return VARIANTS[0]; // TOP_LEFT
        if (hasTop && hasRight) return VARIANTS[1]; // TOP_RIGHT
        if (hasBottom && hasLeft) return VARIANTS[2]; // BOTTOM_LEFT
        if (hasBottom && hasRight) return VARIANTS[3]; // BOTTOM_RIGHT

        if (hasTop) return VARIANTS[4]; // TOP
        if (hasBottom) return VARIANTS[5]; // BOTTOM
        if (hasLeft) return VARIANTS[6]; // LEFT
        if (hasRight) return VARIANTS[7]; // RIGHT

        return VARIANTS[8]; // SOLO
    }

    public void deleteBridge(int x, int y) {
        // TODO: STEAL FROM GroundCanvas
    }

    public void updateNeighbors(int x, int y) {
        // TODO: STEAL FROM GroundCanvas
    }
}