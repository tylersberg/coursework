package data_access;

import com.mysql.cj.jdbc.MysqlDataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Contact;
import model.Customer;

import java.sql.*;
import java.time.ZonedDateTime;

import static java.time.ZoneOffset.UTC;

/**
 * The SqlDB class implements the DataAccessor interface to provide access to a SQL database.
 */
public final class SqlDB implements DataAccessor {

    // Connection Properties
    static final private String PROTOCOL = "jdbc";
    static final private String TYPE = "mysql";
    static final private String URL = "wgudb.ucertify.com";
    static final private String SCHEMA = "WJ07PEZ";
    static final private String JDBC_URL = PROTOCOL + ":" + TYPE + "://" + URL;
    static final private String USER = "U07PEZ";
    static final private String PASSWORD = "53689091260";

    private Connection connection;
    private int userID;

    /**
     * Constructor for SqlDB objects, establishes a connection to be used in communication with the database.
     * @param user Username to be used for validation and to determine the UserID.
     * @param password Password to be used for validation.
     * @throws SQLException Thrown if the connection fails or the credentials provided are invalid.
     */
    public SqlDB(String user,String password) throws SQLException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl(JDBC_URL);
        dataSource.setUser(USER);
        dataSource.setPassword(PASSWORD);
        connection = dataSource.getConnection();
        PreparedStatement setSchema =connection.prepareStatement("Use " + SCHEMA);
        setSchema.executeUpdate();
        PreparedStatement validate = connection.prepareStatement("SELECT User_ID FROM users WHERE User_Name = ? AND Password = ?");
        validate.setString(1, user);
        validate.setString(2, password);
        ResultSet match = validate.executeQuery();
        if (match.next()) {
            userID = match.getInt("User_ID");
        } else {
            connection = null;
            throw new SQLException();
        }
    }

    /**
     * Queries the database for all countries.
     * @return  Returns an ObservableList of all countries.
     */
    @Override
    public ObservableList<String> getCountries() {
        ObservableList<String> countryList = FXCollections.observableArrayList();
        try {
            PreparedStatement selectCountries = connection.prepareStatement("SELECT Country FROM countries");
            ResultSet countries = selectCountries.executeQuery();
            while (countries.next()) {
                countryList.add(countries.getString("Country"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countryList;
    }

    /**
     * Queries the database for all divisions associated with a particular country.
     * @return  Returns an ObservableList of divisions.
     */
    @Override
    public ObservableList<String> getDivisions(String country) {
        ObservableList<String> divisionList = FXCollections.observableArrayList();
        try {
            PreparedStatement selectDivisions = connection.prepareStatement(
                    "SELECT Division FROM first_level_divisions INNER JOIN countries ON countries.Country_ID = first_level_divisions.COUNTRY_ID WHERE Country = ?");
            selectDivisions.setString(1, country);
            ResultSet divisions = selectDivisions.executeQuery();
            while (divisions.next()) {
                divisionList.add(divisions.getString("Division"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return divisionList;
    }

    /**
     * Queries the database for all appointments and their associated customer, and contact.
     * @return  Returns an ObservableList of all appointments.
     */
    @Override
    public ObservableList<Appointment> getAppointments() throws Exception {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        try {
            PreparedStatement selectAppointments = connection.prepareStatement(
                    "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, " +
                            "contacts.Contact_ID, Contact_Name, Email, User_ID, " +
                            "customers.Customer_ID, Customer_Name, Address, Postal_Code, Phone, " +
                            "countries.Country, first_level_divisions.Division FROM appointments " +
                            "INNER JOIN customers ON appointments.Customer_ID = customers.Customer_ID " +
                            "INNER JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID " +
                            "INNER JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID " +
                            "INNER JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID");
            ResultSet appointments = selectAppointments.executeQuery();
            Contact contact;
            Customer customer;
            while (appointments.next()) {
                contact = new Contact(
                        appointments.getInt("Contact_ID"),
                        appointments.getString("Contact_Name"),
                        appointments.getString("Email"));
                customer = new Customer(
                        appointments.getInt("Customer_ID"),
                        appointments.getString("Customer_Name"),
                        appointments.getString("Address"),
                        appointments.getString("Postal_Code"),
                        appointments.getString("Country"),
                        appointments.getString("Division"),
                        appointments.getString("Phone"));
                Appointment appointment = new Appointment(
                        appointments.getInt("Appointment_ID"),
                        appointments.getString("Title"),
                        appointments.getString("Description"),
                        appointments.getString("Location"),
                        contact,
                        customer,
                        appointments.getInt("User_ID"),
                        appointments.getString("Type"),
                        appointments.getTimestamp("Start").toInstant().atZone(UTC),
                        appointments.getTimestamp("End").toInstant().atZone(UTC));
                appointmentList.add(appointment);
            }
        } catch(SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            throw new Exception("Invalid ID number");
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return appointmentList;

    }

    /**
     * Queries the database for all contacts.
     * @return  Returns an ObservableList of all contacts.
     */
    @Override
    public ObservableList<Contact> getContacts() {
        ObservableList<Contact> contactList = FXCollections.observableArrayList();
        try {
            PreparedStatement selectContacts = connection.prepareStatement("SELECT Contact_ID, Contact_Name, Email FROM contacts");
            ResultSet contacts = selectContacts.executeQuery();
            while (contacts.next()) {
                contactList.add(new Contact(contacts.getInt("Contact_ID"), contacts.getString("Contact_Name"), contacts.getString("Email")));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return contactList;
    }

    /**
     * Queries the database for all customers.
     * @return  Returns an ObservableList of all customers.
     */
    @Override
    public ObservableList<Customer> getCustomers() {
        ObservableList<Customer> returnList = FXCollections.observableArrayList();
        try {
            PreparedStatement selectCustomers = connection.prepareStatement(
                    "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, countries.Country, first_level_divisions.Division " +
                            "FROM customers INNER JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID " +
                            "INNER JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID");
            ResultSet selection = selectCustomers.executeQuery();
            while (selection.next())
                returnList.add(new Customer(
                        selection.getInt("Customer_ID"),
                        selection.getString("Customer_Name"),
                        selection.getString("Address"),
                        selection.getString("Postal_Code"),
                        selection.getString("Country"),
                        selection.getString("Division"),
                        selection.getString("Phone")));
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return returnList;
    }

    /**
     * Inserts a new Customer record into the database.
     * @param name Name of customer.
     * @param address Customer's address.
     * @param postCode Customer's postcode.
     * @param phone Customer's phone number.
     * @param division The division (ie state or province) the customer is located in.
     */
    @Override
    public void addCustomer(String name, String address, String postCode, String phone, String division) {
        try {
            PreparedStatement insert = connection.prepareStatement("INSERT INTO customers " +
                    "(Customer_Name , Address, Postal_Code, Phone, Created_By, Division_ID) VALUES (?, ?, ?, ?, ?, ?)");
            insert.setString(1, name);
            insert.setString(2, address);
            insert.setString(3, postCode);
            insert.setString(4, phone);
            insert.setString(5, String.valueOf(userID));
            insert.setInt(6, getDivisionID(division));
            insert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing Customer record in the database.
     * @param name Name of customer.
     * @param address Customer's address.
     * @param postCode Customer's postcode.
     * @param phone Customer's phone number.
     * @param division The division (ie state or province) the customer is located in.
     * @param id ID number of the customer to be updated.
     */
    @Override
    public void updateCustomer(String name, String address, String postCode, String phone, String division, int id) {
        try {
            PreparedStatement update = connection.prepareStatement("UPDATE customers SET " +
                    "Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? " +
                    "WHERE Customer_ID = ?");
            update.setString(1, name);
            update.setString(2, address);
            update.setString(3, postCode);
            update.setString(4, phone);
            update.setInt(5, getDivisionID(division));
            update.setInt(6, id);
            update.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts an appointment record into the database.
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
    @Override
    public void addAppointment(String title, String description, String location, String type
            , ZonedDateTime start, ZonedDateTime end, int customerId, int contactId, int userId) throws Exception {
        try {
            PreparedStatement insert = connection.prepareStatement("INSERT INTO appointments " +
                    "(Title, Description, Location, Type, Start, End, User_ID, Customer_ID, Contact_ID) " +
                    "VALUES (?,?,?,?,?,?,?,?,?)");
            insert.setString(1, title);
            insert.setString(2, description);
            insert.setString(3, location);
            insert.setString(4, type);
            insert.setTimestamp(5, Timestamp.from(start.toInstant()));
            insert.setTimestamp(6, Timestamp.from(end.toInstant()));
            insert.setInt(7, userId);
            insert.setInt(8, customerId);
            insert.setInt(9, contactId);
            insert.executeUpdate();
        } catch(SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            throw new Exception("Invalid ID number");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Updates an existing appointment record in the database.
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
    @Override
    public void updateAppointment(String title, String description, String location, String type
            , ZonedDateTime start, ZonedDateTime end, int customerId, int contactId, int userId, int appointmentId) {
        try {
            PreparedStatement update = connection.prepareStatement("UPDATE appointments SET " +
                    "Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, User_ID = ?, Customer_ID = ?, Contact_ID = ? " +
                    "WHERE Appointment_ID = ?");
            update.setString(1, title);
            update.setString(2, description);
            update.setString(3, location);
            update.setString(4, type);
            update.setTimestamp(5, Timestamp.from(start.toInstant()));
            update.setTimestamp(6, Timestamp.from(end.toInstant()));
            update.setInt(7, userId);
            update.setInt(8, customerId);
            update.setInt(9, contactId);
            update.setInt(10, appointmentId);
            update.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes an appointment record from the database.
     * @param id appointment ID of the appointment to delete.
     */
    @Override
    public void deleteAppointment(int id) {
        try {
            PreparedStatement delete = connection.prepareStatement("DELETE FROM appointments WHERE Appointment_ID = ?");
            delete.setInt(1, id);
            delete.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a customer customer record from the database.
     * @param id customer ID of the customer to delete.
     */
    @Override
    public void deleteCustomer(int id) {
        try {
            PreparedStatement delete = connection.prepareStatement("DELETE FROM customers WHERE Customer_ID = ?");
            delete.setInt(1, id);
            delete.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes all appointments records  in the database with a specific customer id.
     * @param id customer ID of the customer to delete appointments for.
     */
    @Override
    public void deleteCustomerAppointments(int id) {
        try {
            PreparedStatement delete = connection.prepareStatement("DELETE FROM appointments WHERE Customer_ID = ?");
            delete.setInt(1, id);
            delete.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the User ID for the active user.
     * @return User ID for active user.
     */
    @Override
    public int getUserID() {
        return userID;
    }

    private int getDivisionID(String division) {
        int id = -1;
        try {
            PreparedStatement findID = connection.prepareStatement("SELECT Division_ID FROM first_level_divisions WHERE Division = ?");
            findID.setString(1, division);
            ResultSet foundID = findID.executeQuery();
            if (foundID.next())
                id = foundID.getInt("Division_ID");
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
}
