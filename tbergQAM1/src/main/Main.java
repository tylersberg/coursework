package main;

import control.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The Main class handles initially starting the application.
 */
public class Main extends Application {

    /**
     * The start method runs when the application is launched, and loads the initial login scene.
     * @param primaryStage primaryStage for the application.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(Controller.LOGIN_FXML));
            primaryStage.setTitle("Welcome");
            primaryStage.setScene(new Scene(root, 300, 350));
            primaryStage.show();

            ResourceBundle bundle = ResourceBundle.getBundle("resources.login", Locale.getDefault());
            primaryStage.setTitle(bundle.getString("title"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Launches the application.
     * @param args Arguments for application launch.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
