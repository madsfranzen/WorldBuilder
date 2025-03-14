package com.worldbuilder.Canvas;

/**
 * Canvas for rendering and managing sand terrain tiles.
 * Implements autotiling logic for sand textures.
 */
public class SandCanvas extends GroundCanvas {
    
    // Define variants using parent class TileVariant type
    private static final GroundCanvas.TileVariant[] VARIANTS = {
        new GroundCanvas.TileVariant("TOP_LEFT", 5, 0),
        new GroundCanvas.TileVariant("TOP", 6, 0),
        new GroundCanvas.TileVariant("TOP_RIGHT", 7, 0),
        new GroundCanvas.TileVariant("LEFT", 5, 1),
        new GroundCanvas.TileVariant("CENTER", 6, 1),
        new GroundCanvas.TileVariant("RIGHT", 7, 1),
        new GroundCanvas.TileVariant("BOTTOM_LEFT", 5, 2),
        new GroundCanvas.TileVariant("BOTTOM", 6, 2),
        new GroundCanvas.TileVariant("BOTTOM_RIGHT", 7, 2),
        new GroundCanvas.TileVariant("HOR_LEFT", 5, 3),
        new GroundCanvas.TileVariant("HOR_CENTER", 6, 3),
        new GroundCanvas.TileVariant("HOR_RIGHT", 7, 3),
        new GroundCanvas.TileVariant("VER_TOP", 8, 0),
        new GroundCanvas.TileVariant("VER_CENTER", 8, 1),
        new GroundCanvas.TileVariant("VER_BOTTOM", 8, 2),
        new GroundCanvas.TileVariant("SOLO", 8, 3)
    };

    public SandCanvas(int width, int height) {
        super(width, height, "SAND");
    }

    @Override
    protected GroundCanvas.TileVariant[] getTerrainVariants() {
        return VARIANTS;
    }

    // Convenience methods with specific terrain type
    public void drawSand(int x, int y, boolean updateNeighbors) {
        drawTerrain(x, y, updateNeighbors);
    }

    public void deleteSand(int x, int y) {
        deleteTerrain(x, y);
    }
}
