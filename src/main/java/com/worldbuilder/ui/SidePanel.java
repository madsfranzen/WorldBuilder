package com.worldbuilder.ui;

import java.util.List;
import java.util.Objects;

import com.worldbuilder.TileType;
import com.worldbuilder.debug.DebugInfo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class SidePanel extends VBox {
    public final int width = 200;

    private TileType selectedLayer;

    public SidePanel() {
        super(12);

        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(10));

        setPrefWidth(width);
        setMaxWidth(width);
        setMinWidth(width);

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
                new ButtonConfig("FILL", TileType.FILL),
                new ButtonConfig("BRIDGE", TileType.BRIDGE),
                new ButtonConfig("BRIDGE SHADOW", TileType.BRIDGE_SHADOW),
                new ButtonConfig("DECO", TileType.DECO));

        // Create and configure all buttons
        final List<Button> buttons = buttonConfigs.stream()
                .map(config -> {
                    Button button = new Button(config.text());
                    button.getStyleClass().add("side-panel-button");
                    button.setOnAction(_ -> {
                        selectedLayer = config.type();
                        DebugInfo.updateSelectedLayer("SELECTED LAYER: " + selectedLayer);
                        // Update all button states in one go
                        getChildren().stream()
                            .filter(node -> node instanceof Button)
                            .map(node -> (Button) node)
                            .forEach(b -> b.setDisable(b == button));
                    });
                    return button;
                })
                .toList();
        getChildren().addAll(buttons);
    }

    public TileType getSelectedLayer() {
        return selectedLayer;
    }

}
