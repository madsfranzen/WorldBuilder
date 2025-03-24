package com.worldbuilder.Canvas;

import com.worldbuilder.SpriteLoader;
import com.worldbuilder.debug.DebugInfo;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PlateauCanvas extends Canvas {
    private final GraphicsContext gc = getGraphicsContext2D();
    private final int TILE_SIZE = 64;

    private final PlateauTile[][] tileMap;
    private final Image TILESET = SpriteLoader.getElevationTileset();

    public PlateauCanvas(int width, int height) {
        super(width, height);
        this.tileMap = new PlateauTile[width][height];
    }

    private static record PlateauTile(TileVariant variant) {
    }

    private static record TileVariant(String name, int x, int y) {
    }

    private static final TileVariant[] VARIANTS = {
            new TileVariant("TOP_LEFT", 0, 0),
            new TileVariant("TOP_CENTER", 1, 0),
            new TileVariant("TOP_RIGHT", 2, 0),

            new TileVariant("CENTER_LEFT", 0, 1),
            new TileVariant("CENTER", 1, 1),
            new TileVariant("CENTER_RIGHT", 2, 1),

            new TileVariant("BOTTOM_LEFT", 0, 2),
            new TileVariant("BOTTOM_CENTER", 1, 2),
            new TileVariant("BOTTOM_RIGHT", 2, 2),

            new TileVariant("LONG_TOP", 3, 0),
            new TileVariant("LONG_CENTER", 3, 1),
            new TileVariant("LONG_BOTTOM", 3, 2),

            new TileVariant("WIDE_LEFT", 0, 4),
            new TileVariant("WIDE_CENTER", 1, 4),
            new TileVariant("WIDE_RIGHT", 2, 4),

            new TileVariant("SOLO", 3, 4),

    };

    public void drawPlateau(int x, int y, boolean updateNeighbors) {
        TileVariant variant = determineVariant(x, y);
        tileMap[x][y] = new PlateauTile(variant);

        gc.drawImage(TILESET,
                variant.x() * TILE_SIZE, variant.y() * TILE_SIZE,
                TILE_SIZE, TILE_SIZE,
                x * TILE_SIZE, y * TILE_SIZE,
                TILE_SIZE, TILE_SIZE);

        if (updateNeighbors) {
            updateNeighbors(x, y);
        }

        DebugInfo.setLastAction("Painted plateau at (" + x + ", " + y + ")");
    }

    private TileVariant determineVariant(int x, int y) {
        boolean hasTop = y > 0 && tileMap[x][y - 1] != null;
        boolean hasBottom = y < tileMap[0].length - 1 && tileMap[x][y + 1] != null;
        boolean hasLeft = x > 0 && tileMap[x - 1][y] != null;
        boolean hasRight = x < tileMap.length - 1 && tileMap[x + 1][y] != null;

        if (!hasTop && hasBottom && !hasLeft && hasRight)
            return VARIANTS[0]; // TOP LEFT
        else if (!hasTop && hasBottom && hasLeft && hasRight)
            return VARIANTS[1]; // TOP CENTER
        else if (!hasTop && hasBottom && hasLeft && !hasRight)
            return VARIANTS[2]; // TOP RIGHT

        else if (hasTop && hasBottom && !hasLeft && hasRight)
            return VARIANTS[3]; // CENTER LEFT
        else if (hasTop && hasBottom && hasLeft && hasRight)
            return VARIANTS[4]; // CENTER
        else if (hasTop && hasBottom && hasLeft && !hasRight)
            return VARIANTS[5]; // CENTER RIGHT

        else if (hasTop && !hasBottom && !hasLeft && hasRight)
            return VARIANTS[6]; // BOTTOM LEFT
        else if (hasTop && !hasBottom && hasLeft && hasRight)
            return VARIANTS[7]; // BOTTOM CENTER
        else if (hasTop && !hasBottom && hasLeft && !hasRight)
            return VARIANTS[8]; // BOTTOM RIGHT

        else if (!hasTop && hasBottom && !hasLeft && !hasRight)
            return VARIANTS[9]; // LONG_TOP
        else if (hasTop && hasBottom && !hasLeft && !hasRight)
            return VARIANTS[10]; // LONG_CENTER
        else if (hasTop && !hasBottom && !hasLeft && !hasRight)
            return VARIANTS[11]; // LONG_BOTTOM

        else if (!hasTop && !hasBottom && !hasLeft && hasRight)
            return VARIANTS[12]; // WIDE_LEFT
        else if (!hasTop && !hasBottom && hasLeft && hasRight)
            return VARIANTS[13]; // WIDE_CENTER
        else if (!hasTop && !hasBottom && hasLeft && !hasRight)
            return VARIANTS[14]; // WIDE_RIGHT
        else if (!hasTop && !hasBottom && !hasLeft && !hasRight)
            return VARIANTS[15]; // SOLO
        else
            return null;
    }

    public void deletePlateau(int x, int y) {
        if (tileMap[x][y] != null) {
            tileMap[x][y] = null;
            gc.clearRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            updateNeighbors(x, y);
            DebugInfo.setLastAction("Deleted plateau at (" + x + ", " + y + ")");
        }
    }

    public void updateNeighbors(int x, int y) {
        if (x > 0 && tileMap[x - 1][y] != null)
            drawPlateau(x - 1, y, false);
        if (x < tileMap.length - 1 && tileMap[x + 1][y] != null)
            drawPlateau(x + 1, y, false);
        if (y > 0 && tileMap[x][y - 1] != null)
            drawPlateau(x, y - 1, false);
        if (y < tileMap[0].length - 1 && tileMap[x][y + 1] != null)
            drawPlateau(x, y + 1, false);
    }

    public PlateauTile[][] getTileMap() {
        return tileMap;
    }
}
