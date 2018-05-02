package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.dis2011.data.DB2ConnectionManager;

/**
 * House-Bean ////////////////////////////HAY QUE HACERLA
 *
 CREATE TABLE House(id INTEGER NOT NULL PRIMARY KEY,
  	floors INTEGER,
		price INTEGER,
  	garden SMALLINT,
	FOREIGN KEY(id) REFERENCES Estate(id)
	);
 */
public class House extends Estate {
	private int floors;
	private int price;
	private boolean garden;



	public int getFloors() {
		return floors;
	}

	public void setFloors(int floors) {
		this.floors = floors;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getGarden(){
		return garden ? 1:0;
	}

	public void setGarden(boolean garden){
		this.garden = garden;
	}

	/**
	 Saves Apartament in the database. If no ID has yet been assigned,
	 the enerated id is fetched from DB2 and passed to the model.
	 */
	public void save() {

		super.save();
		// Get connected
		Connection con = DB2ConnectionManager.getInstance().getConnection();

		try {
				// If an ID already exists, make an update ...
				String updateSQL = "INSERT INTO House(floors, price, garden, estateid) VALUES (?, ?, ?, ?)";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Set request parameters
				pstmt.setInt(1, getFloors());
				pstmt.setInt(2, getPrice());
				pstmt.setInt(3, getGarden());
				pstmt.setInt(4, super.getId());
				pstmt.executeUpdate();

				pstmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


}
