package com.company;

/**
 * Main class
 */
/**
 * @author James Badke
 */

import util.JDBC;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Main extends Application{

    public static String currentUserID;
    public static String currentUserName;
    @Override
    /**
     * gets first screen of program
     */
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        stage.setTitle("User Login");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * connects to sql database
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        JDBC.openConnection();

        launch(args);

        JDBC.closeConnection();
    }
}