package control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;

/**
 * The Schedule class is the controller for the schedule scene, which displays the appointment list.
 */
public class Schedule extends Controller implements Editable {

    /** Attributes for the All tableview. */
    @FXML private TableView<Appointment> allTable;
    @FXML private TableColumn<Appointment, Integer> appointmentIdCol;
    @FXML private TableColumn<Appointment, String> titleCol;
    @FXML private TableColumn<Appointment, String> descriptionCol;
    @FXML private TableColumn<Appointment, String> locationCol;
    @FXML private TableColumn<Appointment, String> contactCol;
    @FXML private TableColumn<Appointment, String> typeCol;
    @FXML private TableColumn<Appointment, ZonedDateTime> startCol;
    @FXML private TableColumn<Appointment, ZonedDateTime> endCol;
    @FXML private TableColumn<Appointment, Integer> customerIdCol;

    /** Attributes for the Week tableview. */
    @FXML private TableView<Appointment> weekTable;
    @FXML private TableColumn<Appointment, Integer> appointmentIdColWeek;
    @FXML private TableColumn<Appointment, String> titleColWeek;
    @FXML private TableColumn<Appointment, String> descriptionColWeek;
    @FXML private TableColumn<Appointment, String> locationColWeek;
    @FXML private TableColumn<Appointment, String> contactColWeek;
    @FXML private TableColumn<Appointment, String> typeColWeek;
    @FXML private TableColumn<Appointment, ZonedDateTime> startColWeek;
    @FXML private TableColumn<Appointment, ZonedDateTime> endColWeek;
    @FXML private TableColumn<Appointment, Integer> customerIdColWeek;

    /** Attributes for the Month tableview. */
    @FXML private TableView<Appointment> monthTable;
    @FXML private TableColumn<Appointment, Integer> appointmentIdColMonth;
    @FXML private TableColumn<Appointment, String> titleColMonth;
    @FXML private TableColumn<Appointment, String> descriptionColMonth;
    @FXML private TableColumn<Appointment, String> locationColMonth;
    @FXML private TableColumn<Appointment, String> contactColMonth;
    @FXML private TableColumn<Appointment, String> typeColMonth;
    @FXML private TableColumn<Appointment, ZonedDateTime> startColMonth;
    @FXML private TableColumn<Appointment, ZonedDateTime> endColMonth;
    @FXML private TableColumn<Appointment, Integer> customerIdColMonth;

