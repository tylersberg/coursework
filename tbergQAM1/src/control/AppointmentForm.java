package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Appointment;
import model.Contact;

import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.List;

import static java.time.ZoneOffset.UTC;

/**
 * The AppointmentForm extends Controller and controls the appointment form,
 * which allows for the creation and modification of appointments.
 */
public class AppointmentForm extends Controller {
    /** The static appointment member is used to load an existing appointment into the form.*/
    protected static Appointment appointment = null;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;

    @FXML private Label titleLabel;
    @FXML private TextField titleField;
    @FXML private TextField typeField;
    @FXML private TextField idField;
    @FXML private TextField locationField;
    @FXML private TextField descriptionField;
    @FXML private ComboBox<Contact> contactComboBox;
    @FXML private TextField customerId;
    @FXML private TextField userId;
    @FXML private TextField startField;
    @FXML private TextField endField;
    @FXML private DatePicker date;
    @FXML private Button okButton;

    /**
     * The initialize method is ran when the controller object is created, it executes the initial setup of the form,
     * and loads the data from the appointment if one was provided.
     */
    @FXML public void initialize() {
        try {
            contactComboBox.setItems(dao.getContacts());
            if (appointment != null) {
                titleLabel.setText("Modify Appointment");
                idField.setText(String.valueOf(appointment.getId()));
                titleField.setText(appointment.getTitle());
                typeField.setText(appointment.getType());
                locationField.setText(appointment.getLocation());
                descriptionField.setText(appointment.getDescription());
                startField.setText(LocalTime.from(appointment.getStart().withZoneSameInstant(ZoneId.systemDefault())).toString());
                endField.setText(LocalTime.from(appointment.getEnd().withZoneSameInstant(ZoneId.systemDefault())).toString());
                date.setValue(LocalDate.from(appointment.getStart()));
                contactComboBox.setValue(appointment.getContact());
                customerId.setText(String.valueOf(appointment.getCustomerId()));
                userId.setText(String.valueOf(appointment.getUserId()));
                okButton.setOnAction(this::update);
            } else {
                titleLabel.setText("Add Appointment");
                userId.setText(String.valueOf(dao.getUserID()));
                okButton.setOnAction(this::add);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            appointment = null;
        }
    }

    /**
     * The add method inserts a new appointment into the data layer, using then data in the appointment form fields.
     * @param event ActionEvent which triggered the handler.
     */
    public void add(ActionEvent event) {
        try {
            parseTimeInput();
            dao.addAppointment(
                    titleField.getText(),
                    descriptionField.getText(),
                    locationField.getText(),
                    typeField.getText(),
                    startTime,
                    endTime,
                    Integer.parseInt(customerId.getText()),
                    contactComboBox.getValue().getId(),
                    Integer.parseInt(userId.getText()));
            close(event);
        } catch(NumberFormatException e) {
            e.printStackTrace();
            error("Check ID numbers");
        } catch(DateTimeParseException e) {
            error("Could not read time value, enter time as hh:mm in 24 hour time.");
        } catch(Exception e) {
            error(e.getMessage());
        }
    }

    /**
     * The update method updates an appointment in the data layer, using then data in the appointment form fields.
     * @param event ActionEvent which triggered the handler.
     */
    public void update(ActionEvent event) {
        try {
            parseTimeInput();
            dao.updateAppointment(
                    titleField.getText(),
                    descriptionField.getText(),
                    locationField.getText(),
                    typeField.getText(),
                    startTime,
                    endTime,
                    Integer.parseInt(customerId.getText()),
                    contactComboBox.getValue().getId(),
                    Integer.parseInt(userId.getText()),
                    Integer.parseInt(idField.getText()));
            close(event);
        } catch(NumberFormatException e) {
            error("Check ID numbers");
        } catch(DateTimeParseException e) {
            error("Could not read time value, enter time as hh:mm in 24 hour time.");
        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    /**
     * The cancel method closes the appointment form without performing any data operations.
     * @param event ActionEvent used to access the current window.
     */
    public void cancel(ActionEvent event) {
       close(event);
    }

    /**
     * The parseTimeInput method converts the time and date input in the appointment into ZonedDateTime objects,
     * it also performs logical checks to insure the times are valid.
     * @throws DateTimeParseException
     * <b>EXPLANATION OF LAMBDA EXPRESSIONS:</b>
     * Lambda expressions are used in the code that checks if there are overlapping appointments for the entered customer.
     * A lambda expression is passed as the predicate to the filter operation to get only appointments for the entered customer.
     * Then a lambda expression is used as the predicate for the anyMatch operation to determine if there is any appointment
     * remaining that overlaps with the entered times.
     */
    private void parseTimeInput() throws Exception, DateTimeParseException {
        ZonedDateTime businessHoursStart = LocalDateTime.of(date.getValue(), LocalTime.parse("08:00")).atZone(ZoneId.of("America/New_York")).withZoneSameInstant(UTC);
        ZonedDateTime businessHoursEnd = LocalDateTime.of(date.getValue(), LocalTime.parse("22:00")).atZone(ZoneId.of("America/New_York")).withZoneSameInstant(UTC);
        startTime = LocalDateTime.of(date.getValue(), LocalTime.parse(startField.getText())).atZone(ZoneId.systemDefault()).withZoneSameInstant(UTC);
        endTime = LocalDateTime.of(date.getValue(), LocalTime.parse(endField.getText())).atZone(ZoneId.systemDefault()).withZoneSameInstant(UTC);
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("End time is before appointment start time");
        } else if (startTime.isBefore(businessHoursStart) | endTime.isAfter(businessHoursEnd)) {
            throw new IllegalArgumentException("Appointment time does not fall within business hours.");
        }
        List<Appointment> appointments = dao.getAppointments();
        if (!idField.getText().isEmpty())
            appointments.removeIf(a -> a.getId() == Integer.parseInt(idField.getText()));
        if (appointments.stream()
                .filter(a -> a.getCustomer().getId() == Integer.parseInt(customerId.getText()))
                .anyMatch(a -> a.getStart().isBefore(endTime) && a.getEnd().isAfter(startTime))) {
            throw new IllegalArgumentException("Appointment time overlaps with an existing appointment for that customer.");
        }
    }
}