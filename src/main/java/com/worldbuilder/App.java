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
    public static void main(String[] args) {
        System.out.println("Hello, WorldBuilder 2.0!");
        launch(args);
    }

    static SidePanel sidePanel = new SidePanel();
    static WorldCanvas worldCanvas = new WorldCanvas(32, 32);
    
        @Override
        public void start(Stage primaryStage) throws Exception {
            final int WINDOW_WIDTH = 1200;
            final int WINDOW_HEIGHT = 800;
    
            // Create main components with access to debug info
            BorderPane root = new BorderPane();
    
            // Stack the debug overlay on top of main application
            StackPane overlayPane = new StackPane(worldCanvas, DebugInfo.getDebugLabel());
            StackPane.setAlignment(DebugInfo.getDebugLabel(), Pos.TOP_RIGHT);
    
            overlayPane.setMaxSize(WINDOW_WIDTH - sidePanel.width, WINDOW_HEIGHT);
            overlayPane.setMinSize(WINDOW_WIDTH - sidePanel.width, WINDOW_HEIGHT);
            overlayPane.setPrefSize(WINDOW_WIDTH - sidePanel.width, WINDOW_HEIGHT);
    
            root.setCenter(overlayPane);
            root.setLeft(sidePanel);
    
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
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
