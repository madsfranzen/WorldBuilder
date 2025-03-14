package com.worldbuilder.Canvas;

public class Elevation {
    SandCanvas sandCanvas;
    GrassCanvas grassCanvas;
    ShadowCanvas shadowCanvas;
    StairsCanvas stairsCanvas;
    WallCanvas wallCanvas;
    PlateauCanvas plateauCanvas;
    BridgeCanvas bridgeCanvas;

    public Elevation(int width, int height) {
        this.sandCanvas = new SandCanvas(width, height);
        this.grassCanvas = new GrassCanvas(width, height);
        this.shadowCanvas = new ShadowCanvas(width, height);
        this.stairsCanvas = new StairsCanvas(width, height);
        this.wallCanvas = new WallCanvas(width, height);
        this.plateauCanvas = new PlateauCanvas(width, height);
        this.bridgeCanvas = new BridgeCanvas(width, height);
    }
}
