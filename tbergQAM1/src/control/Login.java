package control;

import data_access.SqlDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Appointment;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.stream.Collectors;

/**
 * The Login class controls the login scene, that is the first scene loaded by the application.
 */
public class Login extends Controller {
    /** The LOG_PATH member stores the path for the file to be used for recording login activity. */
    private String LOG_PATH = "login_activity.txt";

    /** Resource bundle for login form. */
    ResourceBundle bundle = ResourceBundle.getBundle("resources.login", Locale.getDefault());

    @FXML private Label header;
    @FXML private Label userLabel;
    @FXML private Label passwordLabel;
    @FXML private TextField userField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label timeLabel;
    @FXML private Label languageLabel;

    /**
     * The initialize method sets the values of fxml components on the login form using assets from the resource bundle.
     */
    @FXML public void initialize() {
        header.setText(bundle.getString("header"));
        userLabel.setText(bundle.getString("userLabel"));
        passwordLabel.setText(bundle.getString("passwordLabel"));
        loginButton.setText(bundle.getString("loginButton"));
        timeLabel.setText(bundle.getString("timeLabel") + " " + TimeZone.getDefault().getDisplayName());
        languageLabel.setText(bundle.getString("languageLabel"));
    }

    /**
     * The login method is the handler for the login button. It uses the input user name and password to create
     * a Data Access Object that is used by the controllers in the application. It also records login attempts,
     * and performs an initial check for upcoming appointments.
     * @param event ActionEvent that activates the login method.
     */
    @FXML public void login(ActionEvent event) {
        try (FileWriter log = new FileWriter(LOG_PATH, true)) {
            log.write("USER: " + userField.getText() + " " + ZonedDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_DATE_TIME));
            try {
                dao = new SqlDB(userField.getText(), passwordField.getText());
                log.write(" LOGIN SUCCESSFUL" + System.lineSeparator());
                // Check for upcoming appointments.
                ObservableList<Appointment> upcomingAppointments = dao.getAppointments().stream()
                        .filter(a -> a.getStart().isBefore(ZonedDateTime.now().plusMinutes(15)) && a.getStart().isAfter(ZonedDateTime.now()))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
                if (!upcomingAppointments.isEmpty()) {
                    StringBuilder userNotification = new StringBuilder();
                    for (Appointment a : upcomingAppointments) {
                        userNotification.append("Appointment#").append(a.getId()).append(" at ").append(formatTime(a.getStart())).append(System.lineSeparator());
                    }
                    messageBox("Upcoming appointment.", String.valueOf(userNotification));
                } else {
                    messageBox("No Upcoming Appointments", "There are no upcoming appointments");
                }
                changeScene(event, MENU_FXML);
            } catch (SQLException e) {
                log.write(" LOGIN FAILED" + System.lineSeparator());
                e.printStackTrace();
                error(bundle.getString("errorMessage"));
                userField.clear();
                passwordField.clear();
            }
        } catch (IOException e) {
            System.out.println("Failed to access log file");
        } catch (Exception e) {
            error(e.getMessage());
        }
    }

}
