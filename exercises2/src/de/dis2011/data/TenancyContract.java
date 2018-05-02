package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Date;


import de.dis2011.data.DB2ConnectionManager;

/**
 * TenancyContract-Bean
 *
 * Beispiel-Tabelle:

 CREATE TABLE TenancyContract(contract_number INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
 contract_start_date date,
 place varchar(255),
 FOREIGN KEY(contract_number) REFERENCES Contract(contract_number)
 );
 */
public class TenancyContract extends Contract{
	private String start_date;
	private int duration;
	private int additional_cost;


	public String getStartDate(){
		return start_date;
	}
	public void setStartDate(String start_date){
		this.start_date = start_date;
	}

	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getAdditionalCost() {
		return additional_cost;
	}

	public void setAdditionalCost(int additional_cost) {
		this.additional_cost = additional_cost;
	}

        /**
	 Saves TenancyContract in the database. If no ID has yet been assigned,
	 the enerated id is fetched from DB2 and passed to the model.
	 */
	public void save() {
		// Get connected
		Connection con = DB2ConnectionManager.getInstance().getConnection();

                super.save();
                
		try {
			// Add a new element if the object does not already have an ID.
			/*if ( super.getContractNumber() == -1 ) {
				//Attention, here a parameter is given, so that later generated IDs are returned!
				String insertSQL = "INSERT INTO TenancyContract(start_date, duration, additional_cost) VALUES (?,?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Set request parameters and execute request
				pstmt.setString(1, getStartDate());
				pstmt.setInt(2, getDuration());
                                pstmt.setInt(3, getAdditionalCost());
				pstmt.executeUpdate();

				// Get the id of the tight set
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					super.setContractNumber(rs.getInt(1));
				}

				rs.close();
				pstmt.close();
			} else {*/
				// If an ID already exists, make an update ...
				String updateSQL = "INSERT INTO TenancyContract(contract_number, start_date, duration, additional_cost) VALUES (?,?,?,?)";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Set request parameters
                                pstmt.setInt(1, super.getContractNumber());
				pstmt.setString(2, getStartDate());
				pstmt.setInt(3, getDuration());
                                pstmt.setInt(4, getAdditionalCost());
				pstmt.executeUpdate();

				pstmt.close();
			//}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
        
        
        public static TenancyContract load(int contract_number) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

                       
                        String selectSQL = "SELECT * FROM Contract WHERE contract_number = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, contract_number);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
                            String selectSQL2 = "SELECT * FROM TenancyContract WHERE contract_number = ?";
                            PreparedStatement pstmt2 = con.prepareStatement(selectSQL2);
                            pstmt2.setInt(1, contract_number);

                            // Führe Anfrage aus
                            ResultSet rs2 = pstmt2.executeQuery();
                            if (rs2.next()) {

                                    TenancyContract c = new TenancyContract();
                                    c.setContractNumber(contract_number);
                                    c.setDate(rs.getString("date"));
                                    c.setPlace(rs.getString("place"));
                                    c.setStartDate(rs2.getString("start_date"));
                                    c.setDuration(rs2.getInt("duration"));
                                    c.setAdditionalCost(rs2.getInt("additional_cost"));

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
            System.out.println("Start Date: " + getStartDate());
            System.out.println("Duration: " + getDuration());
            System.out.println("Additional Cost: " + getAdditionalCost());
            System.out.println();
        }


}
