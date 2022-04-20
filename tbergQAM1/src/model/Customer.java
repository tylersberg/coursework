package model;

/**
 * The Customer class defines objects that represent customers.
 */
public class Customer {

    private int id;
    private String name;
    private String address;
    private String postCode;
    private String country;
    private String division;
    private String phone;

    /** Constructor for customer objects.
     * @param id Customer ID.
     * @param name Customer's name.
     * @param address Customer's address.
     * @param postCode Customer's postal code.
     * @param country The country of the customer.
     * @param division The subdivision of the country (ie state or province) of the customer.
     * @param phone Customer's phone number.
     */
    public Customer(int id, String name, String address, String postCode, String country, String division, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postCode = postCode;
        this.country = country;
        this.division = division;
        this.phone = phone;
    }

    /**
     * gets a string representation of the customer.
     * @return String representing the customer.
     */
    @Override public String toString() {
        return (String.valueOf(id) + " " + name);
    }

    /**
     * Gets the customer id.
     * @return Customer id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the customer.
     * @return Customer's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the address of the customer.
     * @return Customer's address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the customer's postal code.
     * @return Customer's postal code.
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * Gets the customer's country.
     * @return The customer's country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Gets the division the customer is from.
     * @return Customer's division (ie state or province).
     */
    public String getDivision() {
        return division;
    }

    /**
     * Gets the customer's phone number.
     * @return Customer's phone number.
     */
    public String getPhone() {
        return phone;
    }

}
