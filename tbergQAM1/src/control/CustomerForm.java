package control;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import model.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.sql.SQLException;

/**
 * The CustomerForm extends Controller and controls the customer form,
 * which allows for the creation and modification of customers.
 */
public class CustomerForm extends Controller{
    /** The static customer member is used to load an existing customer into the form. */
    protected static Customer customer = null;
    @FXML private TextField idField;
    @FXML private Label titleLabel;
    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private TextField postCodeField;
    @FXML private TextField phoneField;
    @FXML private ComboBox<String> countryBox;
    @FXML private ComboBox<String> divisionBox;
    @FXML private Button okButton;
    @FXML private Button cancelButton;

    /**
     * The initialize method is ran when the controller object is created, it executes the initial setup of the form,
     * and loads the data from the customer if one was provided.
     */
    @FXML public void initialize() {
        try {
            countryBox.setItems(dao.getCountries());
            if (customer != null) {
                titleLabel.setText("Modify Customer");
                idField.setText(String.valueOf(customer.getId()));
                nameField.setText(customer.getName());
                addressField.setText(customer.getAddress());
                postCodeField.setText(customer.getPostCode());
                phoneField.setText(customer.getPhone());
                countryBox.getSelectionModel().select(customer.getCountry());
                setDivisions();
                divisionBox.getSelectionModel().select(customer.getDivision());
                okButton.setOnAction(this::update);
            } else {
                titleLabel.setText("Add Customer");
                divisionBox.setPromptText("First select Country");
                okButton.setOnAction(this::add);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            customer = null;
        }
    }

    /**
     * The add method inserts a new customer into the data layer, using then data in the customer form fields.
     * @param event ActionEvent which triggered the handler.
     */
    public void add(ActionEvent event) {
        try {
            dao.addCustomer(
                    nameField.getText(),
                    addressField.getText(),
                    postCodeField.getText(),
                    phoneField.getText(),
                    divisionBox.getSelectionModel().getSelectedItem());
            close(event);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The update method updates a customer in the data layer, using then data in the customer form fields.
     * @param event ActionEvent which triggered the handler.
     */
    public void update(ActionEvent event) {
        try {
            dao.updateCustomer(
                    nameField.getText(),
                    addressField.getText(),
                    postCodeField.getText(),
                    phoneField.getText(),
                    divisionBox.getSelectionModel().getSelectedItem(),
                    Integer.parseInt(idField.getText()));
            close(event);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  Populates the division box with the division data corresponding to the selected country.
     */
    public void setDivisions() {
            divisionBox.setPromptText("");
            divisionBox.setItems(dao.getDivisions(countryBox.getSelectionModel().getSelectedItem()));
    }

    /**
     * The cancel method closes the customer form without performing any data operations.
     * @param event ActionEvent used to access the current window.
     */
    public void cancel(ActionEvent event) {
        close(event);
    }
}
