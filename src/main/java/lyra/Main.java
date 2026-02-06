package lyra;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * A GUI for Lyra using FXML.
 */
public class Main extends Application {
    private Lyra lyra = new Lyra("data/lyra.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            javafx.scene.layout.AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("Lyra");
            stage.setResizable(false);
            
            MainWindow mw = fxmlLoader.<MainWindow>getController();
            mw.setLyra(lyra);
            
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
