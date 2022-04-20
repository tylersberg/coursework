package control;

import data_access.DataAccessor;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The Controller contains methods and attributes useful to other controllers. Other controllers inherit from this class.
 */
public abstract class Controller {

    /**
     * The paths to the fxml files used in the application are stored here as static final members.
     */
    final static public String SCHEDULE_FXML = "/view/schedule.fxml";
    final static public String CUSTOMERS_FXML = "/view/customers.fxml";
    final static public String CUSTOMER_FORM_FXML = "/view/customerForm.fxml";
    final static public String APPOINTMENT_FORM_FXML = "/view/appointmentForm.fxml";
    final static public String LOGIN_FXML = "/view/login.fxml";
    final static public String MENU_FXML = "/view/menu.fxml";
    final static public String REPORT_FXML = "/view/reports.fxml";

    static protected DataAccessor dao;

    /** Initialize method is implemented by all controllers and executes when the controller object is created.*/
    public abstract void initialize();

    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm MMM dd");
    public String formatTime(ZonedDateTime time) {
        return time.withZoneSameInstant(ZoneId.systemDefault()).format(TIME_FORMAT);
    }
    /**
     * Changes the current scene.
     * @param event ActionEvent that triggered the scene change.
     * @param fxmlPath FXML file to use for the new scene.
     * @throws IOException
     */
    protected void changeScene(ActionEvent event, String fxmlPath) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = new Scene(parent);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Creates a new window, disable focus to the main window while open.
     * @param event ActionEvent that triggered the creation of the new window.
     * @param fxmlPath FXML file for the new window.
     * @param title Title of new window.
     * @throws IOException
     */
    protected void popupScene(ActionEvent event, String fxmlPath, String title) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    /**
     * Creates an error dialog box.
     * @param errorMessage Message to be displayed in the error box.
     */
    public void error(String errorMessage) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Error");
        error.setContentText(errorMessage);
        error.showAndWait();
    }

    /**
     * Creates a dialog box that displays a message to the user.
     * @param message Message to display.
     */
    public void messageBox(String message) {
        Alert box = new Alert(Alert.AlertType.INFORMATION);
        box.setTitle("Attention");
        box.setHeaderText(message);
        box.showAndWait();
    }
    /**
     * Creates a dialog box that displays a message to the user.
     * @param message Message to display.
     * @param messageBody Longer form message.
     */
    public void messageBox(String message, String messageBody) {
        Alert box = new Alert(Alert.AlertType.INFORMATION);
        box.setTitle("Attention");
        box.setHeaderText(message);
        box.setContentText(messageBody);
        box.showAndWait();
    }

    /**
     * Creates a confirmation dialog box.
     * @param confirmMessage Message to be displayed in the confirmation box.
     * @return True if the user clicks the ok button, false otherwise.
     */
    public boolean confirm(String confirmMessage) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Action");
        confirmation.setContentText(confirmMessage);
        if (confirmation.showAndWait().get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Closes the active window.
     * @param event ActionEvent used to get the Window.
     */
    protected void close(ActionEvent event) {
        ((Stage)((Node) event.getSource()).getScene().getWindow()).close();
    }
}
