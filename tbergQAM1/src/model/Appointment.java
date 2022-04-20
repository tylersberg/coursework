package model;

import java.time.ZonedDateTime;

/**
 * The Appointment class defines objects which hold data for an appointment.
 */
public class Appointment {
    
    private int id;
    private String title;
    private String description;
    private String location;
    private Contact contact;
    private Customer customer;
    private int userId;
    private String type;
    private ZonedDateTime start;
    private ZonedDateTime end;

    /**
     * Constructor for appointment objects.
     * @param id Unique ID for the appointment.
     * @param title Name for the appointment.
     * @param description A description of the appointment.
     * @param location Location for the appointment.
     * @param contact Contact associated with the appointment.
     * @param customer Customer associated with the appointment,
     * @param userId Unique user ID for the user associated with the appointment.
     * @param type Type of the appointment.
     * @param start Start time and date of the appointment.
     * @param end End time and date of the appointment.
     */
    public Appointment(int id, String title, String description, String location, Contact contact, Customer customer, int userId, String type, ZonedDateTime start, ZonedDateTime end) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.customer = customer;
        this.userId = userId;
        this.type = type;
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the ID of the appointment.
     * @return ID of the appointment.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the title of the appointment.
     * @return Title of the appointment.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the description of the appointment.
     * @return description of the appointment.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the location for the appointment.
     * @return Location for the appointment.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns the contact for the appointment.
     * @return Contact for the appointment.
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * Returns the customer associated with the appointment.
     * @return Customer for the appointment.
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Returns the user ID of the appointment.
     * @return User ID associated with the appointment.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Returns the type of the appointment.
     * @return Type of the appointment.
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the start time and date of the appointment.
     * @return The start time and date of the appointment.
     */
    public ZonedDateTime getStart() {
        return start;
    }

    /**
     * Gets the end time and date of the appointment.
     * @return The end time and date of the appointment.
     */
    public ZonedDateTime getEnd() {
        return end;
    }

    /**
     * Returns the ID of customer for the appointment.
     * @return ID of the appointment's associated customer.
     */
    public int getCustomerId() {
        return customer.getId();
    }

    /**
     * Gets the name of the contact associated with an appointment.
     * @return Name of contact for this appointment.
     */
    public String getContactName() {
        return contact.getName();
    }
}
