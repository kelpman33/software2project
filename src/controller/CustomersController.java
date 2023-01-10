package controller;

/**
 * CustomersController class
 */
/**
 * @author James Badke
 */

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
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomersController implements Initializable {
    public TextField custNameField;
    public TextField addressField;
    public TextField postCodeField;
    public TextField phoneNumField;
    public TextField custIDField;
    public Button cancelBtn;
    public TableView custTable;
    public TableColumn idColumn;
    public TableColumn nameColumn;
    public TableColumn addressColumn;
    public TableColumn postalColumn;
    public TableColumn phoneColumn;
    public Button addBtn;
    public Button delBtn;
    public Button modBtn;
    public ComboBox countryCBox;
    public ComboBox divCBox;
    public TableColumn countryColumn;
    public TableColumn divisionColumn;

    private ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        custTable.setItems(allCustomers);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalColumn.setCellValueFactory(new PropertyValueFactory<>("zip"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("divisionID"));

        try {
            populateTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        autoGenID();
        fillCBoxes();
    }

    /**
     * adds values and customer to list in table view
     * @throws SQLException
     */
    public void populateTable() throws SQLException {
        Statement statement = util.JDBC.connection.createStatement();
        String sqlSelect = "SELECT * FROM customers ORDER BY Customer_ID;";
        statement.execute(sqlSelect);
        ResultSet rs = statement.getResultSet();

        while(rs.next()){
            Customer selectCust = new Customer();

            selectCust.setID(rs.getInt("Customer_ID"));
            selectCust.setName(rs.getString("Customer_Name"));
            selectCust.setAddress(rs.getString("Address"));
            selectCust.setZip(rs.getString("Postal_Code"));
            selectCust.setPhone(rs.getString("Phone"));
            selectCust.setDivisionID(rs.getInt("Division_ID"));


            if(selectCust.getDivisionID() <= 54){
                selectCust.setCountry("United States");
            } else if (selectCust.getDivisionID() > 54 && selectCust.getDivisionID() <= 72){
                selectCust.setCountry("Canada");
            } else if (selectCust.getDivisionID() > 72){
                selectCust.setCountry("United Kingdom");
            }

            allCustomers.add(selectCust);
        }
    }

    /**
     * goes back to main screen
     * @param actionEvent back button pressed
     * @throws IOException
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        stage.setTitle("Main Screen");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * adds new customer to list and sql database with selected values
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void onAdd(ActionEvent actionEvent) throws SQLException, IOException {

        String id = custIDField.getText();
        String name = custNameField.getText();
        String address = addressField.getText();
        String zip = postCodeField.getText();
        String phone = phoneNumField.getText();
        String division = (String) divCBox.getValue();
        int divisionID = 0;

        if((division == null)){
            emptyValuesError();
        }else{

        Statement statement2 = util.JDBC.connection.createStatement();
        String sqlSelect = "SELECT * FROM first_level_divisions WHERE Division = '"+ division +"';";
        statement2.execute(sqlSelect);
        ResultSet rs2 = statement2.getResultSet();
        while(rs2.next()){
            divisionID = rs2.getInt("Division_ID");
        }

        Statement statement = util.JDBC.connection.createStatement();
        String sqlInsert = "INSERT INTO customers VALUES('" + id + "', '" + name + "', '" + address + "', '" + zip + "', '" + phone + "', null, null, null, null, '"+ divisionID +"');";
        statement.execute(sqlInsert);
        refreshScene(addBtn);
        }
    }

    /**
     * deletes selected customer from list and sql database
     * @param actionEvent delete button pressed
     * @throws SQLException
     * @throws IOException
     */
    public void onDelete(ActionEvent actionEvent) throws SQLException, IOException {

        Customer selectedCust = (Customer) custTable.getSelectionModel().getSelectedItem();

        if(selectedCust == null){
            noneSelectedError();
        }else{

        // check if there are appointments associated with selected customer
        Statement statement = util.JDBC.connection.createStatement();
        String sqlSelect = "SELECT * FROM appointments WHERE Customer_ID = " + selectedCust.getId() + ";";
        statement.execute(sqlSelect);
        ResultSet rs = statement.getResultSet();

        if (!rs.next()) {
            boolean confirm = confirmDelete(selectedCust.getName());
            if (!confirm) {
                return;
            }
            System.out.println("You deleted Customer " + selectedCust.getId() + ": " + selectedCust.getName());
            String sqlDelete = "DELETE FROM customers WHERE Customer_ID = " + selectedCust.getId() + ";";
            statement.execute(sqlDelete);
            refreshScene(delBtn);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notice");
            alert.setHeaderText("Customer has been deleted");
            alert.setContentText("You deleted Customer " + selectedCust.getId() + ": " + selectedCust.getName());
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Customer has associated appointments");
            alert.setContentText("Associated appointments must be deleted before Customer can be deleted.");
            alert.showAndWait();
        }
    }
    }

    /**
     * updates selected user in list and sql database
     * @param actionEvent update button pressed
     * @throws SQLException
     * @throws IOException
     */
    public void onModify(ActionEvent actionEvent) throws SQLException, IOException {
        String name = custNameField.getText();
        String address = addressField.getText();
        String zip = postCodeField.getText();
        String phone = phoneNumField.getText();
        String division = (String) divCBox.getValue();
        int divisionID = 0;

        Customer selectedCust = (Customer) custTable.getSelectionModel().getSelectedItem();

        if(selectedCust == null){
            noneSelectedError();
        }else{


        boolean confirm = confirmUpdate(selectedCust.getName());
        if (!confirm) {
            return;
        }

        Statement statement2 = util.JDBC.connection.createStatement();
        String sqlSelect = "SELECT * FROM first_level_divisions WHERE Division = '"+ division +"';";
        statement2.execute(sqlSelect);
        ResultSet rs2 = statement2.getResultSet();
        while(rs2.next()){
            divisionID = rs2.getInt("Division_ID");
        }

        Statement statement = util.JDBC.connection.createStatement();
        String sqlUpdate = "UPDATE customers SET Customer_Name = '" + name + "', Address = '" + address + "', Postal_Code = '" + zip + "', Phone = '" + phone + "', Division_ID = '"+ divisionID +"' WHERE Customer_ID = " + selectedCust.getId() + ";";
        statement.execute(sqlUpdate);
        refreshScene(modBtn);
    }
    }

    /**
     * disables and automatically sets customer id based on current number of customers
     */
    public void autoGenID(){
        int newCustID = allCustomers.size() + 1;
        custIDField.setText(String.valueOf(newCustID));
        custIDField.setEditable(false);
        custIDField.setDisable(true);
    }

    /**
     * prompts user to confirm deletion of selected customer
     * @param name name of selected customer
     * @return yes or no
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
     * prompts user to confirm update of selected customer
     * @param name name of selected customer
     * @return yes or no
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
     * reloads current screen and by extension all list and table data
     * @param btn
     * @throws IOException
     */
    public void refreshScene(Button btn) throws IOException {
        Stage stage = (Stage)btn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/view/Customers.fxml"));
        stage.setTitle("Customers");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * fills combo box with associated values
     */
    public void fillCBoxes(){
        countryCBox.getItems().addAll("United States", "Canada", "United Kingdom");
    }

    /**
     * depending on country selected, populates division combo box based on associated country
     * @param actionEvent click country combo box
     * @throws SQLException
     */
    public void onCountryCBoxClick(ActionEvent actionEvent) throws SQLException {
        divCBox.getItems().clear();
        // fill division box based on selected country
        if (countryCBox.getValue().toString() == "United States"){
            Statement statement = util.JDBC.connection.createStatement();
            String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = 1;";
            statement.execute(sql);
            ResultSet rs = statement.getResultSet();
            while(rs.next()){
            divCBox.getItems().addAll(rs.getString("Division"));
            }
        } else if (countryCBox.getValue().toString() == "United Kingdom"){
            Statement statement = util.JDBC.connection.createStatement();
            String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = 2;";
            statement.execute(sql);
            ResultSet rs = statement.getResultSet();
            while(rs.next()){
                divCBox.getItems().addAll(rs.getString("Division"));
            }
        } else if (countryCBox.getValue().toString() == "Canada"){
            Statement statement = util.JDBC.connection.createStatement();
            String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = 3;";
            statement.execute(sql);
            ResultSet rs = statement.getResultSet();
            while(rs.next()){
                divCBox.getItems().addAll(rs.getString("Division"));
            }
        }
    }

    /**
     * automatically sets values of text fields and combo boxes based on current selected customer
     * @param mouseEvent
     * @throws SQLException
     */
    public void onClicked(MouseEvent mouseEvent) throws SQLException {

        if(custTable.getSelectionModel().getSelectedItem() == null){
            System.out.println("No item selected");
        } else {

        Customer selectedCust = (Customer) custTable.getSelectionModel().getSelectedItem();
        Statement statement = util.JDBC.connection.createStatement();
        String sqlSelect = "SELECT Division FROM first_level_divisions WHERE Division_ID = "+ selectedCust.getDivisionID() +";";
        statement.execute(sqlSelect);
        ResultSet rs = statement.getResultSet();

        custNameField.setText(selectedCust.getName());
        addressField.setText(selectedCust.getAddress());
        postCodeField.setText(selectedCust.getZip());
        phoneNumField.setText(selectedCust.getPhone());
        countryCBox.setValue(selectedCust.getCountry());
        while(rs.next()){
        divCBox.setValue(rs.getString("Division"));}
        }
    }
    /**
     * display error for no item selected
     */
    public void noneSelectedError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No customer selected");
        alert.setContentText("Please select a customer.");
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
}