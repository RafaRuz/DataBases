	package de.dis2011;

import de.dis2011.data.EstateAgent;
import de.dis2011.data.Estate;
import de.dis2011.data.Contract;
import de.dis2011.data.DB2ConnectionManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * Hauptklasse
 */
public class Main {

        static EstateAgent actualEstateAgent;

	/**
	 * Startet die Anwendung
	 */
	public static void main(String[] args) {

		showMainMenu();
	}

	/**
	 * Zeigt das Hauptmenü
	 */
	public static void showMainMenu() {
		//Menüoptionen
		final int MENU_AGENT = 0;
                //final int MENU_ESTATE = 1;
                //final int MENU_CONTRACT = 2;
                final int LOGIN = 1;
		final int QUIT = 2;

		//Erzeuge Menü
		Menu mainMenu = new Menu("Main Menu");
		mainMenu.addEntry("Agent Administration", MENU_AGENT);
                mainMenu.addEntry("Login", LOGIN);
                //mainMenu.addEntry("Estate Administration", MENU_ESTATE);
                //mainMenu.addEntry("Contract Management", MENU_CONTRACT);
		mainMenu.addEntry("Terminate", QUIT);

		//Verarbeite Eingabe
		while(true) {
			int response = mainMenu.show();

			switch(response) {
				case MENU_AGENT:
					showEstateAgentMenu();
					break;
                                //case MENU_ESTATE:
				//	showEstateMenu();
				//	break;
                                //case MENU_CONTRACT:
				//	showContractMenu();
				//	break;
                                case LOGIN:
                                    if( login() )   showLoggedAgentMenu();
                                    break;
				case QUIT:
					return;
			}
		}
	}

	/**
	 * Shows agent management
	 */
	public static void showEstateAgentMenu() {
		//Menüoptionen1
		final int NEW_AGENT = 0;
    final int UPDATE_AGENT = 1;
    final int DELETE_AGENT = 2;
		final int BACK = 3;

		//agentverwaltungsmenü
		Menu estateAgentMenu = new Menu("Agent Administration");
		estateAgentMenu.addEntry("New Agent", NEW_AGENT);
    estateAgentMenu.addEntry("Update Agent", UPDATE_AGENT);
		estateAgentMenu.addEntry("Delete Agent", DELETE_AGENT);
		estateAgentMenu.addEntry("Back to Main Menu", BACK);

		//Verarbeite Eingabe
		while(true) {
			int response = estateAgentMenu.show();

			switch(response) {
				case NEW_AGENT:
					newEstateAgent();
					break;

				case BACK:
					return;
			}
		}
	}

	/**
	 * Shows estate management
	 */
	public static void showEstateMenu() {
		//Menüoptionen1
		final int NEW_ESTATE = 0;
		final int UPDATE_ESTATE = 1;
    final int DELETE_ESTATE = 2;
		final int BACK = 3;

		//agentverwaltungsmenü
		Menu estateMenu = new Menu("Estate Administration");
		estateMenu.addEntry("New Estate", NEW_ESTATE);
		estateMenu.addEntry("Update Estate", UPDATE_ESTATE);
		estateMenu.addEntry("Delete Estate", DELETE_ESTATE);
		estateMenu.addEntry("Back to Main Menu", BACK);

		//Verarbeite Eingabe
		while(true) {
			int response = estateMenu.show();

			switch(response) {
				case NEW_ESTATE:
					newEstate();
					break;
			case UPDATE_ESTATE:
				updateEstate();
				break;
			case DELETE_ESTATE:
				deleteEstate(FormUtil.readInt("ID"));
				break;
				case BACK:
					return;
			}
		}
	}
	/**
	 * Shows Contract management
	 */
	public static void showContractMenu() {
		//Menüoptionen1
		final int NEW_CONTRACT = 0;
		final int SEE_CONTRACT = 1;
		final int INSERT_PERSON = 2;
		final int BACK = 3;

		//agentverwaltungsmenü
		Menu contractMenu = new Menu("Contract Administration");
		contractMenu.addEntry("New Contract", NEW_CONTRACT);
		contractMenu.addEntry("New Contract", SEE_CONTRACT);
		contractMenu.addEntry("Insert Person", INSERT_PERSON);
		contractMenu.addEntry("Back to Main Menu", BACK);

		//Verarbeite Eingabe
		while(true) {
			int response = contractMenu.show();

			switch(response) {
				case NEW_CONTRACT:
					newContract();
					break;
				case SEE_CONTRACT:
					seeContracts();
					break;
				case INSERT_PERSON:
					insertPerson();
					break;
				case BACK:
					return;
			}
		}
	}

