package com.example.inventorymanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
// Javadoc is located at \Inventory Management System\Javadoc
/**
 * FUTURE ENHANCEMENTS:
 * This application could utilize a database instead of using a utility class with static list to store the inventory.
 * New constraints could be added for parts and products and their associations with each other.
 * Specific constraints could be added for the InHouse parts and Outsourced parts. For example, InHouse parts can't be deleted their stock just
 * gets set to zero.
 *
 * @author Aidan Strawder
 */

public class Main extends Application {

    /**
     * The first main form displayed to the user when the program runs.
     *
     * @param stage the first stage displayed
     * @throws IOException load() throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Main function and where the application is launched.
     *
     * @param args takes args
     */
    // Javadoc is located at \Inventory Management System\Javadoc
    public static void main(String[] args) {
        launch();
    }
}