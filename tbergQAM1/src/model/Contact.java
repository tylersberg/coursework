package model;

/**
 * The Contact class defines objects that store information for a contact.
 */
public class Contact {

    private int id;
    private String name;
    private String email;

    /**
     * Constructor for Contact objects.
     * @param id Contact ID for the contact.
     * @param name The contact's name.
     * @param email Email for the contact.
     */
    public Contact(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * Produces a string to represent the contact.
     * @return String for the contact.
     */
    @Override public String toString() {
        return (String.valueOf(id) + "  " + name);
    }

    /**
     * Gets the contact id for the contact.
     * @return Contact ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the contact's name.
     * @return Contact's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the email of the contact.
     * @return Contact's email.
     */
    public String getEmail() {
        return email;
    }
}