        public static void showLoggedAgentMenu() {
		//Menüoptionen
                final int MENU_ESTATE = 0;
                final int MENU_CONTRACT = 1;
                final int LOGOUT = 2;
		final int QUIT = 3;

		//Erzeuge Menü
		Menu loggedAgentMenu = new Menu("Logged Agent Menu");
                loggedAgentMenu.addEntry("Estate Administration", MENU_ESTATE);
                loggedAgentMenu.addEntry("Contract Management", MENU_CONTRACT);
                loggedAgentMenu.addEntry("Logout", LOGOUT);

		//Verarbeite Eingabe
		while(true) {
			int response = loggedAgentMenu.show();

			switch(response) {
                                case MENU_ESTATE:
					showEstateMenu();
					break;
                                case MENU_CONTRACT:
					showContractMenu();
					break;
                                case LOGOUT:
                                        actualEstateAgent = null;
                                        return;
			}
		}
	}

	/**
	 * Creates a new agent after the user enters the appropriate data.
	 */
	public static void newEstateAgent() {
		EstateAgent m = new EstateAgent();

		m.setName(FormUtil.readString("Name"));
		m.setAddress(FormUtil.readString("Adresse"));
		m.setLogin(FormUtil.readString("Login"));
		m.setPassword(FormUtil.readString("Passwort"));
		m.save();

		System.out.println("Agent with ID "+m.getId()+" was generated.");
	}

