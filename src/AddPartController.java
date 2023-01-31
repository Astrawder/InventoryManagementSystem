package com.example.inventorymanagementsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The AddPartController class controls the functionality of the add part form and allows creation of
 * a part that is added to the inventory and displayed in the main form.
 *
 * @author Aidan Strawder
 */

public class AddPartController {

    @FXML
    private Label machineOrName;
    @FXML
    private RadioButton inHouseRadioButton;
    @FXML
    private RadioButton outSourcedRadioButton;
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
    private TextField nameOrMIDField;


    public void initialize(){
        inHouseRadioButton.setSelected(true);
    }

    /**
     * This method grabs the last part within the parts list. Takes the ID number from that part and increases it by one
     * and then returns it as the new generated part ID. This makes the generated part IDs contiguous.
     *
     * @return generated part ID
     */
    private int generatePartID(){
        int id;
        if(Inventory.getAllParts().isEmpty()){
            id = 1;
        }
        else {
            Part part = Inventory.getAllParts().get(Inventory.getAllParts().size() - 1);
            id = part.getId();
            id++;
        }
        return id;
    }

    /**
     * This method handles the functionality of the outsourced radio button. If this button is clicked then
     * it will change the bottom label in the form to require a company name.
     */
    @FXML
    private void onOutsourcedSelected(){
        if(inHouseRadioButton.isSelected()){
            inHouseRadioButton.setSelected(false);
            machineOrName.setText("Company Name");
            nameOrMIDField.clear();
        }
        else{
            outSourcedRadioButton.setSelected(true);
        }
    }

    /**
     * This method handles the functionality of the in house radio button. If this button is clicked then
     * it will change the bottom label in the form to require a machine ID number.
     */
    @FXML
    private void onInHouseSelected(){
        if(outSourcedRadioButton.isSelected()){
            outSourcedRadioButton.setSelected(false);
            machineOrName.setText("Machine ID");
            nameOrMIDField.clear();
        }
        else{
            inHouseRadioButton.setSelected(true);
        }
    }

    /**
     * This method will take all the data from the text fields and attempt to create a part with that data. If successful the part will be added
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

            if(inHouseRadioButton.isSelected()){
                int machineID = Integer.parseInt(nameOrMIDField.getText());
                InHouse part = new InHouse(generatePartID(), name, price, stock, min, max, machineID);
                Inventory.addPart(part);
            }
            if(outSourcedRadioButton.isSelected()){
                String companyName = nameOrMIDField.getText();
                if(companyName.isEmpty() || companyName.isBlank()){
                    throw new NumberFormatException();
                }
                Outsourced part = new Outsourced(generatePartID(), name, price, stock, min, max, companyName);
                Inventory.addPart(part);
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
