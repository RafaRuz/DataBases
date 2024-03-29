package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.dis2011.data.DB2ConnectionManager;

/**
 * EstateAgent-Bean
 *
 * Beispiel-Tabelle:

CREATE TABLE ESTATEAGENT(
    /*id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,*\/
    name VARCHAR(255),
    address VARCHAR(255),
    login VARCHAR(40)  NOT NULL UNIQUE PRIMARY KEY,
    password VARCHAR(40)
);

 */

public class EstateAgent {

	private String name;
	private String address;
	private String login = "#";
	private String password;


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

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Lädt einen EstateAgent aus der Datenbank
	 * @param login login des zu ladenden EstateAgents
	 * @return EstateAgent-Instanz
	 */
	public static EstateAgent load(String login) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM estateagent WHERE login = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setString(1, login);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {

				EstateAgent ts = new EstateAgent();
				ts.setName(rs.getString("name"));
				ts.setAddress(rs.getString("address"));
				ts.setLogin(rs.getString("login"));
				ts.setPassword(rs.getString("password"));

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
	 Saves EstateAgent in the database. If no ID has yet been assigned,
	 the enerated id is fetched from DB2 and passed to the model.
	 */
	public void save() {
		// Get connected
		Connection con = DB2ConnectionManager.getInstance().getConnection();

		try {
							if (load(getLogin())== null) {

						//Attention, here a parameter is given, so that later
						// generated IDs are returned!
		                                 System.out.println("JAJA");
						String insertSQL = "INSERT INTO estateagent(name, address, login, password) VALUES (?, ?, ?, ?)";

						PreparedStatement pstmt = con.prepareStatement(insertSQL,
								Statement.RETURN_GENERATED_KEYS);

								// Set request parameters and execute request
						pstmt.setString(1, getName());
						pstmt.setString(2, getAddress());
						pstmt.setString(3, getLogin());
						pstmt.setString(4, getPassword());
						pstmt.executeUpdate();

						/*// Get the id of the tight set
						ResultSet rs = pstmt.getGeneratedKeys();
						if (rs.next()) {
							setId(rs.getInt(1));
						}

						rs.close();*/
						pstmt.close();
					} else {
		                                                             System.out.println("JAJasdasdasA");
						// If an ID already exists, make an update ...
						String updateSQL = "UPDATE estateagent SET name = ?, address = ?, password = ? WHERE login = ?";
						PreparedStatement pstmt = con.prepareStatement(updateSQL);

						// Set request parameters
						pstmt.setString(1, getName());
						pstmt.setString(2, getAddress());
						pstmt.setString(3, getPassword());
						pstmt.setString(4, getLogin());
						pstmt.executeUpdate();

						pstmt.close();
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
	}
	/**
	 Delete EstateAgent in the database.
	 */
	public void delete() {
		// Get connected
		Connection con = DB2ConnectionManager.getInstance().getConnection();

		try {
			// Check if the Agent already exists
			if (getLogin() == "") {

				System.out.println("This agent is not in the database.");
			} else {
				// If an ID already exists, delete it
				String deleteSQL = "DELETE FROM estateagent WHERE login = ?";
				PreparedStatement pstmt = con.prepareStatement(deleteSQL);


				// Set request parameters

				pstmt.setString(1, getLogin());
				pstmt.executeUpdate();

				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
