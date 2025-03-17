package com.worldbuilder.Canvas;

import java.util.ArrayList;
import java.util.List;

import com.worldbuilder.App;
import com.worldbuilder.debug.DebugInfo;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * WorldCanvas - A scrollable grid-based canvas for world building
 * Supports tile painting and grid visualization
 */
public final class WorldCanvas extends ScrollPane {

    // ================== CONSTANTS & FIELDS ==================//

    private final int TILE_SIZE = 64;
    private final int WORLD_WIDTH;
    private final int WORLD_HEIGHT;

    private final Canvas gridCanvas;
    private final Canvas hoverCanvas;
    private final StackPane canvasContainer;

    private final GrassCanvas grassCanvas;
    private final WaterCanvas waterCanvas;
    private final RocksCanvas rocksCanvas;
    private final SandCanvas sandCanvas;
    private final FoamCanvas foamCanvas;
    private final ShadowCanvas shadowCanvas;

    private final List<Canvas> canvasList = new ArrayList<>();

    // Current tile position (mouse)
    private int currentTileX;
    private int currentTileY;
    private int previousTileX;
    private int previousTileY;

    // ================== CONSTRUCTOR ==================//

    public WorldCanvas(int WORLD_WIDTH, int WORLD_HEIGHT) {
        super();
        this.WORLD_WIDTH = WORLD_WIDTH;
        this.WORLD_HEIGHT = WORLD_HEIGHT;

        // Initialize canvas dimensions
        int width = WORLD_WIDTH * TILE_SIZE;
        int height = WORLD_HEIGHT * TILE_SIZE;

        // Setup canvas container
        canvasContainer = new StackPane();

        // Initialize main grid canvas
        gridCanvas = new Canvas(width, height);
        canvasList.add(gridCanvas);
        setupMouseHandlers();

        // Add drawing canvases to the container

        waterCanvas = new WaterCanvas(width, height);
        canvasList.add(waterCanvas);

        foamCanvas = new FoamCanvas(width, height);
        canvasList.add(foamCanvas);

        sandCanvas = new SandCanvas(width, height);
        canvasList.add(sandCanvas);

        rocksCanvas = new RocksCanvas(width, height);
        canvasList.add(rocksCanvas);

        grassCanvas = new GrassCanvas(width, height);
        canvasList.add(grassCanvas);

        shadowCanvas = new ShadowCanvas(width, height);
        canvasList.add(shadowCanvas);

        // Initialize hover canvas
        hoverCanvas = new Canvas(width, height);
        canvasList.add(hoverCanvas);

        // Configure ScrollPane
        setupScrollPane(width, height);

        // Draw initial grid
        drawGrid();

        for (Canvas canvas : canvasList) {
            canvas.setCache(true);
            canvas.setCacheHint(javafx.scene.CacheHint.SPEED);
            canvas.setMouseTransparent(true);
        }
        
        gridCanvas.setMouseTransparent(false);

        // Add all canvases to the container
        canvasContainer.getChildren().addAll(canvasList);

    }

    // ================== SCROLL PANE SETUP ==================//

    private void setupScrollPane(int width, int height) {
        setContent(canvasContainer);
        setPannable(false); // Disable panning to prevent view movement when painting
        setHbarPolicy(ScrollBarPolicy.ALWAYS);
        setVbarPolicy(ScrollBarPolicy.ALWAYS);

        // Don't use fitToWidth/Height as they can cause performance issues
        setFitToWidth(false);
        setFitToHeight(false);

        // Set viewport size - this prevents re-layout calculations
        setPrefViewportWidth(800);
        setPrefViewportHeight(600);

        // Use hardware acceleration for the scroll pane
        setCache(true);
        setCacheHint(javafx.scene.CacheHint.SPEED);

        // Optimize scrolling speed based on size
        double scrollFactor = 0.001 * Math.min(width, height);

        // Handle scroll events with throttling
        setOnScroll(event -> {
            event.consume();
            double deltaX = event.getDeltaX() * scrollFactor;
            double deltaY = event.getDeltaY() * scrollFactor;

            // Apply scroll changes
            setHvalue(getHvalue() - deltaX);
            setVvalue(getVvalue() - deltaY);
        });
    }

