package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.dis2011.data.DB2ConnectionManager;

/**
 * Apartament-Bean ////////////////////////////HAY QUE HACERLA

 CREATE TABLE Apartament(estateid INTEGER NOT NULL PRIMARY KEY,
  	floor INTEGER,
		rent INTEGER,
  	rooms INTEGER,
  	balcony SMALLINT,
	kitchen SMALLINT,
	FOREIGN KEY(estateid) REFERENCES Estate(estateid) ON UPDATE CASCADE
	);

 */
public class Apartament extends Estate {
	private int floor;
	private int rent;
	private int rooms;
	private boolean balcony;
	private boolean kitchen;



	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public int getRent() {
		return rent;
	}

	public void setRent(int rent) {
		this.rent = rent;
	}
	public int getRooms() {
		return rooms;
	}

	public void setRooms(int rooms) {
		this.rooms = rooms;
	}

	public int getBalcony(){
		return balcony ? 1:0;
	}

	public void setBalcony(boolean balcony){
		this.balcony = balcony;
	}

	public int getKitchen(){
		return kitchen ? 1:0;
	}

	public void setKitchen(boolean kitchen){
		this.kitchen = kitchen;
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
				String updateSQL = "INSERT INTO Apartament(floor, rent, rooms, balcony, kitchen,estateid) VALUES (?, ?, ?, ?, ?, ? )";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Set request parameters
				pstmt.setInt(1, getFloor());
				pstmt.setInt(2, getRent());
				pstmt.setInt(3, getRooms());
				pstmt.setInt(4, getBalcony());
				pstmt.setInt(5, getKitchen());
				pstmt.setInt(6, super.getId());
				pstmt.executeUpdate();

				pstmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
/*
	/**
	 Delete Apartament in the database.
	 
	public void delete(int id) {
		String selectSQL = "SELECT * FROM apartamet WHERE estateid = ?";
		PreparedStatement pstmt = con.prepareStatement(selectSQL);

		pstmt.setInt(1, id);

		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			// If an ID already exists, delete it
			String deleteSQL = "DELETE FROM apartamet WHERE estateid = ?";
			PreparedStatement pstmt2 = con.prepareStatement(deleteSQL);
			pstmt2.executeUpdate();

			pstmt2.close();

			} else {
				System.out.println("This apartamet is not in the database.");
			}
			pstmt.close();

		 catch (SQLException e) {
			e.printStackTrace();
		}
	}
	*/


}
