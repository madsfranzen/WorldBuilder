package com.worldbuilder.Canvas;

import com.worldbuilder.debug.DebugInfo;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Abstract base class for ground tile canvases.
 * Provides common functionality for tile-based grounds with autotiling support.
 */
public abstract class GroundCanvas extends Canvas {
    protected static final int TILE_SIZE = 64;
    protected static final Image TILESET = new Image(GroundCanvas.class.getResourceAsStream("/assets/Terrain/Ground/Tilemap_Flat.png"));
    
    protected final GraphicsContext gc;
    protected final TerrainTile[][] tileMap;
    protected final String terrainType;

    /**
     * Record to represent a tile variant with its position in the tileset.
     */
    protected record TileVariant(String name, int x, int y) {}

    /**
     * Record to represent a terrain tile with its variant.
     */
    protected record TerrainTile(TileVariant variant) {}

    /**
     * Creates a new GroundCanvas with specified dimensions and terrain type.
     */
    protected GroundCanvas(int width, int height, String terrainType) {
        super(width, height);
        this.gc = getGraphicsContext2D();
        this.tileMap = new TerrainTile[width][height];
        this.terrainType = terrainType;
    }

    /**
     * Gets the tile variants specific to this terrain type.
     */
    protected abstract TileVariant[] getTerrainVariants();

    /**
     * Determines the appropriate tile variant based on neighboring tiles.
     */
    protected TileVariant determineVariant(int x, int y) {
        TileVariant[] variants = getTerrainVariants();
        
        boolean hasTop = y > 0 && tileMap[x][y-1] != null;
        boolean hasBottom = y < tileMap[0].length-1 && tileMap[x][y+1] != null;
        boolean hasLeft = x > 0 && tileMap[x-1][y] != null;
        boolean hasRight = x < tileMap.length-1 && tileMap[x+1][y] != null;

        if (hasTop && hasBottom && hasLeft && hasRight) return variants[4];  // CENTER
        if (hasLeft && hasRight && !hasTop && !hasBottom) return variants[10]; // HOR_CENTER
        if (hasTop && hasBottom && !hasLeft && !hasRight) return variants[13]; // VER_CENTER
        if (hasRight && hasBottom && !hasLeft && !hasTop) return variants[0];  // TOP_LEFT
        if (hasLeft && hasBottom && !hasRight && !hasTop) return variants[2];  // TOP_RIGHT
        if (hasRight && hasTop && !hasLeft && !hasBottom) return variants[6];  // BOTTOM_LEFT
        if (hasLeft && hasTop && !hasRight && !hasBottom) return variants[8];  // BOTTOM_RIGHT
        if (hasBottom && !hasTop && hasLeft && hasRight) return variants[1];   // TOP
        if (hasTop && !hasBottom && hasLeft && hasRight) return variants[7];   // BOTTOM
        if (hasRight && !hasLeft && hasTop && hasBottom) return variants[3];   // LEFT
        if (hasLeft && !hasRight && hasTop && hasBottom) return variants[5];   // RIGHT
        if (hasRight && !hasLeft && !hasTop && !hasBottom) return variants[9]; // HOR_LEFT
        if (hasLeft && !hasRight && !hasTop && !hasBottom) return variants[11];// HOR_RIGHT
        if (!hasRight && !hasLeft && !hasTop && hasBottom) return variants[12];// VER_TOP
        if (!hasRight && !hasLeft && !hasBottom && hasTop) return variants[14];// VER_BOTTOM
        return variants[15]; // SOLO
    }

    /**
     * Draws a terrain tile at the specified coordinates.
     */
    public void drawTerrain(int x, int y, boolean updateNeighbors) {
        TileVariant variant = determineVariant(x, y);
        tileMap[x][y] = new TerrainTile(variant);

        gc.drawImage(TILESET, 
            variant.x() * TILE_SIZE, variant.y() * TILE_SIZE,
            TILE_SIZE, TILE_SIZE,
            x * TILE_SIZE, y * TILE_SIZE,
            TILE_SIZE, TILE_SIZE
        );

        if (updateNeighbors) {
            updateNeighbors(x, y);
        }

        DebugInfo.setLastAction("Painted " + terrainType + " at (" + x + ", " + y + ")");
    }

    /**
     * Deletes a terrain tile at the specified coordinates.
     */
    public void deleteTerrain(int x, int y) {
        if (tileMap[x][y] != null) {
            tileMap[x][y] = null;
            gc.clearRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            updateNeighbors(x, y);
            DebugInfo.setLastAction("Deleted " + terrainType + " at (" + x + ", " + y + ")");
        }
    }

    /**
     * Updates neighboring tiles after a terrain modification.
     */
    protected void updateNeighbors(int x, int y) {
        if (x > 0 && tileMap[x-1][y] != null) drawTerrain(x-1, y, false);
        if (x < tileMap.length-1 && tileMap[x+1][y] != null) drawTerrain(x+1, y, false);
        if (y > 0 && tileMap[x][y-1] != null) drawTerrain(x, y-1, false);
        if (y < tileMap[0].length-1 && tileMap[x][y+1] != null) drawTerrain(x, y+1, false);
    }
} 