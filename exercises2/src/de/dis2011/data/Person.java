package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.dis2011.data.DB2ConnectionManager;

/**
 * Person-Bean
 *
 Beispiel-Tabelle:
 CREATE TABLE Person(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
 first_name varchar(255),
 name varchar(255),
  address varchar(255));
 */
public class Person {
        private int id;
	private String first_name;
	private String name;
	private String address;

        public Person(){
            setId(-1);
        }
        
        public Person( String first_name, String name, String address ){
            setFirstName(first_name);
            setName(name);
            setAddress(address);
        }
        
        public int getId(){
            return this.id;
        }
        
        public void setId(int id){
            this.id = id;
        }
        
	public String getFirstName() {
		return first_name;
	}

	public void setFirstName(String first_name) {
		this.first_name = first_name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
        
        

	/**
	 Saves Persons in the database. If no ID has yet been assigned,
	 the enerated id is fetched from DB2 and passed to the model.
	 */
	public void save() {
		// Get connected
		Connection con = DB2ConnectionManager.getInstance().getConnection();

		try {
			// Add a new element if the object does not already have an ID.
			if (getId() == -1) {
				//Attention, here a parameter is given, so that later generated IDs are returned!
				String insertSQL = "INSERT INTO Person(first_name, name, address) VALUES (?,?,?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Set request parameters and execute request
				pstmt.setString(1, getFirstName());
				pstmt.setString(2, getName());
                                pstmt.setString(3, getAddress());
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
				String updateSQL = "UPDATE Person SET first_name = ?, name = ?, address = ? WHERE id = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Set request parameters
				pstmt.setString(1, getFirstName());
				pstmt.setString(2, getName());
                                pstmt.setString(3, getAddress());
				pstmt.setInt(4, getId());
				pstmt.executeUpdate();

				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
        
        public static Person load(int id) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM Person WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {

				Person p = new Person();
				p.setId(id);
                                p.setFirstName(rs.getString("first_name"));
				p.setName(rs.getString("name"));
				p.setAddress(rs.getString("address"));

				rs.close();
				pstmt.close();
				return p;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
        
        public static Person load(String first_name, String name, String address ) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM Person WHERE first_name = ? AND name = ? AND address = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setString(1, first_name);
                        pstmt.setString(2, name);
                        pstmt.setString(3, address);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {

				Person p = new Person();
				p.setId(rs.getInt("id"));
                                p.setFirstName(first_name);
				p.setName(name);
				p.setAddress(address);

				rs.close();
				pstmt.close();
				return p;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
