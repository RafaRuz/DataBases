package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Date;


import de.dis2011.data.DB2ConnectionManager;

/**
 * PurchaseContract-Be
 *
 * Beispiel-Tabelle:

CREATE TABLE PURCHASECONTRACT(
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
    installments INTEGER,
    interest INTEGER,
    person_id INTEGER NOT NULL,
   	house_id INTEGER UNIQUE NOT NULL,
    FOREIGN KEY(id) REFERENCES Contract(id) ON DELETE CASCADE,
    FOREIGN KEY(person_id) REFERENCES Person(id) ON DELETE CASCADE,
    FOREIGN KEY(house_id) REFERENCES House(id) ON DELETE CASCADE
);


 */
public class PurchaseContract extends Contract{
    private int installments;
    private int interest;


    public int getInstallments() {
        return installments;
    }
    public void setInstallments(int installments) {
        this.installments = installments;
    }
    public int getInterest() {
        return interest;
    }

    public void setInterest(int interest) {
        this.interest = interest;
    }

    
    /**
     Saves PurchaseContract in the database. If no ID has yet been assigned,
     the enerated id is fetched from DB2 and passed to the model.
     */
    public void save() {
        // Get connected
        Connection con = DB2ConnectionManager.getInstance().getConnection();

        super.save();

        try {
            // Add a new element if the object does not already have an ID.
            if( getId() == -1 ){
                String updateSQL = "INSERT INTO PurchaseContract(id, installments, interest) VALUES (?,?,?)";
                PreparedStatement pstmt = con.prepareStatement(updateSQL);

                // Set request parameters
                pstmt.setInt(1, super.getId());
                pstmt.setInt(2, getInstallments());
                pstmt.setInt(3, getInterest());
                pstmt.executeUpdate();

                pstmt.close();
            } else {
                // If an ID already exists, make an update ...
                String updateSQL = "UPDATE PurchaseContract SET installments = ?, interest = ? WHERE id = ?";
                PreparedStatement pstmt = con.prepareStatement(updateSQL);

                // Set request parameters
                pstmt.setInt(1, getInstallments());
                pstmt.setInt(2, getInterest());
                pstmt.setInt(3, getId());
                pstmt.executeUpdate();

                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static PurchaseContract load(int id) {
        // Hole Verbindung
        Connection con = DB2ConnectionManager.getInstance().getConnection();
        
        try {
            Contract c_sup = Contract.load(id);
            
            if( c_sup != null ){
                String selectSQL2 = "SELECT * FROM PurchaseContract WHERE id = ?";
                PreparedStatement pstmt2 = con.prepareStatement(selectSQL2);
                pstmt2.setInt(1, id);

                // // Check if the PurchaseContract exists
                ResultSet rs2 = pstmt2.executeQuery();
                if (rs2.next()) {
                    PurchaseContract c = new PurchaseContract();
                    c.setId(id);
                    c.setDate(c_sup.getDate());
                    c.setPlace(c_sup.getPlace());
                    c.setInstallments(rs2.getInt("installments"));
                    c.setInterest(rs2.getInt("interest"));

                    rs2.close();
                    pstmt2.close();
                    return c;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    
    public void print(){
        System.out.println("Contract number: " + getId());
        System.out.println("Date: " + getDate());
        System.out.println("Place: " + getPlace());
        System.out.println("Installments: " + getInstallments());
        System.out.println("Interest Rate: " + getInterest());
        System.out.println();
    }
}
