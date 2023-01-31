package com.example.inventorymanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * This class represents the Inventory that contains parts and products. This class contains
 * methods to add, lookup, update,and delete parts and products.
 *
 * @author Aidan Strawder
 */

public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * This method appends a new part to the parts list.
     *
     * @param newPart adds the part to the inventory
     */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }

    /**
     * This method appends a new product to the products list.
     *
     * @param newProduct adds the product to the inventory
     */
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    /**
     * This method takes in an identification number as a parameter and searches for and returns the identification
     * number. If it's not found it returns null.
     *
     * @param partId the id to look up in the parts list
     * @return the part id
     */
    public static Part lookupPart(int partId){
        Part part = null;
        for (Part p :allParts) {
            if(p.getId() == partId){
                part = p;
            }
        }
        return part;
    }

    /**
     * This method takes in an identification number as a parameter and searches for and returns the identification
     * number. If it's not found it returns null.
     *
     * @param productId the id to look up in the products list
     * @return the product id
     */
    public static Product lookupProduct(int productId){
        Product product = null;
        for (Product p :allProducts) {
            if(p.getId() == productId){
                product = p;
            }
        }
        return product;
    }

    /**
     * This method takes in a part name as a parameter and searches for and return a list of parts matching
     * the full or partial name.
     *
     * @param partName the name to look up in the parts list
     * @return the list of parts that match the input
     */
    public static ObservableList<Part> lookupPart(String partName){
        FilteredList<Part> matches = new FilteredList<>(FXCollections.observableArrayList());

        for (Part p : allParts) {
            if(p.getName().equals(partName)){
                matches = new FilteredList<Part>(allParts, part -> part.getName().equals(partName));
            }
            else{
                matches = new FilteredList<Part>(allParts, part -> part.getName().contains(partName));
            }
        }
        return matches;
    }

    /**
     * This method takes in a product name as a parameter and searches for and return a list of products matching
     * the full or partial name.
     *
     * @param productName the name to lookup in the products list
     * @return the list of products that match the input
     */
    public static ObservableList<Product> lookupProduct(String productName){
        FilteredList<Product> matches = new FilteredList<>(FXCollections.observableArrayList());

        for (Product p : allProducts) {
            if(p.getName().equals(productName)){
                matches = new FilteredList<Product>(allProducts, product -> product.getName().equals(productName));
            }
            else{
                matches = new FilteredList<Product>(allProducts, product -> product.getName().contains(productName));
            }
        }
        return matches;
    }

    /**
     * Replaces the current part at the specific index with the new part.
     *
     * @param index index of part in parts list
     * @param selectedPart the new part that replaces the old part
     */
    public static void updatePart(int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }

    /**
     * Replaces the current product at the specific index with the new product.
     *
     * @param index index of product in products list
     * @param newProduct the new product that replaces the old product
     */
    public static void updateProduct(int index, Product newProduct){
        allProducts.set(index, newProduct);
    }

    /**
     * Delete the part from the list and return true or false if completed.
     *
     * @param selectedPart part to be deleted
     * @return boolean value
     */
    public static boolean deletePart(Part selectedPart){
        return allParts.removeIf(part -> part.equals(selectedPart));
    }

    /**
     * Delete the product from the list and return true or false if completed.
     *
     * @param selectedProduct product to be deleted
     * @return boolean value
     */
    public static boolean deleteProduct(Product selectedProduct){
        return allProducts.removeIf(product -> product.equals(selectedProduct) && product.getAllAssociatedParts().isEmpty());
    }

    /**
     * Returns the current list of parts in the inventory.
     *
     * @return list of parts
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Returns the current list of products in the inventory.
     *
     * @return list of products
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
