package com.example.inventorymanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.util.Comparator;

/**
 * The AddProductController class controls the functionality of the add product form and allows creation of
 * a product that is added to the inventory and displayed in the main form.
 *
 * @author Aidan Strawder
 */

public class AddProductController {

    private Comparator<Part> sortByID;
    @FXML
    private TextField nameField;
    @FXML
    private TextField invField;
    @FXML
    private TextField pcField;
    @FXML
    private TextField maxField;
    @FXML
    private TextField minField;
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

    private ObservableList<Part> associatedPartList;
    @FXML
    private TableView<Part> associatedPartTable;
    @FXML
    private TableColumn<String, Integer> associatedIDColumn;
    @FXML
    private TableColumn<Part, String> associatedNameColumn;
    @FXML
    private TableColumn<Part, Integer> associatedInvColumn;
    @FXML
    private TableColumn<Part, Double> associatedPCColumn;

    public void initialize(){
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPCColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        partTable.setItems(Inventory.getAllParts());

        associatedIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPCColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        sortByID = Comparator.comparing(Part::getId);
        associatedPartList = FXCollections.observableArrayList();
    }

    /**
     * This method grabs the last product within the products list. Takes the ID number from that product and increases it by one
     * and then returns it as the new generated product ID. This makes the generated product IDs contiguous.
     *
     * @return generated part id
     */
    private int generateProductID(){
        int id;
        if(Inventory.getAllProducts().isEmpty()){
            id = 1000;
        }
        else {
            Product product = Inventory.getAllProducts().get(Inventory.getAllProducts().size() - 1);
            id = product.getId();
            id++;
        }
        return id;
    }
    /**
     * This method is called when characters are typed into the search field above the parts table inside the add product form. This method will search
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
    private void onSearchPartInput(){
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
     * This method is called when the add button within the add product form is clicked.
     * This method takes a selected part within the parts table and adds it to the associated parts table.
     */
    @FXML
    private void onAddButtonClick(){
        Part selectedPart = partTable.getSelectionModel().getSelectedItem();
        associatedPartList.add(selectedPart);
        associatedPartList.sort(sortByID);
        associatedPartTable.setItems(associatedPartList);
        partTable.getSelectionModel().select(null);
    }

    /**
     * This method is called when the remove associated part button is clicked.
     * This method removes a part from the associated part list after confirmation.
     * <br>
     * <br>
     * LOGICAL ERROR:
     * If a part was not selected when the button is clicked then a descriptive alert will pop up. This error was handled using an
     * if statement.
     */
    @FXML
    private void onRemoveAssociatedPartButtonClick(){
        Part selectedPart = associatedPartTable.getSelectionModel().getSelectedItem();

        if(associatedPartTable.getSelectionModel().getSelectedItem() == null){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("An item must be selected before it can be removed!");
            a.show();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you would like to remove the selected part?");
        alert.showAndWait();
        if(alert.getResult().getButtonData().isDefaultButton()){
            associatedPartList.remove(selectedPart);
            associatedPartList.sort(sortByID);
            associatedPartTable.setItems(associatedPartList);
        }
        partTable.getSelectionModel().select(null);
    }

    /**
     * This method will take all the data from the text fields and associated parts table and attempt to
     * create a product with that data. If successful the product will be added
     * to the inventory. The form then closes and returns to the main form.
     * <br>
     * <br>
     * RUNTIME ERROR:
     * Try/catch is used to catch number format exceptions that are thrown within the method. A number format exception is thrown if invalid
     * input is detected.
     * <br>
     * <br>
     * LOGICAL ERROR:
     * An if statement is used to handle the logical errors associated with min, max, and stock.
     * <br>
     * <br>
     * @param event the event passed when the button is clicked. This event is used to properly close the form
     */
    @FXML
    private void onSaveButtonClick(ActionEvent event){
        try {
            String name = nameField.getText();
            double price = Double.parseDouble(pcField.getText());
            int stock = Integer.parseInt(invField.getText());
            int min = Integer.parseInt(minField.getText());
            int max = Integer.parseInt(maxField.getText());

            if(max <= min || stock <= min || stock >= max || name.isEmpty() || name.isBlank()){
                throw new NumberFormatException();
            }
            else{
                Product product = new Product(generateProductID(), name, price, stock, min, max);
                for (Part p : associatedPartList) {
                    product.addAssociatedPart(p);
                }
                Inventory.addProduct(product);
            }
        }
        catch (NumberFormatException e){
            e.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Invalid Input");
            a.setContentText("Please remember to enter the appropriate type of data into the text fields!");
            a.show();
            return;
        }

        Node node = (Node) event.getSource();
        Stage currentStage = (Stage) node.getScene().getWindow();
        currentStage.getOnCloseRequest().handle(null);
        currentStage.close();
    }

    /**
     * This method closes the form and returns to the main form.
     *
     * @param event the event passed when the button is clicked. This event is used to properly close the form
     */
    @FXML
    private void onCancelButtonClick(ActionEvent event){
        Node node = (Node) event.getSource();
        Stage currentStage = (Stage) node.getScene().getWindow();
        currentStage.hide();
    }
}
