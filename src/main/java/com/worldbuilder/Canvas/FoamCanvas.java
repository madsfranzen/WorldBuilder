package com.worldbuilder.Canvas;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.worldbuilder.SpriteLoader;
import com.worldbuilder.debug.DebugInfo;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
/**
 * A specialized Canvas for rendering animated water foam effects.
 * Handles sprite-based animation with automatic frame updates.
 */
public class FoamCanvas extends Canvas {
    // Sprite configuration
    private static final int FRAME_COUNT = 8;
    private static final int TILE_SIZE = 64;
    private static final int GRID_SIZE = 3;  // 3x3 grid
    private static final int FRAME_WIDTH = TILE_SIZE * GRID_SIZE;
    private static final int FRAME_HEIGHT = TILE_SIZE * GRID_SIZE;
    private static final long FRAME_DURATION_NS = 100_000_000; // 100ms in nanoseconds

    private final Image foamSpritesheet;
    private final Set<FoamPosition> activeFoams;
    private final AnimationTimer animator;
    
    private int currentFrame;
    private long lastFrameTime;

    private final FoamTile[][] tileMap;

    /**
     * Represents a position for foam animation on the canvas.
     */
    private static record FoamPosition(int centerX, int centerY) {}

    private static class FoamTile {
    }

    /**
     * Creates a new FoamCanvas with specified dimensions.
     *
     * @param width The width of the canvas
     * @param height The height of the canvas
     * @throws RuntimeException if sprite resources cannot be loaded
     */
    public FoamCanvas(int width, int height) {
        super(width, height);
        this.activeFoams = Collections.synchronizedSet(new HashSet<>());
        this.foamSpritesheet = SpriteLoader.getFoamSpritesheet();
        this.animator = createAnimator();
        this.animator.start();
        this.tileMap = new FoamTile[width][height];
    }

    private AnimationTimer createAnimator() {
        return new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastFrameTime >= FRAME_DURATION_NS) {
                    updateAnimation();
                    lastFrameTime = now;
                }
            }
        };
    }

    private void updateAnimation() {
        currentFrame = (currentFrame + 1) % FRAME_COUNT;
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        
        // Use synchronized set to avoid concurrent modification
        synchronized (activeFoams) {
            activeFoams.forEach(pos -> draw3x3Foam(pos.centerX(), pos.centerY()));
        }
    }

    /**
     * Adds a new foam animation at the specified center position.
     *
     * @param centerX center X coordinate in tile units
     * @param centerY center Y coordinate in tile units
     */
    public void drawFoam(int centerX, int centerY) {
        tileMap[centerX][centerY] = new FoamTile();
        activeFoams.add(new FoamPosition(centerX, centerY));
        DebugInfo.setLastAction("Added FOAM at (" + centerX + ", " + centerY + ")");
    }

    public void deleteFoam(int centerX, int centerY) {
        tileMap[centerX][centerY] = null;
        activeFoams.remove(new FoamPosition(centerX, centerY));
        DebugInfo.setLastAction("Deleted FOAM at (" + centerX + ", " + centerY + ")");
    }

    private void draw3x3Foam(int centerX, int centerY) {
        getGraphicsContext2D().drawImage(
            foamSpritesheet,
            currentFrame * FRAME_WIDTH, 0,
            FRAME_WIDTH, FRAME_HEIGHT,
            (centerX - 1) * TILE_SIZE,
            (centerY - 1) * TILE_SIZE,
            FRAME_WIDTH, FRAME_HEIGHT
        );
    }

    /**
     * Stops the animation and releases resources.
     * Should be called when the canvas is no longer needed.
     */
    public void dispose() {
        animator.stop();
        activeFoams.clear();
    }

    public FoamTile[][] getTileMap() {
        return tileMap;
    }
}