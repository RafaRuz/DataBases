	package de.dis2011;

import de.dis2011.data.EstateAgent;
import de.dis2011.data.Estate;
import de.dis2011.data.Contract;



/**
 * Hauptklasse
 */
public class Main {
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
                final int MENU_ESTATE = 1;
                final int MENU_CONTRACT = 2;
		final int QUIT = 3;

		//Erzeuge Menü
		Menu mainMenu = new Menu("Main Menu");
		mainMenu.addEntry("Agent Administration", MENU_AGENT);
                mainMenu.addEntry("Estate Administration", MENU_ESTATE);
                mainMenu.addEntry("Contract Management", MENU_CONTRACT);
		mainMenu.addEntry("Terminate", QUIT);

		//Verarbeite Eingabe
		while(true) {
			int response = mainMenu.show();

			switch(response) {
				case MENU_AGENT:
					showEstateAgentMenu();
					break;
        case MENU_ESTATE:
					showEstateMenu();
					break;
        case MENU_CONTRACT:
					showContractMenu();
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
                final int DELETE_AGENT = 1;
		final int BACK = 2;

		//agentverwaltungsmenü
		Menu estateAgentMenu = new Menu("Agent Administration");
		estateAgentMenu.addEntry("New Agent", NEW_AGENT);
		estateAgentMenu.addEntry("Delete Agent", DELETE_AGENT);
		estateAgentMenu.addEntry("Back to Main Menu", BACK);

		//Verarbeite Eingabe
		while(true) {
			int response = estateAgentMenu.show();

			switch(response) {
				case NEW_AGENT:
					newEstateAgent();
					break;
				case DELETE_AGENT:
					deleteEstateAgent(FormUtil.readInt("ID"));
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
		final int BACK = 1;

		//agentverwaltungsmenü
		Menu estateMenu = new Menu("Estate Administration");
		estateMenu.addEntry("New Estate", NEW_ESTATE);
		estateMenu.addEntry("Back to Main Menu", BACK);

		//Verarbeite Eingabe
		while(true) {
			int response = estateMenu.show();

			switch(response) {
				case NEW_ESTATE:
					newEstate();
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
		final int BACK = 1;

		//agentverwaltungsmenü
		Menu contractMenu = new Menu("Contract Administration");
		contractMenu.addEntry("New Contract", NEW_CONTRACT);
		contractMenu.addEntry("Back to Main Menu", BACK);

		//Verarbeite Eingabe
		while(true) {
			int response = contractMenu.show();

			switch(response) {
				case NEW_CONTRACT:
					newContract();
					break;
				case BACK:
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
	 * Delete an agent from the database.
	 */
	public static void deleteEstateAgent(int id) {
		EstateAgent m = new EstateAgent();

		m.load(id).delete();;


		System.out.println("Agent with ID "+m.load(id).getId()+" was deleted.");
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
	 * Creates a new contract after the user enters the appropriate data.
	 */
	public static void newContract() {
		Contract c = new Contract();

		c.setDate(FormUtil.readString("Date"));
		c.setPlace(FormUtil.readString("Place"));
		c.save();

		System.out.println("Contract with number "+c.getContractNumber()+" was generated.");
	}

}
