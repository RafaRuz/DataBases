package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.dis2011.data.DB2ConnectionManager;

/**
 * Apartament-Bean ////////////////////////////HAY QUE HACERLA

 CREATE TABLE Apartament(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
  	floor INTEGER,
		rent INTEGER,
  	rooms INTEGER,
  	balcony SMALLINT,
	kitchen SMALLINT,
	FOREIGN KEY(id) REFERENCES Estate(id)
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

	public boolean getBalcony(){
		return balcony;
	}

	public void setBalcony(boolean balcony){
		this.balcony = balcony;
	}

	public boolean getKitchen(){
		return kitchen;
	}

	public void setKitchen(boolean kitchen){
		this.kitchen = kitchen;
	}


}
