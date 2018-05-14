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

CREATE TABLE HOUSE(
    id INTEGER NOT NULL PRIMARY KEY,
    floors INTEGER,
    price INTEGER,
    garden SMALLINT,
    FOREIGN KEY(id) REFERENCES Estate(id) ON DELETE CASCADE
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

	public void setGarden(int garden){
		if (garden ==0) this.garden = (false);
                else this.garden = true;
	}
        public void setGarden(boolean garden){
		this.garden = garden;
	}
	public static House load(int id) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM House WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {

				House ts = new House();

				ts.setId(id);
				ts.setFloors(rs.getInt("Floors"));
				ts.setPrice(rs.getInt("Price"));
				ts.setGarden(rs.getInt("Garden"));

				String selectSQL1 = "SELECT * FROM estate WHERE id = ?";
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
				// If an ID already exists, make an update ...
				String updateSQL = "INSERT INTO House(floors, price, garden, id) VALUES (?, ?, ?, ?)";
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


		/**
		 Delete House in the database.
	*/
		public void delete() {
			// Get connected
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			try {
				// Check if the Estate already exists
				if (getId() == -1) {

					System.out.println("This house is not in the database.");
				} else {
					// If an ID already exists, delete it
					String deleteSQL = "DELETE FROM house WHERE id = ?";
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
