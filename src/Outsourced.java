package com.example.inventorymanagementsystem;

/**
 * The Outsourced class extends the abstract class part. The Outsourced class
 * represents an Outsourced part.
 *
 * @author Aidan Strawder
 */

public class Outsourced extends Part{
    private String companyName;

    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * This method sets the company name for the Outsourced part.
     *
     * @param companyName the name to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * This method returns the company name for the Outsourced part.
     *
     * @return the company name
     */
    public String getCompanyName(){
        return companyName;
    }
}
