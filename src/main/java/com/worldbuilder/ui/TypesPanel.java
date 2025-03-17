package com.worldbuilder.ui;

import java.util.ArrayList;
import java.util.List;

import com.worldbuilder.debug.DebugInfo;
import com.worldbuilder.SpriteLoader;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TypesPanel extends VBox {
    public final int width = 120;
    private int selectedType = 0;
    private String typeName;
    private final Label titleLabel = new Label();
    private int selectedIndex = 0;

    private final List<Button> buttons = new ArrayList<>();

    public TypesPanel() {
        super(12);

        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(10, 10, 10, -10));

        setPrefWidth(width);
        setMaxWidth(width);
        setMinWidth(width);

        // Load the stylesheet
        String cssPath = getClass().getResource("/styles.css").toExternalForm();
        getStylesheets().add(cssPath);

        // Configure title
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        titleLabel.setPadding(new Insets(0, 0, 10, 0));
    }


    public void resetSelection() {
        selectedType = 0;
        selectedIndex = 0;
        getChildren().stream()
            .filter(node -> node instanceof Button)
            .map(node -> (Button) node)
            .forEach(button -> button.setDisable(false));
    }

    public void showRocks() {
        typeName = "ROCKS";
        getChildren().clear();
        titleLabel.setText(typeName + " Types");
        getChildren().add(titleLabel);

        for (int i = 0; i < 3; i++) {
            final int typeIndex = i;
            Button button = new Button(typeName + " TYPE " + (typeIndex + 1));
            buttons.add(button);
            button.setPrefWidth(64);
            button.setMinWidth(64);
            button.setMaxWidth(64);
            button.setPrefHeight(64);
            button.setMinHeight(64);
            button.setMaxHeight(64);
            
            // Get rock image from SpriteLoader
            Image rockImage = SpriteLoader.getRocksImage(typeIndex + 1);
            
            // Extract single frame from sprite sheet based on index
            WritableImage frame = SpriteLoader.extractFrame(rockImage, 0, 8); // 8 frames in the spritesheet
            
            ImageView imageView = new ImageView(frame);
            imageView.setFitWidth(85);
            imageView.setFitHeight(85);
            imageView.setTranslateX(-20);
            imageView.setTranslateY(0);
            button.setGraphic(imageView);
            button.setFocusTraversable(false);
            button.setOnAction(_ -> {
                selectedIndex = typeIndex;
                selectedType = typeIndex + 1;
                buttons.stream()
                    .filter(b -> b != button)
                    .forEach(b -> b.setDisable(false));
                button.setDisable(true);
                DebugInfo.updateSelectedLayer("SELECTED " + typeName + " TYPE: " + selectedType);
            });
            getChildren().add(button);
        }
    } 

    public int getSelectedIndex() {
        return selectedIndex;
    }
}
