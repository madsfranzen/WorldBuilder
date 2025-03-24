package com.worldbuilder.Canvas;

import java.util.ArrayList;
import java.util.List;

import com.worldbuilder.SpriteLoader;
import com.worldbuilder.debug.DebugInfo;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * A specialized Canvas for rendering and managing shadows.
 * This canvas handles the drawing and deletion of connected shadow tiles, ensuring
 * proper overlap handling and visual consistency.
 */
public class ShadowCanvas extends Canvas {
    private final GraphicsContext gc = getGraphicsContext2D();

    // Constants for tile dimensions and shadow properties
    private static final int TILE_SIZE = 64;
    private static final int SHADOW_SIZE = TILE_SIZE * 3;
    private static final int SHADOW_CONNECTION_RADIUS = 2;
    
    private static final Image SHADOW_IMAGE = SpriteLoader.getShadowsImage();

    
    private final ShadowTileMap[][] tileMap;

    /**
     * Represents a single shadow tile in the grid.
     * Acts as a marker for shadow presence.
     */
    private static class ShadowTileMap {
        // Marker class - no additional properties needed
    }

    /**
     * Creates a new ShadowCanvas with the specified dimensions.
     * @param width The width of the canvas in tiles
     * @param height The height of the canvas in tiles
     */
    public ShadowCanvas(int width, int height) {
        super(width, height);
        this.tileMap = new ShadowTileMap[width][height];
    }

    /**
     * Draws a shadow at the specified grid coordinates.
     * Will not draw if a shadow already exists at the location.
     * 
     * @param x The x-coordinate in the tile grid
     * @param y The y-coordinate in the tile grid
     */
    public void drawShadow(int x, int y) {
        if (tileMap[x][y] == null) {
            tileMap[x][y] = new ShadowTileMap();
            gc.drawImage(SHADOW_IMAGE, 
                        x * TILE_SIZE - TILE_SIZE, 
                        y * TILE_SIZE - TILE_SIZE, 
                        SHADOW_SIZE, 
                        SHADOW_SIZE);
            DebugInfo.setLastAction("Painted SHADOW at (" + x + ", " + y + ")");
        } else {
            DebugInfo.setError("Shadow already painted at (" + x + ", " + y + ")");
        }
    }

    /**
     * Record class for storing neighbor coordinates.
     */
    private record Neighbor(int x, int y) {}

    /**
     * Deletes a shadow at the specified coordinates and handles connected shadows
     * to maintain visual consistency. This includes collecting all connected shadows,
     * clearing their area, and redrawing necessary shadows.
     * 
     * @param x The x-coordinate in the tile grid
     * @param y The y-coordinate in the tile grid
     */
    public void deleteShadow(int x, int y) {
        if (tileMap[x][y] == null) {
            return;
        }

        List<Neighbor> connectedShadows = new ArrayList<>();
        collectConnectedShadows(x, y, connectedShadows);

        // Clear and redraw connected shadows
        connectedShadows.forEach(shadow -> {
            tileMap[shadow.x()][shadow.y()] = null;
            gc.clearRect(
                shadow.x() * TILE_SIZE - TILE_SIZE,
                shadow.y() * TILE_SIZE - TILE_SIZE,
                SHADOW_SIZE,
                SHADOW_SIZE
            );
        });

        // Repaint shadows except the deleted one
        connectedShadows.stream()
            .filter(shadow -> shadow.x() != x || shadow.y() != y)
            .forEach(shadow -> drawShadow(shadow.x(), shadow.y()));

        DebugInfo.setLastAction("Deleted SHADOW at (" + x + ", " + y + ")");
    }

    /**
     * Recursively collects all shadows connected to the specified coordinates.
     * Two shadows are considered connected if they are within SHADOW_CONNECTION_RADIUS
     * tiles of each other.
     * 
     * @param x The x-coordinate to check
     * @param y The y-coordinate to check
     * @param connectedShadows List to store connected shadow coordinates
     */
    private void collectConnectedShadows(int x, int y, List<Neighbor> connectedShadows) {
        if (isOutOfBounds(x, y) || 
            tileMap[x][y] == null || 
            connectedShadows.contains(new Neighbor(x, y))) {
            return;
        }

        connectedShadows.add(new Neighbor(x, y));

        // Check neighbors within connection radius
        for (int dx = -SHADOW_CONNECTION_RADIUS; dx <= SHADOW_CONNECTION_RADIUS; dx++) {
            for (int dy = -SHADOW_CONNECTION_RADIUS; dy <= SHADOW_CONNECTION_RADIUS; dy++) {
                if (dx == 0 && dy == 0) continue;
                collectConnectedShadows(x + dx, y + dy, connectedShadows);
            }
        }
    }

    /**
     * Checks if the given coordinates are within the tile map bounds.
     * 
     * @param x The x-coordinate to check
     * @param y The y-coordinate to check
     * @return true if the coordinates are out of bounds
     */
    private boolean isOutOfBounds(int x, int y) {
        return x < 0 || x >= tileMap.length || y < 0 || y >= tileMap[0].length;
    }

    public ShadowTileMap[][] getTileMap() {
        return tileMap;
    }
}