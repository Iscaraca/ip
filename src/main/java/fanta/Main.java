package fanta;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * GUI entry point using JavaFX.
 */
public class Main extends Application {
    private final Fanta fanta = new Fanta("data/fanta.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("Fanta");
            fxmlLoader.<MainWindow>getController().setFanta(fanta);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
