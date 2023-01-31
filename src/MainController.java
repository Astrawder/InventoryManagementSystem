package com.example.inventorymanagementsystem;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.IOException;

/**
 * The MainController class enables multiple forms to be opened on button clicks and
 * handles the deletion and searching of parts and products within the application.
 *
 * @author Aidan Strawder
 */

public class MainController {

    @FXML
    private TableView<Part> partTable;
    @FXML
    private TextField partSearch;
    @FXML
    private TableColumn<String, Integer> partIDColumn;
    @FXML
    private TableColumn<Part, String> partNameColumn;
    @FXML
    private TableColumn<Part, Integer> partInvColumn;
    @FXML
    private TableColumn<Part, Double> partPCColumn;
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TextField productSearch;
    @FXML
    private TableColumn<Product, Integer> productIDColumn;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    @FXML
    private TableColumn<Product, Integer> productInvColumn;
    @FXML
    private TableColumn<Product, Double> productPCColumn;

    public void initialize(){
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPCColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPCColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * This method is called once the exit button within the form is clicked. This method
     * causes the application to close/exit.
     */
    @FXML
    private void onExitButtonClick() {
        Platform.exit();
    }

    /**
     * This method is called once the add button below the parts table is clicked. This method
     * will display the add part form and pass control of the application to the AddPartController class. This method
     * then waits for the add part form to be closed and once it closes it will update the parts table.
     */
    @FXML
    private void onAddPartButtonClick() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagementsystem/add-part-view.fxml"));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                partTable.setItems(Inventory.getAllParts());
            }
        });
    }

    /**
     * This method is called once the modify button below the parts table is clicked. This method passes the
     * selected part within the parts table to the ModifyPartController class. This method
     * will display the modify part form and pass control of the application to the ModifyPartController class. This method
     * then waits for the modify part form to be closed and once it closes it will update the parts table.
     * <br>
     * <br>
     * LOGICAL ERROR:
     * If a part is not selected then this method will pop up a descriptive alert and will not continue. This is handled
     * by using an if statement to check if a part was selected or not for modification.
     */
    @FXML
    private void onModifyPartButtonClick() {
        if(partTable.getSelectionModel().getSelectedItem() == null){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("An item must be selected before modification!");
            a.show();
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagementsystem/modify-part-view.fxml"));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setScene(scene);
        Part selectedPart = partTable.getSelectionModel().getSelectedItem();
        ModifyPartController controller = fxmlLoader.getController();
        controller.initPart(selectedPart);
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                partTable.setItems(Inventory.getAllParts());
                partTable.getSelectionModel().select(null);
            }
        });
    }

    /**
     * This method is called once the delete button below the parts table is clicked within the application.
     * This method will delete the selected part within the parts table after confirmation.
     * <br>
     * <br>
     * LOGICAL ERROR:
     * If a part is not selected then this method will pop up a descriptive alert and will not delete. This is handled
     * by using an if statement to check if a part was selected or not.
     */
    @FXML
    private void onDeletePartButtonClick() {
        if(partTable.getSelectionModel().getSelectedItem() == null){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("An item must be selected before deletion!");
            a.show();
            return;
        }
        Part selectedPart = partTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you would like to delete the selected part?");
        alert.showAndWait();
        if(alert.getResult().getButtonData().isDefaultButton()){
            if(Inventory.deletePart(selectedPart)){
                partTable.setItems(Inventory.getAllParts());
            }
            else{
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Part was not deleted!");
                alert.show();
            }
        }
        if(Inventory.getAllParts().isEmpty()){
            partTable.setPlaceholder(new Label("No content in table"));
        }
    }

    /**
     * This method is called when characters are typed into the search field above the parts table. This method will search
     * by part ID number or by part name (full or partial name). If a partial name is detected, all parts will be displayed
     * that contain that partial name. If a full name or an ID number have an exact match detected then the part will be displayed
     * and automatically selected.
     * <br>
     * <br>
     * LOGICAL ERROR:
     * This method will not search for any parts unless the parts table is populated with at least one part. If the search field
     * is empty the parts table will be repopulated. If a part is not found using the name or the ID number then an error
     * will be displayed in the UI of the parts table. These logical errors are all handled with if-else statements. The detection of whether
     * an ID number is being used for searching or not is detected by the regular expression that is used.
     */
    @FXML
    private void onSearchPartInput() {
        ObservableList<Part> partsListFiltered;
        Part part;
        if(Inventory.getAllParts().isEmpty()){
            return;
        }
        if(!partSearch.getText().isEmpty()){
            if(partSearch.getText().matches("[0-9]+")){
                int partID = Integer.parseInt(partSearch.getText());
                part = Inventory.lookupPart(partID);
                if(part != null){
                    partsListFiltered = FXCollections.observableArrayList();
                    partsListFiltered.add(part);
                    partTable.setItems(partsListFiltered);
                    partTable.getSelectionModel().select(0);
                }
                else{
                    partTable.setItems(null);
                    partTable.setPlaceholder(new Label("Part ID Not Found"));
                }
            }
            else {
                partsListFiltered = Inventory.lookupPart(partSearch.getText());
                if(!partsListFiltered.isEmpty()){
                    partTable.setItems(partsListFiltered);

                    for (Part p : partsListFiltered) {
                        if(p.getName().equals(partSearch.getText())){
                            partTable.getSelectionModel().select(0);
                        }
                    }
                }
                else{
                    partTable.setItems(null);
                    partTable.setPlaceholder(new Label("Part Not Found"));
                }
            }
        }
        else{
            partTable.getSelectionModel().select(null);
            partTable.setItems(Inventory.getAllParts());
        }
    }

    /**
     * This method is called once the add button below the products table is clicked. This method
     * will display the add product form and pass control of the application to the AddProductController class. This method
     * then waits for the add product form to be closed and once it closes it will update the products table.
     */
    @FXML
    private void onAddProductButtonClick() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagementsystem/add-product-view.fxml"));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                productTable.setItems(Inventory.getAllProducts());
            }
        });
    }

    /**
     * This method is called once the modify button below the products table is clicked. This method passes the
     * selected product within the products table to the ModifyProductController class. This method
     * will display the modify product form and pass control of the application to the ModifyProductController class. This method
     * then waits for the modify product form to be closed and once it closes it will update the products table.
     * <br>
     * <br>
     * LOGICAL ERROR:
     * If a product is not selected then this method will pop up a descriptive alert and will not continue. This is handled
     * by using an if statement to check if a product was selected or not for modification.
     */
    @FXML
    private void onModifyProductButtonClick() {
        if(productTable.getSelectionModel().getSelectedItem() == null){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("An item must be selected before modification!");
            a.show();
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagementsystem/modify-product-view.fxml"));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setScene(scene);
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        ModifyProductController controller = fxmlLoader.getController();
        controller.initProduct(selectedProduct);
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                productTable.setItems(Inventory.getAllProducts());
                productTable.getSelectionModel().select(null);
            }
        });
    }

    /**
     * This method is called once the delete button below the products table is clicked within the application.
     * This method will delete the selected product within the products table after confirmation.
     * <br>
     * <br>
     * LOGICAL ERROR:
     * If a product is not selected then this method will pop up a descriptive alert and will not continue. This is handled
     * by using an if statement to check if a product was selected or not.
     * A product cannot delete if a part is associated with it. A descriptive alert will pop up if a product with associated
     * parts attempts to be deleted.
     */
    @FXML
    private void onDeleteProductButtonClick() {
        if(productTable.getSelectionModel().getSelectedItem() == null){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("An item must be selected before deletion!");
            a.show();
            return;
        }
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you would like to delete the selected part?");
        alert.showAndWait();
        if(alert.getResult().getButtonData().isDefaultButton()){
            if(Inventory.deleteProduct(selectedProduct)){
                productTable.setItems(Inventory.getAllProducts());
            }
            else{
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Product was not deleted because parts are associated with this current product!");
                alert.show();
            }
        }
        if(Inventory.getAllParts().isEmpty()){
            partTable.setPlaceholder(new Label("No content in table"));
        }
    }

    /**
     * This method is called when characters are typed into the search field above the products table. This method will search
     * by product ID number or by product name (full or partial name). If a partial name is detected, all products will be displayed
     * that contain that partial name. If a full name or an ID number have an exact match detected then the product will be displayed
     * and automatically selected.
     * <br>
     * <br>
     * LOGICAL ERROR:
     * This method will not search for any products unless the products table is populated with at least one product. If the search field
     * is empty the products table will be repopulated. If a product is not found using the name or the ID number then an error
     * will be displayed in the UI of the products table. These errors are all handled with if-else statements. The detection of whether
     * an ID number is being used for searching or not is detected by the regular expression that is used.
     */
    @FXML
    private void onSearchProductInput() {
        ObservableList<Product> productsListFiltered;
        Product product;
        if(Inventory.getAllProducts().isEmpty()){
            return;
        }
        if(!productSearch.getText().isEmpty()){
            if(productSearch.getText().matches("[0-9]+")){
                int productID = Integer.parseInt(productSearch.getText());
                product = Inventory.lookupProduct(productID);
                if(product != null){
                    productsListFiltered = FXCollections.observableArrayList();
                    productsListFiltered.add(product);
                    productTable.setItems(productsListFiltered);
                    productTable.getSelectionModel().select(0);
                }
                else{
                    productTable.setItems(null);
                    productTable.setPlaceholder(new Label("Product ID Not Found"));
                }
            }
            else {
                productsListFiltered = Inventory.lookupProduct(productSearch.getText());
                if(!productsListFiltered.isEmpty()){
                    productTable.setItems(productsListFiltered);

                    for (Product p : productsListFiltered) {
                        if(p.getName().equals(productSearch.getText())){
                            productTable.getSelectionModel().select(0);
                        }
                    }
                }
                else{
                    productTable.setItems(null);
                    productTable.setPlaceholder(new Label("Product Not Found"));
                }
            }
        }
        else{
            productTable.getSelectionModel().select(null);
            productTable.setItems(Inventory.getAllProducts());
        }
    }
}