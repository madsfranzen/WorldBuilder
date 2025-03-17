plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("org.beryx.jlink") version "3.0.1"
}

repositories { 
    mavenCentral()
    // Add Sonatype snapshots repository for latest JavaFX releases
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

val javaFXVersion = "21.0.2"

dependencies {
    implementation("com.google.guava:guava:32.0.1-jre")
    implementation("org.json:json:20250107")
    
    // JavaFX dependencies with explicit versions
    implementation("org.openjfx:javafx-controls:$javaFXVersion")
    implementation("org.openjfx:javafx-fxml:$javaFXVersion")
    implementation("org.openjfx:javafx-base:$javaFXVersion")
    implementation("org.openjfx:javafx-graphics:$javaFXVersion")
    implementation("org.openjfx:javafx-media:$javaFXVersion")
    implementation("org.openjfx:javafx-swing:$javaFXVersion")
    implementation("org.openjfx:javafx-web:$javaFXVersion")
}

javafx {
    version = javaFXVersion
    modules = listOf(
        "javafx.controls",
        "javafx.fxml",
        "javafx.base",
        "javafx.graphics",
        "javafx.media",
        "javafx.swing",
        "javafx.web"
    )
}

java {
    toolchain { 
        languageVersion.set(JavaLanguageVersion.of(21))  // Using Java 21 LTS to match JavaFX version
    }
}

sourceSets {
    main {
        java.srcDir("src/main/java")
        resources {
            srcDir("src/main/resources")
            include("**/*.png", "**/*.jpg", "**/*.fxml", "**/*.css", "**/*.properties")
        }
    }
}

tasks {
    withType<Copy> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    
    withType<JavaExec> {
        standardInput = System.`in`
        jvmArgs = listOf(
            "--enable-preview",
            "-Xms1g",  // Initial heap size
            "-Xmx4g",  // Maximum heap size 
            "-XX:+UseG1GC", // Use G1 garbage collector
            "-XX:+UseStringDeduplication", // Optimize string usage
            "-Dprism.order=d3d,metal,es2,sw", // Try hardware acceleration first
            "-Dprism.forceGPU=true", // Force GPU usage
            "-Dprism.text=t2k",
            "-Djavafx.verbose=true",
            "-Dprism.maxvram=4g", // Increased texture memory
            "-Dprism.targetfps=60", // Target framerate
            "-Djavafx.animation.pulse=60", // Animation pulse rate
            "-Dprism.dirtyopts=false", // Disable dirty region optimizations
            "-Dquantum.multithreaded=true" // Enable multithreaded rendering
        )
    }
    
    withType<JavaCompile> {
        options.compilerArgs.add("--enable-preview")
    }
    
    processResources {
        filesMatching("**/*.properties") {
            expand(project.properties)
        }
    }
}


application { 
    mainClass.set("com.worldbuilder.App")
    applicationDefaultJvmArgs = listOf(
        "--enable-preview",
        "-Xms1g",  // Initial heap size
        "-Xmx4g",  // Maximum heap size
        "-XX:+UseG1GC", // Use G1 garbage collector
        "-XX:+UseStringDeduplication", // Optimize string usage
        "-Dprism.order=d3d,metal,es2,sw", // Try hardware acceleration first
        "-Dprism.forceGPU=true", // Force GPU usage
        "-Dprism.text=t2k",
        "-Djavafx.verbose=true",
        "-Dprism.maxvram=4g", // Increased texture memory
        "-Dprism.targetfps=60", // Target framerate
        "-Djavafx.animation.pulse=60", // Animation pulse rate
        "-Dprism.dirtyopts=false", // Disable dirty region optimizations
        "-Dquantum.multithreaded=true" // Enable multithreaded rendering
    )
}
