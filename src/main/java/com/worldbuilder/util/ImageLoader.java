package com.worldbuilder.util;

import java.io.InputStream;

import javafx.scene.image.Image;

/**
 * Utility class for loading images with memory optimization settings.
 * Centralizes image loading to ensure consistent memory usage patterns.
 */
public class ImageLoader {
    
    /**
     * Loads an image with optimized memory settings.
     * 
     * @param resourcePath Path to the image resource
     * @param width Width to scale the image to
     * @param height Height to scale the image to
     * @return The loaded image with optimized settings
     */
    public static Image loadImage(String resourcePath, double width, double height) {
        try (InputStream is = ImageLoader.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new RuntimeException("Resource not found: " + resourcePath);
            }
            return new Image(is, width, height, true, true);
        } catch (Exception e) {
            throw new RuntimeException("Error loading image: " + resourcePath, e);
        }
    }
    
    /**
     * Loads an image with optimized memory settings from a specific class's resources.
     * 
     * @param clazz The class to load the resource from
     * @param resourcePath Path to the image resource
     * @param width Width to scale the image to
     * @param height Height to scale the image to
     * @return The loaded image with optimized settings
     */
    public static Image loadImage(Class<?> clazz, String resourcePath, double width, double height) {
        try (InputStream is = clazz.getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new RuntimeException("Resource not found: " + resourcePath);
            }
            return new Image(is, width, height, true, true);
        } catch (Exception e) {
            throw new RuntimeException("Error loading image: " + resourcePath, e);
        }
    }
    
    /**
     * Loads a spritesheet with optimized memory settings.
     * 
     * @param resourcePath Path to the spritesheet resource
     * @param frameWidth Width of a single frame
     * @param frameHeight Height of a single frame
     * @param frameCount Number of frames in the spritesheet
     * @return The loaded spritesheet with optimized settings
     */
    public static Image loadSpritesheet(String resourcePath, double frameWidth, double frameHeight, int frameCount) {
        return loadImage(resourcePath, frameWidth * frameCount, frameHeight);
    }
    
    /**
     * Loads a spritesheet with optimized memory settings from a specific class's resources.
     * 
     * @param clazz The class to load the resource from
     * @param resourcePath Path to the spritesheet resource
     * @param frameWidth Width of a single frame
     * @param frameHeight Height of a single frame
     * @param frameCount Number of frames in the spritesheet
     * @return The loaded spritesheet with optimized settings
     */
    public static Image loadSpritesheet(Class<?> clazz, String resourcePath, 
                                       double frameWidth, double frameHeight, int frameCount) {
        return loadImage(clazz, resourcePath, frameWidth * frameCount, frameHeight);
    }
} 