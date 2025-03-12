package com.worldbuilder.Canvas;

import com.worldbuilder.App;
import com.worldbuilder.debug.DebugInfo;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * WorldCanvas - A scrollable grid-based canvas for world building
 * Supports tile painting and grid visualization
 */
public final class WorldCanvas extends ScrollPane {

    //================== CONSTANTS & FIELDS ==================//
    
    private final int TILE_SIZE = 64;
    private final int WORLD_WIDTH;
    private final int WORLD_HEIGHT;

    private final Canvas gridCanvas;
    private final Pane canvasContainer;

    private final GrassCanvas grassCanvas;
    private final WaterCanvas waterCanvas;
    private final SandCanvas sandCanvas;
    private final FoamCanvas foamCanvas;
    private final ShadowCanvas shadowCanvas;
    // Current tile position (mouse)
    private int currentTileX;
    private int currentTileY;

    //================== CONSTRUCTOR ==================//

    public WorldCanvas(int WORLD_WIDTH, int WORLD_HEIGHT) {
        super();
        this.WORLD_WIDTH = WORLD_WIDTH;
        this.WORLD_HEIGHT = WORLD_HEIGHT;

        // Initialize canvas dimensions
        int width = WORLD_WIDTH * TILE_SIZE;
        int height = WORLD_HEIGHT * TILE_SIZE;

        // Setup canvas container
        canvasContainer = new Pane();
        canvasContainer.setPrefSize(width, height);
        canvasContainer.setMaxSize(width, height);
        canvasContainer.setMinSize(width, height);

        // Initialize main grid canvas
        gridCanvas = new Canvas(width, height);
        setupMouseHandlers();
        canvasContainer.getChildren().add(gridCanvas);

        // Add drawing canvases to the container

        waterCanvas = new WaterCanvas(width, height);
        canvasContainer.getChildren().add(waterCanvas);
        waterCanvas.setMouseTransparent(true);

        foamCanvas = new FoamCanvas(width, height);
        canvasContainer.getChildren().add(foamCanvas);
        foamCanvas.setMouseTransparent(true);

        sandCanvas = new SandCanvas(width, height);
        canvasContainer.getChildren().add(sandCanvas);
        sandCanvas.setMouseTransparent(true);

        grassCanvas = new GrassCanvas(width, height);
        canvasContainer.getChildren().add(grassCanvas);
        grassCanvas.setMouseTransparent(true);

        shadowCanvas = new ShadowCanvas(width, height);
        canvasContainer.getChildren().add(shadowCanvas);
        shadowCanvas.setMouseTransparent(true);

        // Configure ScrollPane
        setupScrollPane(width, height);
        
        // Draw initial grid
        drawGrid();
    }

    //================== SCROLL PANE SETUP ==================//

    private void setupScrollPane(int width, int height) {
        setContent(canvasContainer);
        setPannable(false);
        setHbarPolicy(ScrollBarPolicy.ALWAYS);
        setVbarPolicy(ScrollBarPolicy.ALWAYS);
        setFitToWidth(true);
        setFitToHeight(true);

        // Enable mouse wheel panning
        setOnScroll(event -> {
            event.consume();
            double panSpeed = 10.0;
            setHvalue(getHvalue() - event.getDeltaX() / panSpeed / width);
            setVvalue(getVvalue() - event.getDeltaY() / panSpeed / height);
        });
    }

    //================== MOUSE HANDLERS ==================//

    private void setupMouseHandlers() {
        gridCanvas.setOnMouseMoved(this::updateTilePosition);
        gridCanvas.setOnMousePressed(event -> {
            updateTilePosition(event);
            paintTile();
        });
        gridCanvas.setOnMouseDragged(event -> {
            updateTilePosition(event);
            paintTile();
        });
    }

    private void updateTilePosition(MouseEvent event) {
        // Get tile coordinates directly from mouse position
        currentTileX = Math.min(Math.max(0, (int)(event.getX() / TILE_SIZE)), WORLD_WIDTH - 1);
        currentTileY = Math.min(Math.max(0, (int)(event.getY() / TILE_SIZE)), WORLD_HEIGHT - 1);
        
        DebugInfo.updateCoordinates("Tile", currentTileX, currentTileY);
    }

    //================== DRAWING METHODS ==================//

    private void paintTile() {
        switch (App.getSidePanel().getSelectedLayer()) {
            case WATER -> waterCanvas.drawWater(currentTileX, currentTileY);
            case FOAM -> foamCanvas.addFoamAt(currentTileX, currentTileY);
            case SAND -> sandCanvas.drawSand(currentTileX, currentTileY);
            case GRASS -> grassCanvas.drawGrass(currentTileX, currentTileY);
            case SHADOW -> shadowCanvas.drawShadow(currentTileX, currentTileY);
            case null -> System.out.println("No tile selected");
            default -> System.out.println("No tile selected");
        }
    }

    public void drawGrid() {
        GraphicsContext gc = gridCanvas.getGraphicsContext2D();
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(1);

        // Draw vertical lines
        for (int x = 0; x <= WORLD_WIDTH; x++) {
            gc.strokeLine(x * TILE_SIZE, 0, x * TILE_SIZE, gridCanvas.getHeight());
        }

        // Draw horizontal lines
        for (int y = 0; y <= WORLD_HEIGHT; y++) {
            gc.strokeLine(0, y * TILE_SIZE, gridCanvas.getWidth(), y * TILE_SIZE);
        }
    }

    //================== GETTERS ==================//

    public int getCurrentTileX() { return currentTileX; }
    public int getCurrentTileY() { return currentTileY; }
}
