C195 Database Client Scheduler Application

index.html located in Software2DBClientApp/src/Javadoc

The purpose of this application is to be able to view, create, update, and delete customer and appointment data within
an sql database. The application can also schedule appointments and alert the user when appointments are about to begin.

Author: James Badke
Contact Email: jbadke@wgu.edu
Application Version: 1.2
Date: 2/14/2022
IDE: IntelliJ IDEA 2021.2.3 (Community Edition)
JDK: 11.0.13
JavaFX SDK: 17.0.1


Directions Below:

LOGIN SCREEN------------------------------------------------------------------------------------------------------------

Enter username and password into corresponding boxes and click "login" to login


MAIN SCREEN-------------------------------------------------------------------------------------------------------------

Click on any of the 3 upper buttons to progress to the corresponding screen,
or click the bottom "Exit" button to exit the program.


CUSTOMERS SCREEN--------------------------------------------------------------------------------------------------------

add button:
Adds new customer to table view and inserts customer into sql database.
Uses values of text fields and combo boxes for customer values.

update button:
Replaces values of selected customer in table view and in sql database
with values retrieved from text fields and combo boxes. Values will automatically set
to values of selected customer upon clicking on them in the table view. Confirmation required.

delete button:
Deletes customer currently selected in table view from list and sql database. Confirmation required.

back button:
Returns to main screen.


APPOINTMENTS SCREEN-----------------------------------------------------------------------------------------------------

add button:
Adds new appointment to table view and inserts appointment into sql database.
Uses values of text fields, combo boxes, and datepicker for appointment values. If the user ID field is left blank,
the user ID of the currently logged in user will be added instead. Contact ID and Customer ID can not be left blank.

update button:
Replaces values of selected appointment in table view and in sql database
with values retrieved from text fields and combo boxes. Values will automatically set
to values of selected appointment upon clicking on them in the table view. Confirmation required.

delete button:
Deletes appointment currently selected in table view from list and sql database. Confirmation required.

back button:
Returns to main screen.


REPORTS SCREEN----------------------------------------------------------------------------------------------------------

Click on any of the three tabs to view the corresponding report.

back button:
Returns to main screen.
------------------------------------------------------------------------------------------------------------------------


Description of Additional Report:
The additional report lists the total number of customers by each of the three possible
countries (United States, United Kingdom, or Canada).


MySQL Connector Driver Version: mysql-connector-java-8.0.271