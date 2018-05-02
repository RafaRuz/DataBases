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
f
 CREATE TABLE PurchaseContract(contract_number INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
 installments INTEGER,
 interest INTEGER),
 FOREIGN KEY(contract_number) REFERENCES Contract(contract_number)
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
                        // If an ID already exists, make an update ...
                        String updateSQL = "INSERT INTO PurchaseContract(contract_number,installments, interest) VALUES (?,?, ?)";
                        PreparedStatement pstmt = con.prepareStatement(updateSQL);

                        // Set request parameters
                        pstmt.setInt(1, super.getContractNumber());
                        pstmt.setInt(2, getInstallments());
                        pstmt.setInt(3, getInterest());
                        pstmt.executeUpdate();

                        pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
        
        
        
        public static PurchaseContract load(int contract_number) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

                       
                        String selectSQL = "SELECT * FROM contract WHERE contract_number = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, contract_number);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
                            String selectSQL2 = "SELECT * FROM PurchaseContract WHERE contract_number = ?";
                            PreparedStatement pstmt2 = con.prepareStatement(selectSQL2);
                            pstmt2.setInt(1, contract_number);

                            // Führe Anfrage aus
                            ResultSet rs2 = pstmt2.executeQuery();
                            if (rs2.next()) {

                                    PurchaseContract c = new PurchaseContract();
                                    c.setContractNumber(contract_number);
                                    c.setDate(rs.getString("date"));
                                    c.setPlace(rs.getString("place"));
                                    c.setInstallments(rs2.getInt("installments"));
                                    c.setInterest(rs2.getInt("interest"));

                                    rs.close();
                                    pstmt.close();
                                    return c;
                            }
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
        
        public void print(){
            System.out.println("Contract number: " + getContractNumber());
            System.out.println("Date: " + getDate());
            System.out.println("Place: " + getPlace());
            System.out.println("Installments: " + getInstallments());
            System.out.println("Interest Rate: " + getInterest());
            System.out.println();
        }


}
