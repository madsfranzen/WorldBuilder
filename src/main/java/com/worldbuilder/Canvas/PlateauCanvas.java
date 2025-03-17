package com.worldbuilder.Canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import com.worldbuilder.SpriteLoader;

public class PlateauCanvas extends Canvas {
    private final GraphicsContext gc = getGraphicsContext2D();
    private final int TILE_SIZE = 64;
    
    private final PlateauTile[][] tileMap;
    private final Image TILESET = SpriteLoader.getElevationTileset();

    public PlateauCanvas(int width, int height) {
        super(width, height);
        this.tileMap = new PlateauTile[width][height];
    }

    private static record PlateauTile(TileVariant variant) {}

    private static record TileVariant(String name, int x, int y) {}
    
    private static final TileVariant[] VARIANTS = {
        new TileVariant("TOP_LEFT", 0, 0),
        new TileVariant("TOP_RIGHT", 1, 0),
        new TileVariant("BOTTOM_LEFT", 0, 1),
        new TileVariant("BOTTOM_RIGHT", 1, 1),
        new TileVariant("CENTER", 0, 0),
        new TileVariant("TOP", 0, 0),
        new TileVariant("BOTTOM", 0, 0)
    };

    public void drawPlateau(int x, int y) {
        TileVariant variant = determineVariant(x, y);
        tileMap[x][y] = new PlateauTile(variant);

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

        if (hasTop && hasBottom) return VARIANTS[1]; // CENTER
        if (hasTop) return VARIANTS[0]; // LEFT
        if (hasBottom) return VARIANTS[2]; // BOTTOM
        if (hasLeft) return VARIANTS[3]; // TOP
        if (hasRight) return VARIANTS[4]; // BOTTOM

        return VARIANTS[5]; // SOLO
    }
    
    public void deletePlateau(int x, int y) {
        // TODO: STEAL FROM GroundCanvas
    }

    public void updateNeighbors(int x, int y) {
        // TODO: STEAL FROM GroundCanvas
    }   
    
    
    
}
