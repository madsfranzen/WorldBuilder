package com.worldbuilder.Canvas;

public class Elevation {
    private final SandCanvas sandCanvas;
    private final GrassCanvas grassCanvas;
    private final ShadowCanvas shadowCanvas;
    private final StairsCanvas stairsCanvas;
    private final WallCanvas wallCanvas;
    private final PlateauCanvas plateauCanvas;
    private final BridgeCanvas bridgeCanvas;

    private final int zIndex;

    public Elevation(int width, int height, int zIndex) {
        this.zIndex = zIndex;
        this.sandCanvas = new SandCanvas(width, height);
        this.grassCanvas = new GrassCanvas(width, height);
        this.shadowCanvas = new ShadowCanvas(width, height);
        this.stairsCanvas = new StairsCanvas(width, height);
        this.wallCanvas = new WallCanvas(width, height);
        this.plateauCanvas = new PlateauCanvas(width, height);
        this.bridgeCanvas = new BridgeCanvas(width, height);
    }

    public SandCanvas getSandCanvas() {
        return sandCanvas;
    }

    public GrassCanvas getGrassCanvas() {
        return grassCanvas;
    }

    public ShadowCanvas getShadowCanvas() {
        return shadowCanvas;
    }

    public StairsCanvas getStairsCanvas() {
        return stairsCanvas;
    }

    public WallCanvas getWallCanvas() {
        return wallCanvas;
    }

    public PlateauCanvas getPlateauCanvas() {
        return plateauCanvas;
    }

    public BridgeCanvas getBridgeCanvas() {
        return bridgeCanvas;
    }

    public int getZIndex() {
        return zIndex;
    }

 
}
