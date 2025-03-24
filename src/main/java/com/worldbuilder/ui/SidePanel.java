package com.worldbuilder.ui;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.worldbuilder.App;
import com.worldbuilder.Canvas.WorldCanvas;
import com.worldbuilder.TileType;
import com.worldbuilder.debug.DebugInfo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class SidePanel extends HBox {
    public final int width = 200;
    private TileType selectedLayer;
    private final VBox mainPanel;
    private final TypesPanel typesPanel;
    private Pane overlayPane;
    private int windowWidth = 1200; // Default window width

    private final Button saveButton = new Button("Save");
    private final Button loadButton = new Button("Load");
    private final Button exportButton = new Button("Export");
    private final Button importButton = new Button("Import");

    public SidePanel() {
        super(0); // No spacing between panels
        
        mainPanel = new VBox(12);
        mainPanel.setAlignment(Pos.TOP_CENTER);
        mainPanel.setPadding(new Insets(10));
        mainPanel.setPrefWidth(width);
        mainPanel.setMaxWidth(width);
        mainPanel.setMinWidth(width);

        typesPanel = new TypesPanel();
        typesPanel.setVisible(false);
        typesPanel.setManaged(false);

        // Load the stylesheet safely
        String cssPath = Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm();
        getStylesheets().add(cssPath);

        // Create list of button configurations
        record ButtonConfig(String text, TileType type) {
        }
        var buttonConfigs = java.util.List.of(
                new ButtonConfig("WATER", TileType.WATER),
                new ButtonConfig("FOAM", TileType.FOAM),
                new ButtonConfig("ROCKS", TileType.ROCKS),
                new ButtonConfig("SAND", TileType.SAND),
                new ButtonConfig("GRASS", TileType.GRASS),
                new ButtonConfig("SHADOW", TileType.SHADOW),
                new ButtonConfig("STAIRS", TileType.STAIRS),
                new ButtonConfig("PLATEAU", TileType.PLATEAU),
                new ButtonConfig("WALL", TileType.WALL),
                new ButtonConfig("GRASSFILL", TileType.GRASSFILL),
                new ButtonConfig("SANDFILL", TileType.SANDFILL),
                new ButtonConfig("BRIDGE", TileType.BRIDGE),
                new ButtonConfig("BRIDGESHADOW", TileType.BRIDGESHADOW),
                new ButtonConfig("DECO", TileType.DECO));

        // Create and configure all buttons
        final List<Button> buttons = buttonConfigs.stream()
                .map(config -> {
                    Button button = new Button(config.text());
                    button.getStyleClass().add("side-panel-button");
                    button.setOnAction(event -> {
                        selectedLayer = config.type();
                        DebugInfo.updateSelectedLayer("SELECTED LAYER: " + selectedLayer);
                        // Update all button states in one go
                        mainPanel.getChildren().stream()
                            .filter(node -> node instanceof Button)
                            .map(node -> (Button) node)
                            .forEach(b -> b.setDisable(b == button));
                        
                        // Show/hide rock type panel
                        boolean isRocksSelected = config.type() == TileType.ROCKS;
                        if (isRocksSelected) {
                            typesPanel.showRocks();
                        }
                        typesPanel.setVisible(isRocksSelected);
                        typesPanel.setManaged(isRocksSelected);
                        if (!isRocksSelected) {
                            typesPanel.resetSelection();
                        }
                        updateOverlayWidth();
                    });
                    return button;
                })
                .toList();
        mainPanel.getChildren().addAll(buttons);

        exportButton.setOnAction(event -> {
            System.out.println("Exporting");
            WorldCanvas worldCanvas = App.getWorldCanvas();
            worldCanvas.export();
        });

        importButton.setOnAction(event -> {
            WorldCanvas worldCanvas = App.getWorldCanvas();
            try {
                worldCanvas.importfunc();
            } catch (IOException e) {
                DebugInfo.setError("Failed to import world: " + e.getMessage());
                e.printStackTrace();
            }
        });


        exportButton.getStyleClass().add("importExportButton");
        importButton.getStyleClass().add("importExportButton");
        HBox buttonBox = new HBox(12);
        HBox.setMargin(exportButton, new Insets(12, 0, 0, 0));
        HBox.setMargin(importButton, new Insets(12, 0, 0, 0));
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(exportButton, importButton);
        mainPanel.getChildren().addAll(buttonBox);
        getChildren().addAll(mainPanel, typesPanel);
    }

    public void setOverlayPane(Pane overlayPane, int windowWidth) {
        this.overlayPane = overlayPane;
        this.windowWidth = windowWidth;
        updateOverlayWidth();
        
        // Add listener for when scene is available
        overlayPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                updateOverlayWidth();
            }
        });
    }

    private void updateOverlayWidth() {
        if (overlayPane != null) {
            int totalWidth = width + (typesPanel.isVisible() ? typesPanel.width : 0);
            int availableWidth = windowWidth - totalWidth;
            overlayPane.setMaxWidth(availableWidth);
            overlayPane.setPrefWidth(availableWidth);
            overlayPane.setMinWidth(availableWidth);
        }
    }

    public int getTotalWidth() {
        return width + (typesPanel.isVisible() ? width : 0);
    }

    public TileType getSelectedLayer() {
        return selectedLayer;
    }

    public int getSelectedRockType() {
        return typesPanel.getSelectedIndex();
    }
}
