package com.worldbuilder;

import com.worldbuilder.Canvas.WorldCanvas;
import com.worldbuilder.debug.DebugInfo;
import com.worldbuilder.ui.SidePanel;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;
    
    static SidePanel sidePanel = new SidePanel();
    static WorldCanvas worldCanvas = new WorldCanvas(32, 32);
    
    public static void main(String[] args) {
        // Hardware acceleration settings
        System.setProperty("prism.order", "d3d,metal,es2,sw"); // Try hardware acceleration first
        System.setProperty("prism.forceGPU", "true"); // Force GPU usage
        System.setProperty("prism.text", "t2k");
        System.setProperty("javafx.animation.fullspeed", "true");
        System.setProperty("prism.maxvram", "4g"); // Increased texture memory
        
        // Performance optimizations
        System.setProperty("prism.targetfps", "60");  // Target framerate
        System.setProperty("javafx.animation.pulse", "60"); // Animation pulse rate
        System.setProperty("prism.vsync", "false"); // Disable vsync if not needed
        System.setProperty("prism.dirtyopts", "false"); // Disable dirty region optimizations
        System.setProperty("quantum.multithreaded", "true"); // Enable multithreaded rendering
      
        System.out.println("Hello, WorldBuilder 2.0!");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        // Create main components with access to debug info
        BorderPane root = new BorderPane();

        // Stack the debug overlay on top of main application
        StackPane overlayPane = new StackPane(worldCanvas, DebugInfo.getBox());
        StackPane.setAlignment(DebugInfo.getBox(), Pos.TOP_RIGHT);

        overlayPane.setMaxSize(WINDOW_WIDTH - sidePanel.getTotalWidth(), WINDOW_HEIGHT);
        overlayPane.setMinSize(WINDOW_WIDTH - sidePanel.getTotalWidth(), WINDOW_HEIGHT);
        overlayPane.setPrefSize(WINDOW_WIDTH - sidePanel.getTotalWidth(), WINDOW_HEIGHT);
        sidePanel.setOverlayPane(overlayPane, WINDOW_WIDTH);

        root.setCenter(overlayPane);
        root.setLeft(sidePanel);

        root.setStyle("-fx-background-color: #f0f0f0;");
        
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        // Set background color for the scene
        scene.setFill(javafx.scene.paint.Color.LIGHTGRAY);
        
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("WorldBuilder 2.0");

        primaryStage.show();
    }

    public static SidePanel getSidePanel() {
        return sidePanel;
    }

    public WorldCanvas getWorldCanvas() {
        return worldCanvas;
    }
}
