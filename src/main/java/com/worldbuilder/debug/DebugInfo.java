package com.worldbuilder.debug;

import java.util.LinkedHashMap;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class DebugInfo extends VBox {
    private static final Label debugLabel;
    private static final Map<String, String> categories = new LinkedHashMap<>();
    private static String separator = "\n";
    private static boolean enabled = true;

    static {
        debugLabel = new Label("READY");
        setupStyle();
    }

    private static void setupStyle() {
        // Debug label setup
        debugLabel.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: blue; " +
            "-fx-padding: 10px 20px 20px 20px; " +
            "-fx-font-family: monospace;"
        );
        debugLabel.setAlignment(Pos.TOP_RIGHT);
        debugLabel.setEffect(new DropShadow());
        debugLabel.setTextAlignment(TextAlignment.RIGHT);

        // Container setup
        VBox container = new VBox(debugLabel);
        container.setAlignment(Pos.TOP_RIGHT);
        container.setPadding(new Insets(20));
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
        if (!enabled) return;
        categories.put(category, category + ": " + value);
        updateDisplay();
    }

    private static void updateDisplay() {
        if (!enabled) {
            debugLabel.setText("");
            return;
        }
        debugLabel.setText(String.join(separator, categories.values()));
    }

    public static void setSeparator(String newSeparator) {
        separator = newSeparator;
        updateDisplay();
    }

    public static void setEnabled(boolean isEnabled) {
        enabled = isEnabled;
        debugLabel.setVisible(enabled);
        if (!enabled) {
            debugLabel.setText("");
        }
    }

    public static Label getDebugLabel() {
        return debugLabel;
    }

    public static void clearCategories() {
        categories.clear();
        updateDisplay();
    }
} 