package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.dis2011.data.DB2ConnectionManager;

/**
 * Estate-Bean
 *
 * Beispiel-Tabelle:

CREATE TABLE ESTATE(
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
    city VARCHAR(255),
    postal_code INTEGER,
    street VARCHAR(255),
    street_number INTEGER,
    square_area INTEGER,
    manager VARCHAR(40) NOT NULL,
    FOREIGN KEY(manager) REFERENCES EstateAgent(login) ON DELETE CASCADE
);


CREATE TABLE Management(
    id_agent INTEGER,
    id_estate INTEGER UNIQUE NOT NULL,
    PRIMARY KEY(id_estate),
    FOREIGN KEY(id_agent) REFERENCES EstateAgent(id),
    FOREIGN KEY(id_estate) REFERENCES Estate(id),
);


 */
public class Estate {
	private int id = -1;
	private String city;
	private int postal_code;
	private String street;
	private int street_number;
	private int square_area;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getPostalCode() {
		return postal_code;
	}

	public void setPostalCode(int postal_code) {
		this.postal_code = postal_code;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getStreetNumber() {
		return street_number;
	}

	public void setStreetNumber(int street_number) {
		this.street_number = street_number;
	}

	public int getSquareArea() {
		return square_area;
	}

		public void setSquareArea(int square_area) {
		this.square_area = square_area;
	}

	/**
	 * Lädt einen Estate aus der Datenbank
	 * @param id ID des zu ladenden Estate
	 * @return Estate-Instanz
	 */
	public static Estate load(int id) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM estate WHERE estateid = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {

				Estate ts = new Estate();

				ts.setId(id);
				ts.setCity(rs.getString("City"));
				ts.setPostalCode(rs.getInt("Postal_Code"));
				ts.setStreet(rs.getString("Street"));
				ts.setStreetNumber(rs.getInt("Street_Number"));
				ts.setSquareArea(rs.getInt("Square_Area"));

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
	 Saves Estate in the database. If no ID has yet been assigned,
	 the enerated id is fetched from DB2 and passed to the model.
	 */
	public void save() {
		// Get connected
		Connection con = DB2ConnectionManager.getInstance().getConnection();

		try {
			// Add a new element if the object does not already have an ID.
			if (getId() == -1) {
				//Attention, here a parameter is given, so that later generated IDs are returned!
				String insertSQL = "INSERT INTO Estate(city, postal_code, street, street_number, square_area) VALUES (?, ?, ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Set request parameters and execute request
				pstmt.setString(1, getCity());
				pstmt.setInt(2, getPostalCode());
				pstmt.setString(3, getStreet());
				pstmt.setInt(4, getStreetNumber());
				pstmt.setInt(5, getSquareArea());
				pstmt.executeUpdate();

				// Get the id of the tight set
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					setId(rs.getInt(1));
				}

				rs.close();
				pstmt.close();
			} else {
				// If an ID already exists, make an update ...
				String updateSQL = "UPDATE Estate SET city = ?, postal_code = ?, street = ?, street_number = ?, square_area = ? WHERE estateid = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Set request parameters
				pstmt.setString(1, getCity());
				pstmt.setInt(2, getPostalCode());
				pstmt.setString(3, getStreet());
				pstmt.setInt(4, getStreetNumber());
				pstmt.setInt(5, getSquareArea());
				pstmt.setInt(6, getId());
				pstmt.executeUpdate();

				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 Delete Estate in the database.
	 */
	public void delete() {
		// Get connected
		Connection con = DB2ConnectionManager.getInstance().getConnection();

		try {
			// Check if the Estate already exists
			if (getId() == -1) {

				System.out.println("This estate is not in the database.");
			} else {
				// If an ID already exists, delete it
				String deleteSQL = "DELETE FROM estate WHERE estateid = ?";
				PreparedStatement pstmt = con.prepareStatement(deleteSQL);


				// Set request parameters

				pstmt.setInt(1, getId());
				pstmt.executeUpdate();

				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


}
