package data_access;

import javafx.collections.ObservableList;
import model.Appointment;
import model.Contact;
import model.Customer;

import java.sql.SQLException;
import java.time.ZonedDateTime;

/**
 * The DataAccessor interface define methods to be implemented by classes
 * that provide access to the persistent data layer of the application.
 */
public interface DataAccessor {

    /**
     * Accesses the list of all countries.
     * @return  Returns an ObservableList of all countries.
     */
    ObservableList<String> getCountries();

    /**
     * Accesses the list of divisions associated with a particular country.
     * @return  Returns an ObservableList of divisions.
     */
    ObservableList<String> getDivisions(String country);

    /**
     * Accesses the list of all appointments.
     * @return  Returns an ObservableList of all appointments.
     */
    ObservableList<Appointment> getAppointments() throws Exception;

    /**
     * Accesses the list of all contacts.
     * @return  Returns an ObservableList of all contacts.
     */
    ObservableList<Contact> getContacts();

    /**
     * Accesses the list of all customers.
     * @return  Returns an ObservableList of all customers.
     */
    ObservableList<Customer> getCustomers();

    /**
     * Creates a new Customer.
     * @param name Name of customer.
     * @param address Customer's address.
     * @param postCode Customer's postcode.
     * @param phone Customer's phone number.
     * @param division The division (ie state or province) the customer is located in.
     */
    void addCustomer(String name, String address, String postCode, String phone, String division);

    /**
     * Updates an existing Customer.
     * @param name Name of customer.
     * @param address Customer's address.
     * @param postCode Customer's postcode.
     * @param phone Customer's phone number.
     * @param division The division (ie state or province) the customer is located in.
     * @param id ID number of the customer to be updated.
     */
    void updateCustomer(String name, String address, String postCode, String phone, String division, int id);

    /**
     * Creates a new appointment.
     * @param title Title of the appointment.
     * @param description Description of the appointment.
     * @param location Location of the appointment.
     * @param type The type of appointment.
     * @param start The start time and date of the appointment.
     * @param end The end time and date of the appointment.
     * @param customerId Customer ID of the customer associated with this appointment.
     * @param contactId Contact ID of the contact for this appointment.
     * @param userId User ID associated with this appointment.
     */
    void addAppointment(String title, String description, String location, String type
            , ZonedDateTime start, ZonedDateTime end, int customerId, int contactId, int userId) throws Exception;


    /**
     * Updates an existing appointment.
     * @param title Title of the appointment.
     * @param description Description of the appointment.
     * @param location Location of the appointment.
     * @param type The type of appointment.
     * @param start The start time and date of the appointment.
     * @param end The end time and date of the appointment.
     * @param customerId Customer ID of the customer associated with this appointment.
     * @param contactId Contact ID of the contact for this appointment.
     * @param userId User ID associated with this appointment.
     * @param appointmentId Appointment ID of the appointment to update.
     */
    void updateAppointment(String title, String description, String location, String type
            , ZonedDateTime start, ZonedDateTime end, int customerId, int contactId, int userId, int appointmentId);

    /**
     * Deletes an appointment.
     * @param id appointment ID of the appointment to delete.
     */
    void deleteAppointment(int id);

    /**
     * Deletes a customer.
     * @param id customer ID of the customer to delete.
     */
    void deleteCustomer(int id);

    /**
     * Deletes all appointments associated with a customer.
     * @param id customer ID of the customer to delete appointments for.
     */
    void deleteCustomerAppointments(int id);

    /**
     * Gets the User ID for the active user.
     * @return User ID for active user.
     */
    int getUserID();
}
