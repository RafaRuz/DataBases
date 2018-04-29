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
 CREATE TABLE House(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
  	floors INTEGER,
		price INTEGER,
  	garden SMALLINT,
	FOREIGN KEY(id) REFERENCES Estate(id)
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

	public boolean getGarden(){
		return garden;
	}

	public void setGarden(boolean garden){
		this.garden = garden;
	}


}
