package com.worldbuilder.Canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class WallCanvas extends Canvas {
    private final GraphicsContext gc = getGraphicsContext2D();
    private final int TILE_SIZE = 64;

    private final WallTile[][] tileMap;

    private final Image TILESET = new Image(
            getClass().getResourceAsStream("/assets/Terrain/Ground/Tilemap_Elevation.png"));

    public WallCanvas(int width, int height) {
        super(width, height);
        this.tileMap = new WallTile[width][height];
    }

    private static record WallTile(TileVariant variant) {
    }

    private static record TileVariant(String name, int x, int y) {
    }

    private static final TileVariant[] VARIANTS = {
            new TileVariant("LEFT", 0, 1),
            new TileVariant("CENTER", 1, 1),
            new TileVariant("RIGHT", 2, 1),
            new TileVariant("SOLO", 0, 0)
    };

    public void drawWall(int x, int y) {
        TileVariant variant = determineVariant(x, y);
        tileMap[x][y] = new WallTile(variant);

        gc.drawImage(TILESET,
                variant.x() * TILE_SIZE, variant.y() * TILE_SIZE,
                TILE_SIZE, TILE_SIZE,
                x * TILE_SIZE, y * TILE_SIZE,
                TILE_SIZE, TILE_SIZE);
    }

    private TileVariant determineVariant(int x, int y) {
        boolean hasLeft = x > 0 && tileMap[x - 1][y] != null;
        boolean hasRight = x < tileMap.length - 1 && tileMap[x + 1][y] != null;

        if (hasLeft && hasRight)
            return VARIANTS[1]; // CENTER
        if (hasLeft)
            return VARIANTS[0]; // LEFT
        if (hasRight)
            return VARIANTS[2]; // RIGHT

        return VARIANTS[3]; // SOLO
    }

    public void deleteWall(int x, int y) {
        // TODO: STEAL FROM GroundCanvas
    }

    public void updateNeighbors(int x, int y) {
        // TODO: STEAL FROM GroundCanvas
    }

}
