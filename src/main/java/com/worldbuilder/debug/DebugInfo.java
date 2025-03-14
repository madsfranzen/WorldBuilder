package com.worldbuilder.debug;

import java.util.LinkedHashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class DebugInfo extends VBox {
    private static AnchorPane container;
    private static final Label debugLabel;
    private static final Label lastAction;
    private static final Label errorLabel;
    private static final Map<String, String> categories = new LinkedHashMap<>();
    private static String separator = "\n";
    private static boolean enabled = true;

    static {
        debugLabel = new Label("READY");
        lastAction = new Label("");
        errorLabel = new Label("");
        setupStyle();
    }

    private static void setupStyle() {
        // Debug label setup
        debugLabel.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-text-fill: blue; " +
                        "-fx-padding: 10px 20px 20px 20px; " +
                        "-fx-font-family: monospace;");
        debugLabel.setAlignment(Pos.TOP_RIGHT);
        debugLabel.setEffect(new DropShadow());
        debugLabel.setTextAlignment(TextAlignment.RIGHT);

        lastAction.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-text-fill: blue; " +
                        "-fx-padding: 10px 20px 20px 20px; " +
                        "-fx-font-family: monospace;");
        lastAction.setAlignment(Pos.BOTTOM_RIGHT);
        lastAction.setEffect(new DropShadow());
        lastAction.setTextAlignment(TextAlignment.RIGHT);

        // Error label setup
        errorLabel.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-text-fill: red; " +
                        "-fx-padding: 10px 20px 20px 20px; " +
                        "-fx-font-family: monospace;");
        errorLabel.setAlignment(Pos.BOTTOM_RIGHT);
        errorLabel.setEffect(new DropShadow());
        errorLabel.setTextAlignment(TextAlignment.RIGHT);

        // Create AnchorPane container instead of VBox
        container = new AnchorPane();
        
        // Add all labels to the AnchorPane
        container.getChildren().addAll(debugLabel, lastAction, errorLabel);
        
        // Position debugLabel at the top-right
        AnchorPane.setTopAnchor(debugLabel, 20.0);
        AnchorPane.setRightAnchor(debugLabel, 20.0);
        
        // Position lastAction at the bottom-right
        AnchorPane.setBottomAnchor(lastAction, 60.0);  // Moved up to make room for error label
        AnchorPane.setRightAnchor(lastAction, 20.0);
        
        // Position errorLabel at the bottom-right, below lastAction
        AnchorPane.setBottomAnchor(errorLabel, 20.0);
        AnchorPane.setRightAnchor(errorLabel, 20.0);
        
        container.setPadding(new Insets(0));
        container.setPickOnBounds(false);
    }

    // Static methods
    public static void updatePosition(String category, double x, double y) {
        setPosition(category, x, y);
    }

    public static void updateCoordinates(String category, int x, int y) {
        setCoordinates(category, x, y);
    }

    public static void updateScale(String category, double x, double y) {
        setScale(category, x, y);
    }

    public static void setPosition(String category, double x, double y) {
        setCategory(category, String.format("(%.1f, %.1f)", x, y));
    }

    public static void setCoordinates(String category, int x, int y) {
        setCategory(category, String.format("(%d, %d)", x, y));
    }

    public static void setScale(String category, double x, double y) {
        setCategory(category, String.format("(%.2f, %.2f)", x, y));
    }

    public static void setCategory(String category, String value) {
        if (!enabled)
            return;
        categories.put(category, category + ": " + value);
        updateDisplay();
    }

    public static void updateSelectedLayer(String message) {
        if (!enabled)
            return;
        categories.put("LOG", message);
        updateDisplay();
    }

    private static void updateDisplay() {
        if (!enabled) {
            Platform.runLater(() -> debugLabel.setText(""));
            return;
        }
        final String text = String.join(separator, categories.values());
        Platform.runLater(() -> debugLabel.setText(text));
    }

    public static void setSeparator(String newSeparator) {
        separator = newSeparator;
        updateDisplay();
    }

    public static void setEnabled(boolean isEnabled) {
        enabled = isEnabled;
        Platform.runLater(() -> {
            debugLabel.setVisible(enabled);
            lastAction.setVisible(enabled);
            errorLabel.setVisible(enabled);
            if (!enabled) {
                debugLabel.setText("");
                lastAction.setText("");
                errorLabel.setText("");
            }
        });
    }

    public static AnchorPane getBox() {
        return container;
    }

    public static void setLastAction(String action) {
        if (!enabled)
            return;
        Platform.runLater(() -> {
            lastAction.setText(action);
            clearError();
        try {
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    Platform.runLater(() -> lastAction.setText(""));
                } catch (InterruptedException e) {
                    // Ignore interruption
                }
            }).start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        });
    }

    public static void setError(String error) {
        if (!enabled)
            return;
        Platform.runLater(() -> errorLabel.setText(error));
        // Schedule error to be cleared after 3 seconds
        try {
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    Platform.runLater(() -> clearError());
                } catch (InterruptedException e) {
                    // Ignore interruption
                }
            }).start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void clearError() {
        Platform.runLater(() -> errorLabel.setText(""));
    }

    public static void clearCategories() {
        categories.clear();
        updateDisplay();
    }
}