    /**
     * The DateTimeCell class extends TableCell and overrides the updateItem method to set the text of the cell to a
     * specific format, while preserving the underlying ZonedDateTime.
     */
    private class DateTimeCell extends TableCell<Appointment, ZonedDateTime> {
        public DateTimeCell() {}
        @Override
        protected void updateItem(ZonedDateTime item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty ? null : formatTime(item));
        }
    }


    /**
     * Initializes the scene, it retrieves the appointment list and populates table views that display them.
     * It also checks for any upcoming appointments and notifies the user.
     * <b>EXPLANATION OF LAMBDA EXPRESSIONS:</b>
     * Lambda expressions are used to filter the appointment list for the weekTable and monthTable.
     * The appointment list is used as the source for a stream. A lambda expression is used as the predicate for the filter operation,
     * to remove appointments that are not in the required timeframe. There are also method references used to collect the appointments
     * into a new list, using Collectors.toCollection.
     * Lambda Expressions are also used to set a non default cellFactory for the cells holding ZonedDateTimes.
     */
    @FXML public void initialize() {
        try {
            ObservableList<Appointment> appointmentList = FXCollections.observableArrayList(dao.getAppointments());

            /** Initialization of the All tableview.*/
            allTable.setItems(appointmentList);
            appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
            contactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
            startCol.setCellFactory(c -> new DateTimeCell());
            endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
            endCol.setCellFactory(c -> new DateTimeCell());
            customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            allTable.getSortOrder().add(startCol);

            /** Initialization of the Week tableview.*/
            weekTable.setItems(appointmentList.stream()
                    .filter(a -> a.getStart().isAfter(ZonedDateTime.now()) && a.getStart().isBefore(ZonedDateTime.now().plusWeeks(1)))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList)));
            appointmentIdColWeek.setCellValueFactory(new PropertyValueFactory<>("id"));
            titleColWeek.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionColWeek.setCellValueFactory(new PropertyValueFactory<>("description"));
            locationColWeek.setCellValueFactory(new PropertyValueFactory<>("location"));
            contactColWeek.setCellValueFactory(new PropertyValueFactory<>("contactName"));
            typeColWeek.setCellValueFactory(new PropertyValueFactory<>("type"));
            startColWeek.setCellValueFactory(new PropertyValueFactory<>("start"));
            startColWeek.setCellFactory(c -> new DateTimeCell());
            endColWeek.setCellValueFactory(new PropertyValueFactory<>("end"));
            endColWeek.setCellFactory(c -> new DateTimeCell());
            customerIdColWeek.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            weekTable.getSortOrder().add(startCol);

            /** Initialization of the Month tableview.*/
            monthTable.setItems(appointmentList.stream()
                    .filter(a -> a.getStart().getYear() == ZonedDateTime.now().getYear())
                    .filter(a -> a.getStart().getMonth() == ZonedDateTime.now().getMonth())
                    .collect(Collectors.toCollection(FXCollections::observableArrayList)));
            appointmentIdColMonth.setCellValueFactory(new PropertyValueFactory<>("id"));
            titleColMonth.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionColMonth.setCellValueFactory(new PropertyValueFactory<>("description"));
            locationColMonth.setCellValueFactory(new PropertyValueFactory<>("location"));
            contactColMonth.setCellValueFactory(new PropertyValueFactory<>("contactName"));
            typeColMonth.setCellValueFactory(new PropertyValueFactory<>("type"));
            startColMonth.setCellValueFactory(new PropertyValueFactory<>("start"));
            startColMonth.setCellFactory(c -> new DateTimeCell());
            endColMonth.setCellValueFactory(new PropertyValueFactory<>("end"));
            endColMonth.setCellFactory(c -> new DateTimeCell());
            customerIdColMonth.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            monthTable.getSortOrder().add(startCol);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Passes the item selected in the active tableview to the AppointmentForm controller, and the loads the appointment form.
     * @param event ActionEvent that activated the method call.
     */
    public void modify(ActionEvent event) {
        try {
            if(weekTable.isFocused())
                AppointmentForm.appointment = weekTable.getSelectionModel().getSelectedItem();
            else if (monthTable.isFocused())
                AppointmentForm.appointment = monthTable.getSelectionModel().getSelectedItem();
            else
                AppointmentForm.appointment = allTable.getSelectionModel().getSelectedItem();
            popupScene(event, APPOINTMENT_FORM_FXML, "Modify Appointment");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the item selected in the active tableview.
     * @param event ActionEvent that activated the method call.
     */
    public void delete(ActionEvent event) {
        try {
            int id;
            String type;
            if(weekTable.isFocused()) {
                id = weekTable.getSelectionModel().getSelectedItem().getId();
                type = weekTable.getSelectionModel().getSelectedItem().getType();
            } else if (monthTable.isFocused()) {
                id = monthTable.getSelectionModel().getSelectedItem().getId();
                type = monthTable.getSelectionModel().getSelectedItem().getType();
            } else {
                id = allTable.getSelectionModel().getSelectedItem().getId();
                type = allTable.getSelectionModel().getSelectedItem().getType();
            }
            dao.deleteAppointment(id);
            messageBox("Appointment deleted.", ("Removed appointment #" + id + " of type: " + type));
        } catch(NullPointerException e) {
            error("No item selected, select an item to delete.");
            e.printStackTrace();
        }
    }
}
