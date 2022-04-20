package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * The Customer class controls the customer scene, which holds a tableview of all customers.
 */
public class Customers extends Controller implements Editable {
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Integer> idCol;
    @FXML private TableColumn<Customer, String> nameCol;
    @FXML private TableColumn<Customer, String> addressCol;
    @FXML private TableColumn<Customer, String> postCodeCol;
    @FXML private TableColumn<Customer, String> countryCol;
    @FXML private TableColumn<Customer, String> divisionCol;

    /**
     * The initialize method loads the tableview with data.
     */
    @FXML public void initialize() {
            customerTable.setItems(dao.getCustomers());
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
            postCodeCol.setCellValueFactory(new PropertyValueFactory<>("postCode"));
            countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
            divisionCol.setCellValueFactory(new PropertyValueFactory<>("division"));
    }

    /**
     * Sets the customer selected in the customer table to the customer member of the CustomerForm, and the loads the
     * customer form in a new window.
     * @param event ActionEvent that triggered the method.
     */
    public void modify(ActionEvent event) {
        try {
            CustomerForm.customer = customerTable.getSelectionModel().getSelectedItem();
            popupScene(event, CUSTOMER_FORM_FXML, "Modify Customer");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the selected customer from the persistent data layer. First deletes any appointments that customer has.
     * @param event ActionEvent that triggered the method.
     */
    public void delete(ActionEvent event) {
        try {
            int toDelete = customerTable.getSelectionModel().getSelectedItem().getId();
            if (confirm("Delete selected customer? This will delete all of the customer's appointments as well.")) {
                dao.deleteCustomerAppointments(toDelete);
                dao.deleteCustomer(toDelete);
                messageBox("Customer removed.");
            }
        } catch(NullPointerException e) {
            error("No item selected, select an item to delete.");
            e.printStackTrace();
        }
    }
}
