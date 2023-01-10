package controller;

/**
 * LoginController class
 */
/**
 * @author James Badke
 */

import com.company.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import util.JDBC;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.*;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;

//make sure appointment alerts still function correctly
public class LoginController implements Initializable {

    public TextField usernameText;
    public TextField passwordText;
    public Button loginBtn;
    public Label userLocationLabel;
    public Button exitBtn;
    public Label userLabel;
    public Label passLabel;
    public Label locationLabel;
    public Label titleLabel;

    public ZoneId localZoneId = ZoneId.systemDefault();
    public String currentZoneId = localZoneId.toString();
    public Locale currentLanguage = Locale.getDefault();
    public ResourceBundle rb = ResourceBundle.getBundle("language_files/rb");

    private final DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final ZoneId localZoneID = ZoneId.systemDefault();
    private final ZoneId utcZoneID = ZoneId.of("UTC");

    @Override

    /**
     * runs immediately upon screen load
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println("Language:" + currentLanguage);
        System.out.println("ZoneID: " + localZoneId);

        usernameText.setText("admin");
        passwordText.setText("admin");
        userLabel.setText(rb.getString("username"));
        passLabel.setText(rb.getString("password"));
        loginBtn.setText(rb.getString("login"));
        titleLabel.setText(rb.getString("login"));
        userLocationLabel.setText(currentZoneId);
        locationLabel.setText(rb.getString("location"));
        exitBtn.setText(rb.getString("exit"));
    }

    /**
     * Determines if user information is valid
     * @param actionEvent when login button pressed
     * @throws IOException
     * @throws SQLException
     */
    public void onLogin(ActionEvent actionEvent) throws IOException, SQLException, ParseException {
        String enteredUser = usernameText.getText();
        String enteredPass = passwordText.getText();
        int userID = getUserID(enteredUser);
        Main.currentUserID = String.valueOf(userID);
        Main.currentUserName = usernameText.getText();

        writeLog();

        if (isValidPassword(userID, enteredPass)){
            System.out.println("Login successful!");
            Stage stage = (Stage) loginBtn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            stage.setTitle("Main Screen");
            stage.setScene(new Scene(root));
            stage.show();

            // get current time at login
            Date loggedTime = new Date();
            //Date loggedTime = new Date(currentLocalDate.getTime());

            System.out.println("You logged in at: " + loggedTime);

            getAppointmentTimes(loggedTime);

        } else {
            loginError();
        }
    }

    /**
     * Exits the application
     * @param actionEvent when exit button is pressed
     */
    public void onExit(ActionEvent actionEvent) {
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Determines if entered password matches user records
     * @param userID ID of user attempting login to match with password
     * @param password Password being entered
     * @return password is or is not valid boolean
     * @throws SQLException
     */
    private boolean isValidPassword(int userID, String password) throws SQLException {

        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "SELECT password FROM users WHERE User_ID ='" + userID + "'";;
        ResultSet rs = statement.executeQuery(sqlStatement);

        while (rs.next()) {
            if (rs.getString("password").equals(password)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the ID of the corresponding username
     * @param username Username to retrieve ID from
     * @return User ID int
     * @throws SQLException
     */
    private int getUserID(String username) throws SQLException {
        int userID = -1;
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "SELECT User_ID FROM users WHERE User_Name ='" + username + "'";
        ResultSet rs = statement.executeQuery(sqlStatement);

        while (rs.next()) {
            userID = rs.getInt("User_ID");
        }
        return userID;
    }

    /**
     * When login fails, display this error
     */
    private void loginError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(rb.getString("error"));
        alert.setHeaderText(rb.getString("header"));
        alert.setContentText(rb.getString("context"));
        alert.showAndWait();
    }

    /**
     * Gets appointment times for user logging in to check if there are upcoming appointments, either displays message
     * that there are no upcoming appointments, or there are within 15 minutes
     * @param loggedTime Time user logs in
     * @throws SQLException
     */
    private void getAppointmentTimes(Date loggedTime) throws SQLException, ParseException {
        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "SELECT Start, Appointment_ID FROM appointments WHERE User_ID ='" + Main.currentUserID + "'";
        ResultSet rs = statement.executeQuery(sqlStatement);
        boolean upcoming = false;

        while(rs.next()){
            //get database start and end time stored as UTC
            String startUTC = rs.getString("start").substring(0, 19);

            //get appointment id
            String appID = rs.getString(("appointment_id"));

            //convert database UTC to LocalDateTime
            LocalDateTime utcStartDT = LocalDateTime.parse(startUTC, datetimeDTF);

            //convert times UTC zoneId to local zoneId
            ZonedDateTime localZoneStart = utcStartDT.atZone(utcZoneID).withZoneSameInstant(localZoneID);

            //convert ZonedDateTime to a string
            String localStartDT = localZoneStart.format(datetimeDTF);

            //convert string to date for time comparison
            Date startTime = simpleFormat.parse(localStartDT);

            //checks if start date matches current date within 15 minutes
            if((startTime.getTime() - loggedTime.getTime() <= 900000) && (startTime.getTime() - loggedTime.getTime() >= 0)){
                upcoming = true;
                System.out.println("You have an appointment within 15 minutes!");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notice");
                alert.setHeaderText("Appointment Imminent");
                alert.setContentText("You have an appointment within 15 minutes! ID: " + appID + ", Date: " + startTime);
                alert.showAndWait();
            }
        }
        if (upcoming == false){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notice");
            alert.setHeaderText("No Upcoming Appointments");
            alert.setContentText("You have no upcoming appointments.");
            alert.showAndWait();
        }
    }

    /**
     * writes user login attempts to login_activity.txt, both successful and unsuccessful
     * @throws IOException
     * @throws SQLException
     */
    public void writeLog() throws IOException, SQLException {

        Date currentDate = new Date();

        String fileName = "login_activity.txt", user;
        FileWriter outputText = new FileWriter(fileName, true);
        PrintWriter outputFile = new PrintWriter(outputText);

        if (isValidPassword(getUserID(usernameText.getText()), passwordText.getText())) {
        user = usernameText.getText();
        outputFile.println(user +" successfully logged in at "+ currentDate);

        outputFile.close();
        System.out.println("User logged to file.");
        } else {
            outputFile.println("Unsuccessful attempted login at " + currentDate);
            outputFile.close();
            System.out.println("Attempt logged to file.");
        }
    }
}
