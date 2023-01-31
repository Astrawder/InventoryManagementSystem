package com.example.inventorymanagementsystem;

/**
 * The InHouse class extends the abstract class part. The InHouse class
 * represents an InHouse part.
 *
 * @author Aidan Strawder
 */

public class InHouse extends Part{
    private int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * This method sets the machine identification number for the InHouse part.
     *
     * @param machineId the id to set
     */
    public void setMachineId(int machineId){
        this.machineId = machineId;
    }

    /**
     * This method returns the machine identification number for the InHouse part.
     *
     * @return the machine id
     */
    public int getMachineId(){
        return machineId;
    }
}
