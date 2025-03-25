package com.worldbuilder.Canvas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.worldbuilder.App;
import com.worldbuilder.debug.DebugInfo;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

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
    private final StairsCanvas stairsCanvas;
    private final WallCanvas wallCanvas;
    private final PlateauCanvas plateauCanvas;
    private final BridgeCanvas bridgeCanvas;
    private final BridgeShadowCanvas bridgeShadowCanvas;
    private final List<Canvas> canvasList = new ArrayList<>();
    private final SandFillCanvas sandFillCanvas;
    private final GrassFillCanvas grassFillCanvas;

    private final List<Elevation> elevationCanvasList = new ArrayList<>();

    private String[][][] collisionMap;
    
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
    
            stairsCanvas = new StairsCanvas(width, height);
            canvasList.add(stairsCanvas);
    
            wallCanvas = new WallCanvas(width, height);
            canvasList.add(wallCanvas);
    
            plateauCanvas = new PlateauCanvas(width, height);
            canvasList.add(plateauCanvas);
    
            sandFillCanvas = new SandFillCanvas(width, height);
            canvasList.add(sandFillCanvas);
    
            grassFillCanvas = new GrassFillCanvas(width, height);
            canvasList.add(grassFillCanvas);
    
            bridgeShadowCanvas = new BridgeShadowCanvas(width, height);
            canvasList.add(bridgeShadowCanvas);
    
            bridgeCanvas = new BridgeCanvas(width, height);
            canvasList.add(bridgeCanvas);
    
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
                case STAIRS -> {
                    stairsCanvas.drawStairs(currentTileX, currentTileY, true);
                }
                case DECO -> {
                }
                case WALL -> {
                    wallCanvas.drawWall(currentTileX, currentTileY, true);
                }
                case PLATEAU -> {
                    plateauCanvas.drawPlateau(currentTileX, currentTileY, true);
                }
                case BRIDGE -> {
                    bridgeCanvas.drawBridge(currentTileX, currentTileY, true);
                }
                case BRIDGESHADOW -> {
                    bridgeShadowCanvas.drawBridgeShadow(currentTileX, currentTileY);
                }
                case GRASSFILL -> {
                    grassFillCanvas.drawGrassFill(currentTileX, currentTileY);
                }
                case SANDFILL -> {
                    sandFillCanvas.drawSandFill(currentTileX, currentTileY);
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
                case STAIRS -> {
                    stairsCanvas.deleteStairs(currentTileX, currentTileY);
                }
                case WALL -> {
                    wallCanvas.deleteWall(currentTileX, currentTileY);
                }
                case PLATEAU -> {
                    plateauCanvas.deletePlateau(currentTileX, currentTileY);
                }
                case BRIDGE -> {
                    bridgeCanvas.deleteBridge(currentTileX, currentTileY);
                }
                case BRIDGESHADOW -> {
                    bridgeShadowCanvas.deleteBridgeShadow(currentTileX, currentTileY);
                }
                case GRASSFILL -> {
                    grassFillCanvas.deleteGrassFill(currentTileX, currentTileY);
                }
                case SANDFILL -> {
                    sandFillCanvas.deleteSandFill(currentTileX, currentTileY);
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
    
        // ================== SAVE, LOAD, EXPORT, IMPORT ==================//
    
        public void export() {
            int numberOfCanvases = canvasList.size();
            collisionMap = new String[WORLD_WIDTH][WORLD_HEIGHT][2 + elevationCanvasList.size()];

        String[][][] tileMap = new String[WORLD_WIDTH][WORLD_HEIGHT][numberOfCanvases - 2];

        for (int i = 1; i < numberOfCanvases - 1; i++) {

            Canvas canvas = canvasList.get(i);

            for (int x = 0; x < WORLD_WIDTH; x++) {
                for (int y = 0; y < WORLD_HEIGHT; y++) {
                    switch (canvas.getClass().getSimpleName()) {
                        case "GrassCanvas" -> {
                            if (grassCanvas.getTileMap()[x][y] != null) {
                                tileMap[x][y][i - 1] = "GRASS";
                                collisionMap[x][y][0] = "GRASS";
                            }
                        }
                        case "WaterCanvas" -> {
                            if (waterCanvas.getTileMap()[x][y]) {
                                tileMap[x][y][i - 1] = "WATER";
                                collisionMap[x][y][0] = "WATER";
                            }
                        }
                        case "FoamCanvas" -> {
                            if (foamCanvas.getTileMap()[x][y] != null) {
                                tileMap[x][y][i - 1] = "FOAM";
                            }
                        }
                        case "SandCanvas" -> {
                            if (sandCanvas.getTileMap()[x][y] != null) {
                                tileMap[x][y][i - 1] = "SAND";
                                collisionMap[x][y][0] = "SAND";
                            }
                        }
                        case "RocksCanvas" -> {
                            if (rocksCanvas.getTileMap()[x][y] != null) {
                                int rockType = rocksCanvas.getTileMap()[x][y].getRockType() + 1;
                                tileMap[x][y][i - 1] = "ROCKS" + rockType;
                            }
                        }
                        case "ShadowCanvas" -> {
                            if (shadowCanvas.getTileMap()[x][y] != null) {
                                tileMap[x][y][i - 1] = "SHADOW";
                            }
                        }
                        case "WallCanvas" -> {
                            if (wallCanvas.getTileMap()[x][y] != null) {
                                tileMap[x][y][i - 1] = "WALL";
                                collisionMap[x][y][0] = "WALL";
                            }
                        }
                        case "PlateauCanvas" -> {
                            if (plateauCanvas.getTileMap()[x][y] != null) {
                                tileMap[x][y][i - 1] = "PLATEAU";
                                collisionMap[x][y][1] = "PLATEAU";
                            }
                        }
                        case "StairsCanvas" -> {
                            if (stairsCanvas.getTileMap()[x][y] != null) {
                                tileMap[x][y][i - 1] = "STAIRS";
                                collisionMap[x][y][0] = "STAIRS";
                            }
                        }
                        case "BridgeCanvas" -> {
                            if (bridgeCanvas.getTileMap()[x][y] != null) {
                                tileMap[x][y][i - 1] = "BRIDGE";
                                collisionMap[x][y][1] = "BRIDGE";
                            }
                        }
                        case "BridgeShadowCanvas" -> {
                            if (bridgeShadowCanvas.getTileMap()[x][y]) {
                                tileMap[x][y][i - 1] = "BRIDGESHADOW";
                            }
                        }
                        case "SandFillCanvas" -> {
                            if (sandFillCanvas.getTileMap()[x][y] != null) {
                                tileMap[x][y][i - 1] = "SANDFILL";
                            }
                        }
                        case "GrassFillCanvas" -> {
                            if (grassFillCanvas.getTileMap()[x][y] != null) {
                                tileMap[x][y][i - 1] = "GRASSFILL";
                            }
                        }
                        default -> {
                        }
                    }
                }
            }
        }
        // convert tileMap to json
        String json = new Gson().toJson(tileMap);
        // save json to file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save World");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        java.io.File file = fileChooser.showSaveDialog(this.getScene().getWindow());
        if (file != null) {
            try {
                java.io.FileWriter writer = new java.io.FileWriter(file);
                writer.write(json);
                writer.close();
            } catch (Exception e) {
                DebugInfo.setError("Failed to save world: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // save collisionMap to file
        String collisionJson = new Gson().toJson(collisionMap);
        FileChooser collisionFileChooser = new FileChooser();
        collisionFileChooser.setTitle("Save Collision Map");
        collisionFileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        java.io.File collisionFile = collisionFileChooser.showSaveDialog(this.getScene().getWindow());
        if (collisionFile != null) {
            try {
                java.io.FileWriter writer = new java.io.FileWriter(collisionFile);
                writer.write(collisionJson);
                writer.close();
            } catch (Exception e) {
                DebugInfo.setError("Failed to save collision map: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void importfunc() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import World");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        java.io.File file = fileChooser.showOpenDialog(this.getScene().getWindow());
        if (file != null) {
            String json = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            String[][][] tileMap = new Gson().fromJson(json, String[][][].class);

            int numberOfCanvases = tileMap[0][0].length;

            // WalkableCanvas walkableCanvas0 = new WalkableCanvas(WORLD_WIDTH,
            // WORLD_HEIGHT);
            // WalkableCanvas walkableCanvas1 = new WalkableCanvas(WORLD_WIDTH,
            // WORLD_HEIGHT);
            // WalkableCanvas walkableCanvas2 = new WalkableCanvas(WORLD_WIDTH,
            // WORLD_HEIGHT);

            // ArrayList<WalkableCanvas> walkableCanvases = new ArrayList<>();

            // walkableCanvases.add(walkableCanvas0);
            // walkableCanvases.add(walkableCanvas1);
            // walkableCanvases.add(walkableCanvas2);

            // String[][][] collisionMap

            // PlayerCanvas playerCanvas0 = new PlayerCanvas(WORLD_WIDTH, WORLD_HEIGHT);
            // PlayerCanvas playerCanvas1 = new PlayerCanvas(WORLD_WIDTH, WORLD_HEIGHT);
            // PlayerCanvas playerCanvas2 = new PlayerCanvas(WORLD_WIDTH, WORLD_HEIGHT);

            // ArrayList<PlayerCanvas> playerCanvases = new ArrayList<>();

            // playerCanvases.add(playerCanvas);
            // playerCanvases.add(playerCanvas2);
            // playerCanvases.add(playerCanvas3);

            // if player with z-index 0 wants to move to walkablecanvas0 (z index 0)
            // Check if walkablecanvas0[player.x][player.y] is walkable -> collisionMap ==
            // walk
            //
            // if player wants to move up a z-layer -> check if [x+1][y][z+1] == "walk" and
            // [x][y][z] == "stairs"

            for (int i = 0; i < numberOfCanvases; i++) {
                for (int x = 0; x < WORLD_WIDTH; x++) {
                    for (int y = 0; y < WORLD_HEIGHT; y++) {
                        String tile = tileMap[x][y][i];
                        if (tile != null) {
                            switch (tile) {
                                case "GRASS" -> {
                                    grassCanvas.drawGrass(x, y, true);
                                }
                                case "WATER" -> {
                                    waterCanvas.drawWater(x, y);
                                }
                                case "FOAM" -> {
                                    foamCanvas.drawFoam(x, y);
                                }
                                case "SAND" -> {
                                    sandCanvas.drawSand(x, y, true);
                                }
                                case "ROCKS1" -> {
                                    rocksCanvas.drawRocks(x, y, 1);
                                }
                                case "ROCKS2" -> {
                                    rocksCanvas.drawRocks(x, y, 2);
                                }
                                case "ROCKS3" -> {
                                    rocksCanvas.drawRocks(x, y, 3);
                                }
                                case "ROCKS4" -> {
                                    rocksCanvas.drawRocks(x, y, 4);
                                }
                                case "SHADOW" -> {
                                    shadowCanvas.drawShadow(x, y);
                                }
                                case "WALL" -> {
                                    wallCanvas.drawWall(x, y, true);
                                }
                                case "PLATEAU" -> {
                                    plateauCanvas.drawPlateau(x, y, true);
                                }
                                case "STAIRS" -> {
                                    stairsCanvas.drawStairs(x, y, true);
                                }
                                case "BRIDGE" -> {
                                    bridgeCanvas.drawBridge(x, y, true);
                                }
                                case "BRIDGESHADOW" -> {
                                    bridgeShadowCanvas.drawBridgeShadow(x, y);
                                }
                                case "GRASSFILL" -> {
                                    grassFillCanvas.drawGrassFill(x, y);
                                }
                                case "SANDFILL" -> {
                                    sandFillCanvas.drawSandFill(x, y);
                                }
                                default -> {
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void save() {
        // TODO: Implement save
    }

    public void load() {
        // TODO: Implement load
    }

    // ================== GETTERS ==================//

    public int getCurrentTileX() {
        return currentTileX;
    }

    public int getCurrentTileY() {
        return currentTileY;
    }
}