    /**
	 * Updates a new agent after the user enters the appropriate data.
	 */
	public static void updateEstateAgent() {
            int id = FormUtil.readInt("Enter the ID from the Agent to modify");

            // Get connected
            Connection con = DB2ConnectionManager.getInstance().getConnection();

            try {
                    String selectSQL = "SELECT * FROM estateagent WHERE id = ?";
                    PreparedStatement pstmt = con.prepareStatement(selectSQL);

                    pstmt.setInt(1, id);

                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        EstateAgent m = EstateAgent.load(id);

                        System.out.println("Enter the new data:");

                        m.setName(FormUtil.readString("Name"));
                        m.setAddress(FormUtil.readString("Adresse"));
                        m.setLogin(FormUtil.readString("Login"));
                        m.setPassword(FormUtil.readString("Passwort"));
                        m.save();

                        System.out.println("Agent with ID "+m.getId()+" was updated.");
                    }
                    else System.out.println("Invalid Agent ID.");

            } catch (SQLException e) {
                    e.printStackTrace();
            }
	}

	/**
	 * Deletes an agent from the database.
	 */
	public static void deleteEstateAgent(int id) {
		EstateAgent m = EstateAgent.load(id);

		if( m != null ){
                    m.delete();
                    System.out.println("Agent with ID "+Integer.toString(id)+" was deleted.");
                }
                else System.out.println("Unexistent Agent ID.");
	}


	/**
	 * Creates a new estate after the user enters the appropriate data.
	 */
	public static void newEstate() {
		Estate e = new Estate();

		e.setCity(FormUtil.readString("City"));
		e.setPostalCode(FormUtil.readInt("Postal Code"));
		e.setStreet(FormUtil.readString("Street"));
		e.setStreetNumber(FormUtil.readInt("Street Number"));
		e.setSquareArea(FormUtil.readInt("Square Area"));
		e.save();

		System.out.println("Estate with ID "+e.getId()+" was generated.");
	}

	/**
 * Updates a estate after the user enters the appropriate data.
 */
	public static void updateEstate() {
						int id = FormUtil.readInt("Enter the ID from the Estate to modify");

						// Get connected
						Connection con = DB2ConnectionManager.getInstance().getConnection();

						try {
										String selectSQL = "SELECT * FROM estate WHERE estateid = ?";
										PreparedStatement pstmt = con.prepareStatement(selectSQL);

										pstmt.setInt(1, id);

										ResultSet rs = pstmt.executeQuery();
										if (rs.next()) {
												Estate e = Estate.load(id);

												System.out.println("Enter the new data:");

												e.setCity(FormUtil.readString("City"));
												e.setPostalCode(FormUtil.readInt("Postal Code"));
												e.setStreet(FormUtil.readString("Street"));
												e.setStreetNumber(FormUtil.readInt("Street Number"));
												e.setSquareArea(FormUtil.readInt("Square Area"));
												e.save();

												System.out.println("Estate with ID "+e.getId()+" was updated.");
										}
										else System.out.println("Invalid Estate ID.");

						} catch (SQLException e) {
										e.printStackTrace();
						}
	}
	/**
	* Deletes an estate from the database.
	*/
	public static void deleteEstate(int id) {
		Estate e = Estate.load(id);

		if( e != null ){
			e.delete();
			System.out.println("Estate with ID "+Integer.toString(id)+" was deleted.");
		}
		else System.out.println("Unexistent Estate ID.");
	}

	/**
	 * Creates a new contract after the user enters the appropriate data.
	 */
	public static void newContract() {
		Contract c = new Contract();

		c.setDate(FormUtil.readString("Date"));
		c.setPlace(FormUtil.readString("Place"));
		c.save();

		System.out.println("Contract with number "+c.getContractNumber()+" was generated.");
	}

	/**
	 * See all the contracts
	 *//*
	public static void seeContracts() {
		int id = FormUtil.readInt("Enter the ID from the Estate to modify");

		// Get connected
		Connection con = DB2ConnectionManager.getInstance().getConnection();

		try {
						String selectSQL = "SELECT * FROM contract";
						PreparedStatement pstmt = con.prepareStatement(selectSQL);

						pstmt.setInt(1, id);

						ResultSet rs = pstmt.executeQuery();
						if (rs.next()) {
								Estate e = Estate.load(id);

								System.out.println("Enter the new data:");

								e.setCity(FormUtil.readString("City"));
								e.setPostalCode(FormUtil.readInt("Postal Code"));
								e.setStreet(FormUtil.readString("Street"));
								e.setStreetNumber(FormUtil.readInt("Street Number"));
								e.setSquareArea(FormUtil.readInt("Square Area"));
								e.save();

								System.out.println("Estate with ID "+e.getId()+" was updated.");
						}
						else System.out.println("Invalid Estate ID.");

		} catch (SQLException e) {
						e.printStackTrace();
		}
	}
*/
	/**
	 * Creates a new contract after the user enters the appropriate data.
	 */
/*	public static void insertPerson() {
		Contract c = new Contract();

		System.out.println("Contract with number "+c.getContractNumber()+" was generated.");
	}*/

        /**
	 * Ask for an username and password and try to log in
	 */
	public static boolean login() {
                System.out.println("Please enter the username:");
                String username = "";

                username = FormUtil.readString("Name");

                // Get connected
		Connection con = DB2ConnectionManager.getInstance().getConnection();

		try {
                        String selectByLoginSQL = "SELECT * FROM estateagent WHERE login = ?";
                        PreparedStatement pstmt = con.prepareStatement(selectByLoginSQL);

                        pstmt.setString(1, username);

                        ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
                                String password = "";

                                password = FormUtil.readString("Password");

                                if( password.equals(rs.getString("password")) ){
                                    EstateAgent ts = new EstateAgent();
                                    ts.setId(rs.getInt("id"));
                                    ts.setName(rs.getString("name"));
                                    ts.setAddress(rs.getString("address"));
                                    ts.setLogin(rs.getString("login"));
                                    ts.setPassword(rs.getString("password"));

                                    rs.close();
                                    pstmt.close();
                                    actualEstateAgent = ts;

                                    System.out.println("Login successful.");
                                    return true;
                                }
                                else{
                                    System.out.println("Incorrect password.");
                                    return false;
                                }
			}
                        else{
                            System.out.println("Invalid username.");
                            return false;
                        }

                } catch (SQLException e) {
			e.printStackTrace();
		}


		return false;
	}

}