    // ================== MOUSE HANDLERS ==================//

    private void setupMouseHandlers() {
        gridCanvas.setOnMouseMoved(this::updateTilePosition);

        gridCanvas.setOnMousePressed(event -> {
            updateTilePosition(event);

            // IF RIGHT CLICK
            if (event.isSecondaryButtonDown()) {
                deleteTile();
            } else {
                paintTile();
            }
        });

        gridCanvas.setOnMouseDragged(event -> {
            updateTilePosition(event);

            DebugInfo.updatePosition("COORDINATES", event.getX(), event.getY());

            if (event.getX() < 0 || event.getY() < 0) {
                event.consume();
                DebugInfo.setError("CANNOT PAINT OUTSIDE OF CANVAS");
            } else {
                if (event.isSecondaryButtonDown()) {
                    deleteTile();
                } else {
                    paintTile();
                }
            }
        });
    }

    private void updateTilePosition(MouseEvent event) {

        // removee hover effect from previous tile
        previousTileX = currentTileX;
        previousTileY = currentTileY;
        hoverCanvas.getGraphicsContext2D().clearRect(previousTileX * TILE_SIZE, previousTileY * TILE_SIZE, TILE_SIZE,
                TILE_SIZE);

        // Update tile coordinates directly from mouse position
        currentTileX = Math.min(Math.max(0, (int) (event.getX() / TILE_SIZE)), WORLD_WIDTH - 1);
        currentTileY = Math.min(Math.max(0, (int) (event.getY() / TILE_SIZE)), WORLD_HEIGHT - 1);

        // add hover effect to current tile
        hoverCanvas.getGraphicsContext2D().setFill(new Color(1, 1, 0, 0.1)); // Yellow with 10% opacity
        hoverCanvas.getGraphicsContext2D().fillRect(currentTileX * TILE_SIZE, currentTileY * TILE_SIZE, TILE_SIZE,
                TILE_SIZE);

        DebugInfo.updateCoordinates("TILE", currentTileX, currentTileY);
    }

    // ================== DRAWING METHODS ==================//

    private void paintTile() {
        switch (App.getSidePanel().getSelectedLayer()) {
            case WATER -> {
                waterCanvas.drawWater(currentTileX, currentTileY);
            }
            case FOAM -> {
                foamCanvas.drawFoam(currentTileX, currentTileY);
            }
            case SAND -> {
                sandCanvas.drawSand(currentTileX, currentTileY, true);
            }
            case GRASS -> {
                grassCanvas.drawGrass(currentTileX, currentTileY, true);
            }
            case SHADOW -> {
                shadowCanvas.drawShadow(currentTileX, currentTileY);
            }
            case ROCKS -> {
                rocksCanvas.drawRocks(currentTileX, currentTileY, App.getSidePanel().getSelectedRockType());
            }
            case null -> DebugInfo.setError("NO LAYER SELECTED");
            default -> DebugInfo.setError("NO LAYER SELECTED");
        }
    }

    private void deleteTile() {
        switch (App.getSidePanel().getSelectedLayer()) {
            case WATER -> {
                waterCanvas.deleteWater(currentTileX, currentTileY);
            }
            case FOAM -> {
                foamCanvas.deleteFoam(currentTileX, currentTileY);
            }
            case SAND -> {
                sandCanvas.deleteSand(currentTileX, currentTileY);
            }
            case GRASS -> {
                grassCanvas.deleteGrass(currentTileX, currentTileY);
            }
            case SHADOW -> {
                shadowCanvas.deleteShadow(currentTileX, currentTileY);
            }
            case ROCKS -> {
                rocksCanvas.deleteRocks(currentTileX, currentTileY);
            }
            case null -> DebugInfo.setError("NO LAYER SELECTED");
            default -> DebugInfo.setError("NO LAYER SELECTED");
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

    // ================== GETTERS ==================//

    public int getCurrentTileX() {
        return currentTileX;
    }

    public int getCurrentTileY() {
        return currentTileY;
    }
}
