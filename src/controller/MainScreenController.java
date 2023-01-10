package controller;

/**
 * MainScreenController class
 */
/**
 * @author James Badke
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
    public Button custBtn;
    public Button appBtn;
    public Button exitBtn;
    public Button reportsBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * goes to customer screen
     * @param actionEvent customers button press
     * @throws IOException
     */
    public void onCustBtn(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage)custBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/view/Customers.fxml"));
        stage.setTitle("Customers");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * goes to appointments screen
     * @param actionEvent appointments button press
     * @throws IOException
     */
    public void onAppBtn(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage)appBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/view/Appointments.fxml"));
        stage.setTitle("Appointments");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * exits program
     * @param actionEvent exit button press
     */
    public void onExit(ActionEvent actionEvent) {
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * goes to reports screen
     * @param actionEvent reports button press
     * @throws IOException
     */
    public void onReports(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage)reportsBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/view/Reports.fxml"));
        stage.setTitle("Reports");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
