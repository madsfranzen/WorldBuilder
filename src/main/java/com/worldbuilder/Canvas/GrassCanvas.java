package com.worldbuilder.Canvas;

/**
 * Canvas for rendering and managing grass terrain tiles.
 * Implements autotiling logic for grass textures.
 */
public class GrassCanvas extends GroundCanvas {
    
    // Define variants using parent class TileVariant type
    private static final GroundCanvas.TileVariant[] VARIANTS = {
        new GroundCanvas.TileVariant("TOP_LEFT", 0, 0),
        new GroundCanvas.TileVariant("TOP", 1, 0),
        new GroundCanvas.TileVariant("TOP_RIGHT", 2, 0),
        new GroundCanvas.TileVariant("LEFT", 0, 1),
        new GroundCanvas.TileVariant("CENTER", 1, 1),
        new GroundCanvas.TileVariant("RIGHT", 2, 1),
        new GroundCanvas.TileVariant("BOTTOM_LEFT", 0, 2),
        new GroundCanvas.TileVariant("BOTTOM", 1, 2),
        new GroundCanvas.TileVariant("BOTTOM_RIGHT", 2, 2),
        new GroundCanvas.TileVariant("HOR_LEFT", 0, 3),
        new GroundCanvas.TileVariant("HOR_CENTER", 1, 3),
        new GroundCanvas.TileVariant("HOR_RIGHT", 2, 3),
        new GroundCanvas.TileVariant("VER_TOP", 3, 0),
        new GroundCanvas.TileVariant("VER_CENTER", 3, 1),
        new GroundCanvas.TileVariant("VER_BOTTOM", 3, 2),
        new GroundCanvas.TileVariant("SOLO", 3, 3)
    };

    public GrassCanvas(int width, int height) {
        super(width, height, "GRASS");
    }

    @Override
    protected GroundCanvas.TileVariant[] getTerrainVariants() {
        return VARIANTS;
    }

    // Convenience methods with specific terrain type
    public void drawGrass(int x, int y, boolean updateNeighbors) {
        drawTerrain(x, y, updateNeighbors);
    }

    public void deleteGrass(int x, int y) {
        deleteTerrain(x, y);
    }
}
