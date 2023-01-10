package controller;

/**
 * AppointmentsController class
 */
/**
 * @author James Badke
 */

import com.company.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AppointmentsController implements Initializable {

    public RadioButton weekBtn;
    public RadioButton monthBtn;
    public ToggleGroup weekOrMonth;
    public Button backBtn;
    public TableView appTable;
    public TableColumn idColumn;
    public TableColumn titleColumn;
    public TableColumn startColumn;
    public TableColumn endColumn;
    public TableColumn typeColumn;
    public TextField idField;
    public TextField titleField;
    public TextField descripField;
    public ComboBox typeCBox;
    public ComboBox locationCBox;
    public ComboBox contactCBox;
    public ComboBox startCBox;
    public ComboBox endCBox;
    public ComboBox custCBox;
    public DatePicker dateBox;
    public Button delBtn;
    public Button updateBtn;
    public Button addBtn;
    public TableColumn contactColumn;
    public TableColumn customerColumn;
    public RadioButton allBtn;
    public TableColumn descripColumn;
    public TableColumn userColumn;
    public TableColumn locationColumn;
    public ComboBox userCBox;

    private ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    public DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public ZoneId localZoneID = ZoneId.systemDefault();
    public ZoneId utcZoneID = ZoneId.of("UTC");


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appTable.setItems(allAppointments);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("custID"));
        descripColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        fillCBoxes();
        try {
            fillCustBox();
            fillContactBox();
            fillUserBox();
            populateTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        autoGenID();
    }

    /**
     * goes back to main screen of program
     * @param actionEvent when back button is pressed
     * @throws IOException
     */
    public void onBack(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        stage.setTitle("Main Screen");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * adds values of appointment to appointment table
     * @throws SQLException
     */
    public void populateTable() throws SQLException {
        Statement statement = util.JDBC.connection.createStatement();
        String sqlSelect = "SELECT * FROM appointments ORDER BY Appointment_ID;";
        statement.execute(sqlSelect);
        ResultSet rs = statement.getResultSet();
        while(rs.next()){
            addToTable(rs);
        }
    }

    /**
     * fills combo boxes with associated values
     */
    public void fillCBoxes(){
        // add string values to each combo box

        typeCBox.getItems().addAll("Planning Session","De-Briefing");
        locationCBox.getItems().addAll("United States", "United Kingdom", "Canada");
        startCBox.getItems().addAll("00:00","00:15","00:30","00:45","01:00","01:15","01:30","01:45","02:00","02:15","02:30","02:45",
                "03:00","03:15","03:30","03:45","04:00","04:15","04:30","04:45","05:00","05:15","05:30","05:45","06:00","06:15","06:30","06:45",
                "07:00","07:15","07:30","07:45","08:00","08:15","08:30","08:45","09:00","09:15","09:30","09:45","10:00","10:15","10:30","10:45",
                "11:00","11:15","11:30","11:45","12:00","12:15","12:30","12:45","13:00","13:15","13:30","13:45",
                "14:00","14:15","14:30","14:45","15:00","15:15","15:30","15:45","16:00","16:15","16:30","16:45",
                "17:00","17:15","17:30","17:45","18:00","18:15","18:30","18:45","19:00","19:15","19:30","19:45",
                "20:00","20:15","20:30","20:45","21:00","21:15","21:30","21:45","22:00","22:15","22:30","22:45",
                "23:00","23:15","23:30","23:45");
        endCBox.getItems().addAll("00:00","00:15","00:30","00:45","01:00","01:15","01:30","01:45","02:00","02:15","02:30","02:45",
                "03:00","03:15","03:30","03:45","04:00","04:15","04:30","04:45","05:00","05:15","05:30","05:45","06:00","06:15","06:30","06:45",
                "07:00","07:15","07:30","07:45","08:00","08:15","08:30","08:45","09:00","09:15","09:30","09:45","10:00","10:15","10:30","10:45",
                "11:00","11:15","11:30","11:45","12:00","12:15","12:30","12:45","13:00","13:15","13:30","13:45",
                "14:00","14:15","14:30","14:45","15:00","15:15","15:30","15:45","16:00","16:15","16:30","16:45",
                "17:00","17:15","17:30","17:45","18:00","18:15","18:30","18:45","19:00","19:15","19:30","19:45",
                "20:00","20:15","20:30","20:45","21:00","21:15","21:30","21:45","22:00","22:15","22:30","22:45",
                "23:00","23:15","23:30","23:45");
    }

    /**
     * deletes selected appointment from list and from sql database
     * @param actionEvent delete button is pressed
     * @throws SQLException
     */
    public void onDelete(ActionEvent actionEvent) throws SQLException, IOException {
        Appointment selectedApp = (Appointment) appTable.getSelectionModel().getSelectedItem();

        if(selectedApp == null){
            noneSelectedError();
        }else{

        boolean confirm = confirmDelete(selectedApp.getTitle());
        if (!confirm) {
            return;
        }
        System.out.println("You deleted Appointment " + selectedApp.getId() + ": " + selectedApp.getTitle());
        Statement statement = util.JDBC.connection.createStatement();
        String sqlDelete = "DELETE FROM appointments WHERE Appointment_ID = "+ selectedApp.getId() +";";
        statement.execute(sqlDelete);
        allAppointments.remove(selectedApp);
        appTable.setItems(allAppointments);
        appTable.refresh();
        autoGenID();

        refreshScene(delBtn);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notice");
        alert.setHeaderText("Appointment has been deleted");
        alert.setContentText("You deleted Appointment " + selectedApp.getId() + ": " + selectedApp.getType());
        alert.showAndWait();

    }
    }

    /**
     * updates selected appointment from list and sql database with current values
     * @param actionEvent update button pressed
     * @throws SQLException
     * @throws IOException
     * @throws ParseException
     */
    public void onUpdate(ActionEvent actionEvent) throws SQLException, IOException, ParseException {

        Appointment selectedApp = (Appointment) appTable.getSelectionModel().getSelectedItem();

        if(selectedApp == null){
            noneSelectedError();
        }else{
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date currentDate = new Date();
        int id = selectedApp.getId();
        String title = titleField.getText();
        String description = descripField.getText();
        String location = (String) locationCBox.getValue();
        String type = (String) typeCBox.getValue();
        LocalDate startDate = dateBox.getValue();
        LocalDate endDate = startDate;
        String startTime = (String) startCBox.getValue();
        String endTime = (String) endCBox.getValue();
        String lastUpdate = dateFormat.format(currentDate);
        String lastUpdatedBy = Main.currentUserName;
        String custId = (String) custCBox.getValue();
        String userId = (String) userCBox.getValue();
        String contactId = (String) contactCBox.getValue();

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Date compareStartTime = timeFormat.parse(startTime);
        Date compareEndTime = timeFormat.parse(endTime);

        if(compareStartTime.after(compareEndTime)){
            endDate = startDate.plusDays(1);
        }

        boolean overlap = false;
        boolean outsideHours = false;
        Statement statement = util.JDBC.connection.createStatement();
        String sqlSelect = "SELECT Start, End FROM appointments WHERE Customer_ID = " + custId + " AND Appointment_ID != " + id + ";";
        statement.execute(sqlSelect);
        ResultSet rs = statement.getResultSet();
        while (rs.next()) {
            Timestamp enteredStart = Timestamp.valueOf(startDate.toString() + " " + startTime + ":00");
            Timestamp enteredEnd = Timestamp.valueOf(endDate.toString() + " " + endTime + ":00");
            Timestamp selectStart = rs.getTimestamp("Start");
            Timestamp selectEnd = rs.getTimestamp("End");
            if (((enteredStart.compareTo(selectEnd) <= 0) && (enteredStart.compareTo(selectStart) >= 0))
                    || ((enteredEnd.compareTo(selectEnd) <= 0) && (enteredEnd.compareTo(selectStart) >= 0))) {
                    overlap = true;
                }
            if (((compareStartTime.getTime()/1000 < 46800) || (compareStartTime.getTime()/1000 > 97200))
                    || ((compareEndTime.getTime()/1000 < 46800) || (compareEndTime.getTime()/1000 > 97200))) {
                    outsideHours = true;
                }
            }

        if (overlap) {
            overlappingDateError();
        }
        if (outsideHours) {
            outsideHoursError();
        }

        if ((!overlap) && (!outsideHours)) {
            boolean confirm = confirmUpdate(selectedApp.getTitle());
            if (!confirm) {
                return;
            }

            // create LDT of start and end dates
            LocalDateTime startDateTime = LocalDateTime.parse(startDate.toString() + " " + startTime + ":00", datetimeDTF);
            LocalDateTime endDateTime = LocalDateTime.parse(endDate.toString() + " " + endTime + ":00", datetimeDTF);

            // create offset of default time zone
            String offset = ZoneId.systemDefault().getRules().getOffset(Instant.now()).toString();
            ZoneOffset zoneOffset = ZoneOffset.of(offset);

            // get date with offset
            OffsetDateTime offsetStartDateTime = OffsetDateTime.of(startDateTime, zoneOffset);
            OffsetDateTime offsetEndDateTime = OffsetDateTime.of(endDateTime, zoneOffset);

            // create ZDT with applied offset
            ZonedDateTime zonedStart = offsetStartDateTime.atZoneSameInstant(ZoneOffset.UTC);
            ZonedDateTime zonedEnd = offsetEndDateTime.atZoneSameInstant(ZoneOffset.UTC);

            // create date Strings to insert
            String insertStart = zonedStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm"));
            String insertEnd = zonedEnd.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm"));

            String sqlInsert = "UPDATE appointments SET Title = '" + title + "', Description = '" + description + "', Location = '" + location + "', Type = '" + type + "', Start = '" + insertStart + "', End = '" + insertEnd + "', Last_Update = '" + lastUpdate + "', Last_Updated_By = '" + lastUpdatedBy + "', Customer_ID = '" + custId + "', User_ID = '" + userId + "', Contact_ID = '" + contactId + "' WHERE Appointment_ID = " + selectedApp.getId() + ";";
            statement.execute(sqlInsert);
            refreshScene(addBtn);
            autoGenID();
            }
        }
    }

    /**
     * adds new appointment to list and sql database with current values
     * @param actionEvent add button pressed
     * @throws SQLException
     * @throws IOException
     * @throws ParseException
     */
    public void onAdd(ActionEvent actionEvent) throws SQLException, IOException, ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date currentDate = new Date();
        String id = idField.getText();
        String title = titleField.getText();
        String description = descripField.getText();
        String location = (String) locationCBox.getValue();
        String type = (String) typeCBox.getValue();
        LocalDate startDate = dateBox.getValue();
        LocalDate endDate = startDate;
        String startTime = (String) startCBox.getValue();
        String endTime = (String) endCBox.getValue();
        String createDate = dateFormat.format(currentDate);
        String createdBy = Main.currentUserName;
        String lastUpdate = dateFormat.format(currentDate);
        String lastUpdatedBy = Main.currentUserName;
        String custId = (String) custCBox.getValue();
        String contactId = (String) contactCBox.getValue();
        String userId;

        if (userCBox.getValue() != null){
            userId = (String) userCBox.getValue();
        }else{
            userId = Main.currentUserID;
        }

        if((custId == null) || (contactId == null)){
            emptyValuesError();
        }else{

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Date compareStartTime = timeFormat.parse(startTime);
        Date compareEndTime = timeFormat.parse(endTime);

        if(compareStartTime.after(compareEndTime)){
            endDate = startDate.plusDays(1);
        }

        boolean overlap = false;
        boolean outsideHours = false;

        Statement statement = util.JDBC.connection.createStatement();
        String sqlSelect = "SELECT Start, End FROM appointments WHERE Customer_ID = " + custId + ";";
        statement.execute(sqlSelect);
        ResultSet rs = statement.getResultSet();

        while(rs.next()){
            Timestamp enteredStart = Timestamp.valueOf(startDate.toString() + " " + startTime + ":00");
            Timestamp enteredEnd = Timestamp.valueOf(endDate.toString() + " " + endTime + ":00");
            Timestamp selectStart = rs.getTimestamp("Start");
            Timestamp selectEnd = rs.getTimestamp("End");

            if (((enteredStart.compareTo(selectEnd) <= 0) && (enteredStart.compareTo(selectStart) >= 0))
                || ((enteredEnd.compareTo(selectEnd) <= 0) && (enteredEnd.compareTo(selectStart) >= 0))){
                overlap = true;
            }
            if(((compareStartTime.getTime()/1000 < 46800) || (compareStartTime.getTime()/1000 > 97200))
            || ((compareEndTime.getTime()/1000 < 46800) || (compareEndTime.getTime()/1000 > 97200))){
                outsideHours = true;
            }
        }

        if (overlap){
            overlappingDateError();
        }
        if (outsideHours){
            outsideHoursError();
        }
        if ((!overlap) && (!outsideHours)){

            // create LDT of start and end dates
            LocalDateTime startDateTime = LocalDateTime.parse(startDate.toString() + " " + startTime + ":00", datetimeDTF);
            LocalDateTime endDateTime = LocalDateTime.parse(endDate.toString() + " " + endTime + ":00", datetimeDTF);

            // create offset of default time zone
            String offset = ZoneId.systemDefault().getRules().getOffset(Instant.now()).toString();
            ZoneOffset zoneOffset = ZoneOffset.of(offset);

            System.out.println("offset: " + offset);

            // get date with offset
            OffsetDateTime offsetStartDateTime = OffsetDateTime.of(startDateTime, zoneOffset);
            OffsetDateTime offsetEndDateTime = OffsetDateTime.of(endDateTime, zoneOffset);

            // create ZDT with applied offset
            ZonedDateTime zonedStart = offsetStartDateTime.atZoneSameInstant(ZoneOffset.UTC);
            ZonedDateTime zonedEnd = offsetEndDateTime.atZoneSameInstant(ZoneOffset.UTC);

            // create date Strings to insert
            String insertStart = zonedStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm"));
            String insertEnd = zonedEnd.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm"));

            String sqlInsert = "INSERT INTO appointments VALUES('" + id + "', '" + title + "', '" + description + "', '" + location + "', '" + type + "','" + insertStart + "','" + insertEnd + "','" + createDate + "','" + createdBy + "','" + lastUpdate + "','" + lastUpdatedBy + "','" + custId + "','" + userId + "','" + contactId + "');";
            statement.execute(sqlInsert);
            refreshScene(addBtn);
            autoGenID();
        }

        }
    }

    /**
     * automatically generates value of ID text field based on current number of appointments and disables text field
     */
    public void autoGenID(){
        int newAppID = allAppointments.size() + 1;
        idField.setText(String.valueOf(newAppID));
        idField.setEditable(false);
        idField.setDisable(true);
    }

    /**
     * fills customer combo box with associated values retrieved from sql database
     * @throws SQLException
     */
    public void fillCustBox() throws SQLException {
        Statement statement = util.JDBC.connection.createStatement();
        String sqlSelect = "SELECT * FROM customers ORDER BY Customer_ID;";
        statement.execute(sqlSelect);
        ResultSet rs = statement.getResultSet();

        while(rs.next()){
            custCBox.getItems().add(rs.getString("Customer_ID"));
        }
    }

    /**
     * fills contact combo box with associated values retrieved from sql database
     * @throws SQLException
     */
    public void fillContactBox() throws SQLException {
        Statement statement = util.JDBC.connection.createStatement();
        String sqlSelect = "SELECT * FROM contacts ORDER BY Contact_ID;";
        statement.execute(sqlSelect);
        ResultSet rs = statement.getResultSet();

        while (rs.next()) {
            contactCBox.getItems().add(rs.getString("Contact_ID"));
        }
    }

    /**
     * fills user combo box with associated values retrieved from sql database
     * @throws SQLException
     */
    public void fillUserBox() throws SQLException{
        Statement statement = util.JDBC.connection.createStatement();
        String sqlSelect = "SELECT * FROM users ORDER BY User_ID;";
        statement.execute(sqlSelect);
        ResultSet rs = statement.getResultSet();

        while (rs.next()) {
            userCBox.getItems().add(rs.getString("User_ID"));
        }
    }

    /**
     * prompts user for confirmation of delete action
     * @param name name of item selected for deletion
     * @return confirm or deny action
     */
    private boolean confirmDelete(String name) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Are you sure you want to delete " + name + "?");
        alert.setContentText("Click ok to confirm deletion.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * prompts user for confirmation of update action
     * @param name name of item selected for update
     * @return confirm or deny action
     */
    private boolean confirmUpdate(String name) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Update");
        alert.setHeaderText("Are you sure you want to overwrite " + name + "?");
        alert.setContentText("Click ok to confirm overwrite.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * runs filterByWeek() on button press
     * @param actionEvent radio button toggle
     * @throws SQLException
     */
    public void onWeek(ActionEvent actionEvent) throws SQLException {
        filterByWeek();
    }

    /**
     * runs filterByMonth on button press
     * @param actionEvent radio button toggle
     * @throws SQLException
     * @throws IOException
     */
    public void onMonth(ActionEvent actionEvent) throws SQLException, IOException {
        filterByMonth();
    }

    /**
     * shows all current appointments in table view
     * @param actionEvent radio button toggle
     * @throws IOException
     * @throws SQLException
     */
    public void onAll(ActionEvent actionEvent) throws IOException, SQLException {
        allAppointments.clear();
        populateTable();
        appTable.setItems(allAppointments);
    }

    /**
     * reloads current screen and by extension table view and appointment list
     * @param btn button to get scene
     * @throws IOException
     */
    public void refreshScene(Button btn) throws IOException {
        Stage stage = (Stage)btn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/view/Appointments.fxml"));
        stage.setTitle("Appointments");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * filters appointment list by current month
     * @throws SQLException
     * @throws IOException
     */
    public void filterByMonth() throws SQLException, IOException {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        System.out.println("Detected month:" + month);

        allAppointments.clear();
        Statement statement = util.JDBC.connection.createStatement();
        String sqlSelect = "SELECT * FROM appointments WHERE EXTRACT(Month FROM Start) = "+ month +";";
        statement.execute(sqlSelect);
        ResultSet rs = statement.getResultSet();

        while(rs.next()){
            addToTable(rs);
            }
        appTable.setItems(allAppointments);
        appTable.refresh();
    }

    /**
     * filters appointment list by current week
     * @throws SQLException
     */
    public void filterByWeek() throws SQLException {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week = cal.get(Calendar.WEEK_OF_YEAR) - 1;
        System.out.println("Detected week:" + week);

        allAppointments.clear();
        Statement statement = util.JDBC.connection.createStatement();
        String sqlSelect = "SELECT * FROM appointments WHERE EXTRACT(Week FROM Start) = "+ week +";";
        statement.execute(sqlSelect);
        ResultSet rs = statement.getResultSet();

        while(rs.next()){
            addToTable(rs);
        }
        appTable.setItems(allAppointments);
        appTable.refresh();
    }

    /**
     * automatically fills text fields and combo boxes based on selected appointment for updating
     * @param mouseEvent
     * @throws SQLException
     */
    public void onClicked(MouseEvent mouseEvent) throws SQLException {
        if(appTable.getSelectionModel().getSelectedItem() == null){
            System.out.println("No item selected");
        } else {
            Appointment selectedApp = (Appointment) appTable.getSelectionModel().getSelectedItem();

            Statement statement = util.JDBC.connection.createStatement();
            String sqlSelect = "SELECT DATE_FORMAT(Start, '%Y-%c-%d'), DATE_FORMAT(Start, '%k:%i'), DATE_FORMAT(End, '%k:%i') FROM appointments WHERE Appointment_ID = " + selectedApp.getId() + ";";
            statement.execute(sqlSelect);
            ResultSet rs = statement.getResultSet();

            titleField.setText(selectedApp.getTitle());
            descripField.setText(selectedApp.getDescription());
            locationCBox.setValue(selectedApp.getLocation());
            typeCBox.setValue(selectedApp.getType());
            contactCBox.setValue(selectedApp.getContactID());
            custCBox.setValue(selectedApp.getCustID());
            userCBox.setValue(selectedApp.getUserID());

            if (rs.next()) {
                // create dates to add to date and combo boxes
                LocalDateTime startDateTime = LocalDateTime.parse(selectedApp.getStart(), datetimeDTF);
                LocalDateTime endDateTime = LocalDateTime.parse(selectedApp.getEnd(), datetimeDTF);

                dateBox.setValue(LocalDate.from(startDateTime));
                startCBox.setValue(LocalTime.from(startDateTime).toString());
                endCBox.setValue(LocalTime.from(endDateTime).toString());
            }
        }
    }

    /**
     * displays error for overlapping dates
     */
    public void overlappingDateError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Date Overlap Conflict");
        alert.setContentText("Please select times that do not overlap with other appointments.");
        alert.showAndWait();
    }

    /**
     * displays error for outside business hours
     */
    public void outsideHoursError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid Appointment Hours");
        alert.setContentText("Please select times within 8:00am and 10:00pm.");
        alert.showAndWait();
    }

    /**
     * display error for no item selected
     */
    public void noneSelectedError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No appointment selected");
        alert.setContentText("Please select an appointment.");
        alert.showAndWait();
    }

    /**
     * display error for missing required values
     */
    public void emptyValuesError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Required Fields are Blank");
        alert.setContentText("Please fill all required fields.");
        alert.showAndWait();
    }

    /**
     * Adds appointment with selected values to appointment list
     * @param rs resultset of sql select
     * @throws SQLException
     */
    public void addToTable(ResultSet rs) throws SQLException {

        Appointment selectApp = new Appointment();
        selectApp.setID(rs.getInt("Appointment_ID"));
        selectApp.setTitle(rs.getString("Title"));
        selectApp.setType(rs.getString("Type"));
        selectApp.setCustID(rs.getString("Customer_ID"));
        selectApp.setContactID(rs.getString("Contact_ID"));
        selectApp.setDescription(rs.getString("Description"));
        selectApp.setUserID(rs.getString("User_ID"));
        selectApp.setLocation(rs.getString("Location"));

        //get database start and end time stored as UTC
        String startUTC = rs.getString("start").substring(0, 19);
        String endUTC = rs.getString("end").substring(0, 19);

        //convert database UTC to LocalDateTime
        LocalDateTime utcStartDT = LocalDateTime.parse(startUTC, datetimeDTF);
        LocalDateTime utcEndDT = LocalDateTime.parse(endUTC, datetimeDTF);

        //convert times UTC zoneId to local zoneId
        ZonedDateTime localZoneStart = utcStartDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);
        ZonedDateTime localZoneEnd = utcEndDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);

        //convert ZonedDateTime to a string for insertion into AppointmentsTableView
        String localStartDT = localZoneStart.format(datetimeDTF);
        String localEndDT = localZoneEnd.format(datetimeDTF);

        selectApp.setStart(localStartDT);
        selectApp.setEnd(localEndDT);
        allAppointments.add(selectApp);
    }
}