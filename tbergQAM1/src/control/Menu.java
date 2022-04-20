package control;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * The Menu class controls the main menu bar of the application and handles loading different scenes into the content
 * pane underneath the menubar.
 */
public class Menu extends Controller{
    /** The contentController member holds the controller object for the scene currently loaded in the content pane */
    private Controller contentController;
    @FXML private Pane content;

    /**
     * The initialize method sets the content pane to the schedule initially.
     */
    @FXML public void initialize() {
        try {
            setContent(SCHEDULE_FXML);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The setContent method loads an fxml path into the content pane,
     * and sets contentController to the controller for that scene.
     * @param fxmlPath Path to fxml file.
     * @throws IOException If the fxml path is not valid.
     */
    private void setContent(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        content.getChildren().setAll( ((Node)loader.load(getClass().getResourceAsStream(fxmlPath))) );
        contentController = loader.getController();
    }

    /**
     * Handles loading the customers scene into content when Customers is selected from the View menu.
     * @param event ActionEvent from user selecting customer from the menu bar.
     * @throws IOException If the fxml path is not valid.
     */
    @FXML public void customerView(ActionEvent event) throws IOException {
        setContent(CUSTOMERS_FXML);
    }

    /**
     * Handles loading the schedule scene into content when Appointments is selected from the View menu.
     * @param event ActionEvent from user selecting customer from the menu bar.
     * @throws IOException If the fxml path is not valid.
     */
    @FXML public void appointmentView(ActionEvent event) throws IOException {
        setContent(SCHEDULE_FXML);
    }

    /**
     * Opens a new window with the report of appointments by month and type.
     * @param event ActionEvent from user selecting the report.
     * @throws IOException If fxml path is not valid.
     */
    @FXML public void typeMonthReport(ActionEvent event) throws IOException {
        Reports.report = Reports.ReportType.TYPE_AND_MONTH;
        popupScene(event, REPORT_FXML, "Appointments by Type and Month");
    }

    /**
     * Opens a new window with the report of each contact's schedule.
     * @param event ActionEvent from user selecting the report.
     * @throws IOException If fxml path is not valid.
     */
    @FXML public void contactSchedule(ActionEvent event) throws IOException {
        Reports.report = Reports.ReportType.CONTACT_SCHEDULES;
        popupScene(event, REPORT_FXML, "Contact Schedules");
    }

    /**
     * Opens a new window with the report of each customer's schedule.
     * @param event ActionEvent from user selecting the report.
     * @throws IOException If fxml path is not valid.
     */
    @FXML public void customerSchedule(ActionEvent event) throws IOException {
        Reports.report = Reports.ReportType.CUSTOMER_SCHEDULES;
        popupScene(event, REPORT_FXML, "Customer Schedules");
    }

    /**
     * Opens the CustomerForm in a new window, when the form is closed it reloads the scene in the content pane.
     * @param event ActionEvent from user selecting the report.
     * @throws IOException If fxml path is not valid.
     */
    @FXML public void addCustomer(ActionEvent event) throws IOException {
        popupScene(event, CUSTOMER_FORM_FXML, "Add Customer");
        contentController.initialize();
    }

    /**
     * Opens the AppointmentForm in a new window, when the form is closed it reloads the scene in the content pane.
     * @param event ActionEvent from user selecting the report.
     * @throws IOException If fxml path is not valid.
     */
    @FXML public void addAppointment(ActionEvent event) throws IOException {
        popupScene(event, APPOINTMENT_FORM_FXML, "Add Appointment");
        contentController.initialize();
    }

    /**
     * Calls the modify method of the controller for the scene in the content pane.
     * @param event ActionEvent that triggered the method, passed to the corresponding method in the controller.
     */
    @FXML public void modify(ActionEvent event) {
        try {
            ((Editable) contentController).modify(event);
            contentController.initialize();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls the delete method of the controller for the scene in the content pane.
     * @param event ActionEvent that triggered the method, passed to the corresponding method in the controller.
     */
    @FXML public void delete(ActionEvent event) {
        try {
            ((Editable)contentController).delete(event);
            contentController.initialize();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Exits the application.
     * @param event ActionEvent that triggered the method.
     */
    @FXML public void exit(ActionEvent event) {
        Platform.exit();
    }
}
