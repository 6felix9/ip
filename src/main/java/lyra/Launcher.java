package lyra;

/**
 * A launcher class to workaround classpath issues for bundling JavaFX with Shadow JAR.
 */
public class Launcher {
    public static void main(String[] args) {
        Main.launch(Main.class, args);
    }
}
