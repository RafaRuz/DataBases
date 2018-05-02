package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Date;


import de.dis2011.data.DB2ConnectionManager;

/**
 * Contract-Bean
 *

  CREATE TABLE Contract(contract_number INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY ,
  contract_date varchar(255),
  place varchar(255));


	CREATE TABLE Sells(
		 	id_person INTEGER,
		 	id_house INTEGER UNIQUE NOT NULL,
		 	purchase_contract_number INTEGER NOT NULL,
			PRIMARY KEY(purchase_contract_number),
			FOREIGN KEY(id_person) REFERENCES Person(id),
			FOREIGN KEY(id_house) REFERENCES House(id),
			FOREIGN KEY(purchase_contract_number) REFERENCES PURCHASECONTRACT(contract_number)
			);




		CREATE TABLE Rents(
 	id_person INTEGER,
 	id_apartment INTEGER UNIQUE NOT NULL,
 	tenancy_contract_number INTEGER NOT NULL,
	PRIMARY KEY(tenancy_contract_number),
	FOREIGN KEY(id_person) REFERENCES Person(id),
	FOREIGN KEY(id_apartment) REFERENCES House(id),
	FOREIGN KEY(tenancy_contract_number) REFERENCES TENANCYCONTRACT(contract_number)
	);
 */
public class Contract {
	private int contract_number = -1;
	private String date;
	private String place;


	public int getContractNumber() {
		return contract_number;
	}

	public void setContractNumber(int contract_number) {
		this.contract_number = contract_number;
	}

	public String getDate(){
		return date;
	}
	public void setDate(String date){
		this.date = date;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	/**
	 Saves Contract in the database. If no ID has yet been assigned,
	 the enerated id is fetched from DB2 and passed to the model.
	 */
	public void save() {
		// Get connected
		Connection con = DB2ConnectionManager.getInstance().getConnection();

		try {
			// Add a new element if the object does not already have an ID.
			//if (getContractNumber() == -1) {
				//Attention, here a parameter is given, so that later generated IDs are returned!
				String insertSQL = "INSERT INTO Contract(date, place) VALUES (?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Set request parameters and execute request
				pstmt.setString(1, getDate());
				pstmt.setString(2, getPlace());
				pstmt.executeUpdate();

				// Get the id of the tight set
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					setContractNumber(rs.getInt(1));
				}

				rs.close();
				pstmt.close();
			/*} else {
				// If an ID already exists, make an update ...
				String updateSQL = "UPDATE Contract SET contract_date = ?, place = ? WHERE contract_number = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Set request parameters
				pstmt.setString(1, getDate());
				pstmt.setString(2, getPlace());
				pstmt.setInt(3, getContractNumber());
				pstmt.executeUpdate();

				pstmt.close();
			}*/
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
        
        
        
	public static Contract load(int contract_number) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM contract WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, contract_number);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {

				Contract c = new Contract();
				c.setContractNumber(contract_number);
				c.setDate(rs.getString("date"));
				c.setPlace(rs.getString("place"));

				rs.close();
				pstmt.close();
				return c;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
        
        public void print(){
            
        };


}
