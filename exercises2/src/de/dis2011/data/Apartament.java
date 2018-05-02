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

	public void setBalcony(int balcony){
                if (balcony ==0) this.balcony = (false);
                else this.balcony = true;
	}
        public void setBalcony(boolean balcony){
               this.balcony = balcony;
	}

	public int getKitchen(){
		return kitchen ? 1:0;
	}

	public void setKitchen(int kitchen){
		if (kitchen ==0) this.kitchen = (false);
                else this.kitchen = true;
	}
        public void setKitchen(boolean kitchen){
		this.kitchen = kitchen;
	}
	/**
	 * Lädt einen Estate aus der Datenbank
	 * @param id ID des zu ladenden Estate
	 * @return Estate-Instanz
	 */
	public static Apartament load(int id) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM apartament WHERE estateid = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {

				Apartament ts = new Apartament();

				ts.setId(id);
				ts.setFloor(rs.getInt("Floor"));
				ts.setRent(rs.getInt("Rent"));
				ts.setRooms(rs.getInt("Rooms"));
				ts.setBalcony(rs.getInt("Balcony"));
				ts.setKitchen(rs.getInt("Kitchen"));
				String selectSQL1 = "SELECT * FROM estate WHERE estateid = ?";
				PreparedStatement pstmt1 = con.prepareStatement(selectSQL1);
				pstmt1.setInt(1, id);

				// Führe Anfrage aus
				ResultSet rs1 = pstmt1.executeQuery();
				if (rs1.next()) {

				ts.setCity(rs1.getString("City"));
				ts.setPostalCode(rs1.getInt("Postal_Code"));
				ts.setStreet(rs1.getString("Street"));
				ts.setStreetNumber(rs1.getInt("Street_Number"));
				ts.setSquareArea(rs1.getInt("Square_Area"));
				rs1.close();
				pstmt1.close();
}

				rs.close();
				pstmt.close();
				return ts;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
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
				String updateSQL = "INSERT INTO apartament(floor, rent, rooms, balcony, kitchen,estateid) VALUES (?, ?, ?, ?, ?, ? )";
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

	/**
	 Delete Apartament in the database.
*/
	public void delete() {
		// Get connected
		Connection con = DB2ConnectionManager.getInstance().getConnection();

		try {
			// Check if the Estate already exists
			if (getId() == -1) {

				System.out.println("This apartament is not in the database.");
			} else {
				// If an ID already exists, delete it
				String deleteSQL = "DELETE FROM apartament WHERE estateid = ?";
				PreparedStatement pstmt = con.prepareStatement(deleteSQL);


				// Set request parameters

				pstmt.setInt(1, getId());
				pstmt.executeUpdate();

                                super.delete();


				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}




}
