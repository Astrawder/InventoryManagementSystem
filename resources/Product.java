package com.example.inventorymanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class represents a product within the application. This class contains the
 * general functionality of a product using setters and getter methods. This class also
 * contains a list of parts associated with the product that is instantiated.
 *
 * @author Aidan Strawder
 */

public class Product {

    private ObservableList<Part> associatedParts;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.associatedParts = FXCollections.observableArrayList();
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Returns the identification number of the product.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the identification number of the product.
     *
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name of the product.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the product.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the price of the product.
     *
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the product.
     *
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Returns the stock of the product.
     *
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the stock of the product.
     *
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Returns the min of the product.
     *
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * Sets the min of the product.
     *
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Returns the max of the product.
     *
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * Sets the max of the product.
     *
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Adds the part to the associated parts list
     *
     * @param part the part to add to the associate parts list
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /**
     * Removes the selected part from the associated parts list and returns true if removed and false if not removed.
     *
     * @param selectedAssociatedPart the part to remove from the associate parts list
     * @return boolean value
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        return associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * Returns the associatedParts list of the product
     *
     * @return the associated parts list
     */
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }
}
