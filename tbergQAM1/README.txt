*** TITLE ***
tbergQAM1: 
    This application provides a graphical interface for manageing appointment,
    and customer data. It handles communication with a SQL database, for persistent
    storage of the data.

*** ABOUT ***
Author: Tyler S Berg    tberg17@my.wgu.edu
Version: 1.00-16 Jan 2021
IDE: IntelliJ Community 2020.3
Java Version 14.02 Java FX 14.01

*** Instructions for Use ***

Logging in:
    On the initial screen you may enter a user name and password into the corresponding text fields,
    and then either click 'login' or hit the enter key.
Viewing Appointments/Customer tables:
    After login the Appointments table will be displayed, to navigate between the customer and appointment 
    tables select 'View' from the menu bar at the top of the screen and select either Appointments or Customers.
Adding new Appointments/Customers:
    To add new data select 'File' from the menubar and then select the type of data to be added. This will
    open a new window where the new data can be entered. The User Id will default to the logged in user.
    Times should be entered in 24 hour time, without AM/PM.
Modifying data:
    To modify either a customer or an appointment: select the item in the table, select 'Edit' from the menubar and click 'Modify'.
    This will open a window with the data of the item prepopulated into the fields.
Deleting data:
    To delete an item: select it in the table, select 'Edit' from the menubar and click 'Delete'.
Reports:
    To access the reports, first select 'View', then mouse over 'Reports' in the dropdown menu, and select the desired report type
    to the right.

*** Description of additional Report ***
The third report type allows the user to view a summary of all of the appointments scheduled for each individual
customer.
