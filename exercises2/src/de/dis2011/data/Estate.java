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
 CREATE TABLE Estate(Estateid INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
 city varchar(255),
 postal_code INTEGER,
 street varchar(255),
 street_number INTEGER,
  square_area INTEGER	);


	CREATE TABLE Management(
id_person INTEGER,
id_apartment INTEGER UNIQUE NOT NULL,
tenancy_contract_number INTEGER NOT NULL,
PRIMARY KEY(tenancy_contract_number),
FOREIGN KEY(id_person) REFERENCES Person(id),
FOREIGN KEY(id_apartment) REFERENCES House(id),
FOREIGN KEY(tenancy_contract_number) REFERENCES TENANCYCONTRACT(contract_number)
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
				// ts.setPostalCode(rs.getInt("Postal Code"));
				ts.setStreet(rs.getString("Street"));
			//	ts.setStreetNumber(rs.getInt("Street Number"));
			//	ts.setSquareArea(rs.getInt("Square Area"));

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
		Connection con = DB2ConnectionManager.getInstance().getConnection();
	try {
		String selectSQL = "SELECT * FROM estate WHERE estateid = ?";
		PreparedStatement pstmt = con.prepareStatement(selectSQL);

		pstmt.setInt(1, getId());

		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			// If an ID already exists, delete it
			String deleteSQL = "DELETE FROM estate WHERE estateid = ?";
			PreparedStatement pstmt2 = con.prepareStatement(deleteSQL);
			pstmt2.executeUpdate();

			pstmt2.close();

			} else {
				System.out.println("This estate is not in the database.");
			}
			pstmt.close();

        }catch (SQLException e) {
			e.printStackTrace();
		}
	}


}
