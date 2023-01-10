package controller;

/**
 * ReportsController class
 */
/**
 * @author James Badke
 */

import util.TableInterface;
import util.TextInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    public Button backBtn;
    public TextField custInUSField;
    public TextField custInUKField;
    public TextField custInCanadaField;
    public TextField totPlanField;
    public TextField totDeBriefField;
    public TextField janField;
    public TextField febField;
    public TextField marField;
    public TextField aprField;
    public TextField mayField;
    public TextField junField;
    public TextField julField;
    public TextField augField;
    public TextField sepField;
    public TextField octField;
    public TextField novField;
    public TextField decField;

    public TableView contactTable;
    public TableColumn contactIDColumn;
    public TableColumn appIDColumn;
    public TableColumn titleColumn;
    public TableColumn typeColumn;
    public TableColumn descripColumn;
    public TableColumn startColumn;
    public TableColumn endColumn;
    public TableColumn custIDColumn;

    public int numOfUSA = 0;
    public int numOfUK = 0;

    public int numOfCanada = 0;
    public int numOfPlanning = 0;
    public int numOfDeBriefing = 0;

    public int[] months = new int[12];

    private ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    private final DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ZoneId localZoneID = ZoneId.systemDefault();
    private final ZoneId utcZoneID = ZoneId.of("UTC");

    /**
     * runs immediately upon loading "Reports" screen. TableInterface, TextInterface and lambda expressions are
     * used to reduce code length for adding values to table columns.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            populateTable();
            getNumOfCountries();
            getNumOfAppTypes();
            getDates();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        contactTable.setItems(allAppointments);

        /**
         * TableInterface and lambda expression used to reduce code length for adding values to table columns
         */
        // use lambda expression to set values in table columns
        TableInterface table = (tableColumn, string) -> tableColumn.setCellValueFactory(new PropertyValueFactory<>(string));
        table.buildTable(appIDColumn, "id");
        table.buildTable(titleColumn, "title");
        table.buildTable(startColumn, "start");
        table.buildTable(endColumn, "end");
        table.buildTable(typeColumn, "type");
        table.buildTable(contactIDColumn, "contactID");
        table.buildTable(custIDColumn, "custID");
        table.buildTable(descripColumn, "description");

        custInUSField.setText(String.valueOf(numOfUSA));
        custInUKField.setText(String.valueOf(numOfUK));
        custInCanadaField.setText(String.valueOf(numOfCanada));

        totPlanField.setText(String.valueOf(numOfPlanning));
        totDeBriefField.setText(String.valueOf(numOfDeBriefing));

        /**
         * TextInterface and lambda expression used to reduce code length for adding values to text fields
         */
        //use lambda expression to set values of text fields
        TextInterface text = (textField, num) -> textField.setText(String.valueOf(num));
        text.setText(janField, months[0]);
        text.setText(febField, months[1]);
        text.setText(marField, months[2]);
        text.setText(aprField, months[3]);
        text.setText(mayField, months[4]);
        text.setText(junField, months[5]);
        text.setText(julField, months[6]);
        text.setText(augField, months[7]);
        text.setText(sepField, months[8]);
        text.setText(octField, months[9]);
        text.setText(novField, months[10]);
        text.setText(decField, months[11]);

    }

    /**
     * goes back to main screen
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
     * retrieves the total number of appointments per countries
     * @throws SQLException
     */
    public void getNumOfCountries() throws SQLException {
        Statement statement = util.JDBC.connection.createStatement();
        String sqlSelect1 = "SELECT * FROM customers WHERE Division_ID <= 54;";
        statement.execute(sqlSelect1);
        ResultSet rs1 = statement.getResultSet();

        while(rs1.next()){
            numOfUSA++;
        }

        String sqlSelect2 = "SELECT * FROM customers WHERE Division_ID BETWEEN 55 AND 72;";
        statement.execute(sqlSelect2);
        ResultSet rs2 = statement.getResultSet();

        while(rs2.next()){
            numOfCanada++;
        }

        String sqlSelect3 = "SELECT * FROM customers WHERE Division_ID > 72;";
        statement.execute(sqlSelect3);
        ResultSet rs3 = statement.getResultSet();

        while(rs3.next()){
            numOfUK++;
        }
    }

    /**
     * retrieves the total number of appointments by type
     * @throws SQLException
     */
    public void getNumOfAppTypes() throws SQLException {
        Statement statement = util.JDBC.connection.createStatement();
        String sqlSelect1 = "SELECT * FROM appointments WHERE Type = 'Planning Session';";
        statement.execute(sqlSelect1);
        ResultSet rs1 = statement.getResultSet();

        while(rs1.next()){
            numOfPlanning++;
        }

        String sqlSelect2 = "SELECT * FROM appointments WHERE Type = 'De-Briefing';";
        statement.execute(sqlSelect2);
        ResultSet rs2 = statement.getResultSet();

        while(rs2.next()){
            numOfDeBriefing++;
        }
    }

    /**
     * retrieves the total number of appointments by month
     * @throws SQLException
     */
    public void getDates() throws SQLException {
        Statement statement = util.JDBC.connection.createStatement();
        String sqlSelect = "SELECT EXTRACT(Month FROM Start) FROM appointments;";
        statement.execute(sqlSelect);
        ResultSet rs1 = statement.getResultSet();

        while(rs1.next()){
            if(rs1.getInt("EXTRACT(Month FROM Start)") == 1){
                months[0]++;
            }else if(rs1.getInt("EXTRACT(Month FROM Start)") == 2){
                months[1]++;
            }else if(rs1.getInt("EXTRACT(Month FROM Start)") == 3){
                months[2]++;
            }else if(rs1.getInt("EXTRACT(Month FROM Start)") == 4){
                months[3]++;
            }else if(rs1.getInt("EXTRACT(Month FROM Start)") == 5){
                months[4]++;
            }else if(rs1.getInt("EXTRACT(Month FROM Start)") == 6){
                months[5]++;
            }else if(rs1.getInt("EXTRACT(Month FROM Start)") == 7){
                months[6]++;
            }else if(rs1.getInt("EXTRACT(Month FROM Start)") == 8){
                months[7]++;
            }else if(rs1.getInt("EXTRACT(Month FROM Start)") == 9){
                months[8]++;
            }else if(rs1.getInt("EXTRACT(Month FROM Start)") == 10){
                months[9]++;
            }else if(rs1.getInt("EXTRACT(Month FROM Start)") == 11){
                months[10]++;
            }else if(rs1.getInt("EXTRACT(Month FROM Start)") == 12){
                months[11]++;
            }
        }
    }

    /**
     * adds appointments to selected table
     * @throws SQLException
     */
    public void populateTable() throws SQLException {
        Statement statement = util.JDBC.connection.createStatement();
        String sqlSelect = "SELECT * FROM appointments ORDER BY Contact_ID;";
        statement.execute(sqlSelect);
        ResultSet rs = statement.getResultSet();
        while(rs.next()){
            //create appointment to add
            Appointment selectApp = new Appointment();

            //set appointment values to sql columns
            selectApp.setID(rs.getInt("Appointment_ID"));
            selectApp.setTitle(rs.getString("Title"));
            selectApp.setType(rs.getString("Type"));
            selectApp.setCustID(rs.getString("Customer_ID"));
            selectApp.setContactID(rs.getString("Contact_ID"));
            selectApp.setDescription(rs.getString("Description"));

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

            //set time values
            selectApp.setStart(localStartDT);
            selectApp.setEnd(localEndDT);

            allAppointments.add(selectApp);
        }
    }
}