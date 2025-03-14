package com.worldbuilder.Canvas;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.worldbuilder.debug.DebugInfo;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class RocksCanvas extends Canvas {

    // Constants for tile dimensions and animation properties
    private static final int TILE_SIZE = 64;
    private static final int GRID_SIZE = 2;  // 3x3 grid for output size
    private static final int FRAME_WIDTH = TILE_SIZE * GRID_SIZE;
    private static final int FRAME_HEIGHT = TILE_SIZE * GRID_SIZE;
    private static final int FRAME_COUNT = 8;
    private static final long FRAME_DURATION_NS = 100_000_000; // 100ms in nanoseconds

    // Load the 4 rock type images
    private static final Image ROCKS_IMAGE1 = new Image(
            RocksCanvas.class.getResourceAsStream("/assets/Terrain/Water/Rocks/Rocks_01.png"));
    private static final Image ROCKS_IMAGE2 = new Image(
            RocksCanvas.class.getResourceAsStream("/assets/Terrain/Water/Rocks/Rocks_02.png"));
    private static final Image ROCKS_IMAGE3 = new Image(
            RocksCanvas.class.getResourceAsStream("/assets/Terrain/Water/Rocks/Rocks_03.png"));
    private static final Image ROCKS_IMAGE4 = new Image(
            RocksCanvas.class.getResourceAsStream("/assets/Terrain/Water/Rocks/Rocks_04.png"));

    private static final Image[] ROCKS_IMAGES = { ROCKS_IMAGE1, ROCKS_IMAGE2, ROCKS_IMAGE3, ROCKS_IMAGE4 };

    private final GraphicsContext gc;
    private final RocksTile[][] tileMap;
    private final AnimationTimer animator;
    private int currentFrame;
    private long lastFrameTime;
    private final Set<RocksPosition> activeRocks;

        public RocksCanvas(int width, int height) {
            super(width, height);
            this.gc = getGraphicsContext2D();
            this.tileMap = new RocksTile[width][height];
            this.animator = createAnimator();
            this.animator.start();
            this.activeRocks = Collections.synchronizedSet(new HashSet<>());
        }

        private static record RocksPosition(int centerX, int centerY) {}
    
        private static class RocksTile {
            final int rockType;

            RocksTile(int rockType) {
                this.rockType = rockType;
            }
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
            gc.clearRect(0, 0, getWidth(), getHeight());
            
            // Use synchronized set to avoid concurrent modification
            synchronized (activeRocks) {
                activeRocks.forEach(pos -> draw2x2Rocks(pos.centerX(), pos.centerY()));
            }
        }
    
        /**
         * Adds a new foam animation at the specified center position.
         *
         * @param x center X coordinate in tile units
         * @param y center Y coordinate in tile units
         */
        public void drawRocks(int x, int y, int rockType) {
            tileMap[x][y] = new RocksTile(rockType);
            activeRocks.add(new RocksPosition(x, y));
        DebugInfo.setLastAction("Added ROCKS at (" + x + ", " + y + ")");
    }

    // This centers the image on the tile
    private void draw2x2Rocks(int x, int y) {
        getGraphicsContext2D().drawImage(
            ROCKS_IMAGES[tileMap[x][y].rockType],
            currentFrame * FRAME_WIDTH, 0,
            FRAME_WIDTH, FRAME_HEIGHT,
            (x - 1) * TILE_SIZE + TILE_SIZE / 2,
            (y - 1) * TILE_SIZE + TILE_SIZE / 2,
            FRAME_WIDTH, FRAME_HEIGHT
        );
    }

    public void deleteRocks(int x, int y) {
        tileMap[x][y] = null;
        activeRocks.remove(new RocksPosition(x, y));
        DebugInfo.setLastAction("Deleted ROCKS at (" + x + ", " + y + ")");
    }

    public void dispose() {
        animator.stop();
    }
}
