package com.worldbuilder.Canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import com.worldbuilder.debug.DebugInfo;
public class SandCanvas extends Canvas {
    private final Image sandTile = new Image(getClass().getResourceAsStream("/assets/Terrain/Ground/Tilemap_Flat.png"));
    private static final int TILE_SIZE = 64;
    private final GraphicsContext gc = getGraphicsContext2D();

    public SandCanvas(int width, int height) {
        super(width, height);
    }

    // Simple immutable record to hold tile data
    public record TileVariant(String name, int x, int y) {
        // Helper method to draw this variant at a specific position
        public void drawAt(GraphicsContext gc, Image tilesheet, double x, double y) {
            gc.drawImage(tilesheet,
                this.x * TILE_SIZE, this.y * TILE_SIZE,  // Source position in tileset
                TILE_SIZE, TILE_SIZE,                    // Source size
                x * TILE_SIZE, y * TILE_SIZE,           // Target position
                TILE_SIZE, TILE_SIZE                    // Target size
            );
        }
    }

    // Static definitions of all variants
    public static class SandVariants {
        public static final TileVariant 
            TOP_LEFT = new TileVariant("TOP_LEFT", 5, 0),
            TOP = new TileVariant("TOP", 6, 0),
            TOP_RIGHT = new TileVariant("TOP_RIGHT", 7, 0),
            LEFT = new TileVariant("LEFT", 5, 1),
            CENTER = new TileVariant("CENTER", 6, 1),
            RIGHT = new TileVariant("RIGHT", 7, 1),
            BOTTOM_LEFT = new TileVariant("BOTTOM_LEFT", 5, 2),
            BOTTOM = new TileVariant("BOTTOM", 6, 2),
            BOTTOM_RIGHT = new TileVariant("BOTTOM_RIGHT", 7, 2),
            HOR_LEFT = new TileVariant("HOR_LEFT", 5, 3),
            HOR_CENTER = new TileVariant("HOR_CENTER", 6, 3),
            HOR_RIGHT = new TileVariant("HOR_RIGHT", 7, 3),
            VER_TOP = new TileVariant("VER_TOP", 8, 0),
            VER_CENTER = new TileVariant("VER_CENTER", 8, 1),
            VER_BOTTOM = new TileVariant("VER_BOTTOM", 8, 2),
            SOLO = new TileVariant("SOLO", 8, 3);
    }

    public void paintSand(int x, int y) {
        TileVariant variant = determineVariant(x, y);

        gc.drawImage(sandTile, 
            variant.x() * 64, variant.y() * 64,  // Source coordinates in tileset
            64, 64,                              // Source dimensions
            x * 64, y * 64,                      // Destination coordinates
            64, 64                               // Destination dimensions
        );
        DebugInfo.setLastAction("Painted SAND at (" + x + ", " + y + ")");
    }

    public void deleteSand(int x, int y) {
        gc.clearRect(x * 64, y * 64, 64, 64);
        DebugInfo.setLastAction("Deleted SAND at (" + x + ", " + y + ")");
    }

    private TileVariant determineVariant(int x, int y) {
        // Implement logic to determine the variant based on the coordinates
        // For example, you might have a grid of tiles and need to check the surrounding tiles
        // to determine which variant to use
        // This is a placeholder implementation
        return SandVariants.SOLO;
    }
}
