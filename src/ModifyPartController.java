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
 * The ModifyPartController class controls the functionality of the modify part form and allows editing of
 * a part that is present in the inventory.
 *
 * @author Aidan Strawder
 */

public class ModifyPartController {

    private Part part;
    private int partIndex;
    @FXML
    private Label machineOrName;
    @FXML
    private RadioButton inHouseRadioButton;
    @FXML
    private RadioButton outSourcedRadioButton;
    @FXML
    private TextField idField;
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

    /**
     * This method sets the part and part index fields within the class, populates the text fields in the modify part form,
     * and sets the bottom label to machine id or company name based on the instance of the part.
     *
     * @param part the part to be modified
     */
    public void initPart(Part part){
        this.part = part;
        this.partIndex = Inventory.getAllParts().indexOf(part);

        if(part instanceof InHouse){
            inHouseRadioButton.setSelected(true);
            nameOrMIDField.setText(String.valueOf(((InHouse) part).getMachineId()));
            machineOrName.setText("Machine ID");
        }
        if(part instanceof Outsourced){
            outSourcedRadioButton.setSelected(true);
            nameOrMIDField.setText(((Outsourced) part).getCompanyName());
            machineOrName.setText("Company Name");
        }

        idField.setText(String.valueOf(part.getId()));
        nameField.setText(part.getName());
        invField.setText(String.valueOf(part.getStock()));
        pcField.setText(String.valueOf(part.getPrice()));
        maxField.setText(String.valueOf(part.getMax()));
        minField.setText(String.valueOf(part.getMin()));
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
     * This method will take all the data from the text fields and attempt to change/set the
     * data within the part that is being modified. If successful the part will be updated
     * within the inventory. The form the closes and returns to the main form.
     * <br>
     * <br>
     * RUNTIME ERROR:
     * Try/catch is used to catch number format exceptions that are thrown within the method. A number format exception is thrown if invalid
     * input is detected.
     * <br>
     * <br>
     * LOGICAL ERROR:
     * An if statement is used to handle the logical errors associated with min, max, and stock. An if statement is also used to
     * handle changing the instance of a part. If this is done then a new part of the correct changed instance will be created and updated in the
     * inventory.
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
                if(part instanceof InHouse){
                    part.setId(this.part.getId());
                    part.setName(name);
                    part.setPrice(price);
                    part.setStock(stock);
                    part.setMin(min);
                    part.setMax(max);
                    ((InHouse) part).setMachineId(machineID);
                }
                else{
                    part = new InHouse(this.part.getId(), name, price, stock, min, max, machineID);
                }
                Inventory.updatePart(partIndex, part);
            }
            if(outSourcedRadioButton.isSelected()){
                String companyName = nameOrMIDField.getText();
                if(companyName.isEmpty() || companyName.isBlank()){
                    throw new NumberFormatException();
                }
                if(part instanceof Outsourced){
                    part.setId(this.part.getId());
                    part.setName(name);
                    part.setPrice(price);
                    part.setStock(stock);
                    part.setMin(min);
                    part.setMax(max);
                    ((Outsourced) part).setCompanyName(companyName);
                }
                else{
                    part = new Outsourced(this.part.getId(), name, price, stock, min, max, companyName);
                }
                Inventory.updatePart(partIndex, part);
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
