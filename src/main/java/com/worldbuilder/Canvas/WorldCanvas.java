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

    // ================== CONSTANTS & FIELDS ==================//

    private final int TILE_SIZE = 64;
    private final int WORLD_WIDTH;
    private final int WORLD_HEIGHT;

    private final Canvas gridCanvas;
    private final Canvas hoverCanvas;
    private final Pane canvasContainer;

    private final GrassCanvas grassCanvas;
    private final WaterCanvas waterCanvas;
    private final RocksCanvas rocksCanvas;
    private final SandCanvas sandCanvas;
    private final FoamCanvas foamCanvas;
    private final ShadowCanvas shadowCanvas;

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

        rocksCanvas = new RocksCanvas(width, height);
        canvasContainer.getChildren().add(rocksCanvas);
        rocksCanvas.setMouseTransparent(true);

        grassCanvas = new GrassCanvas(width, height);
        canvasContainer.getChildren().add(grassCanvas);
        grassCanvas.setMouseTransparent(true);

        shadowCanvas = new ShadowCanvas(width, height);
        canvasContainer.getChildren().add(shadowCanvas);
        shadowCanvas.setMouseTransparent(true);

        // Initialize hover canvas
        hoverCanvas = new Canvas(width, height);
        canvasContainer.getChildren().add(hoverCanvas);
        hoverCanvas.setMouseTransparent(true);

        // Configure ScrollPane
        setupScrollPane(width, height);

        // Draw initial grid
        drawGrid();
    }

    // ================== SCROLL PANE SETUP ==================//

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
                // rocksCanvas.deleteRocks(currentTileX, currentTileY);
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
