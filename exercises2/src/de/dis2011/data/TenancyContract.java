package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Date;


import de.dis2011.data.DB2ConnectionManager;

/**
 * TenancyContract-Bean
 *
 * Beispiel-Tabelle:

 CREATE TABLE TenancyContract(contract_number INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
 contract_start_date date,
 place varchar(255),
 FOREIGN KEY(contract_number) REFERENCES Contract(contract_number)
 );
 */
public class TenancyContract extends Contract{
	private Date start_date;
	private int duration;
	private int aditional_cost;


	public Date getStartDate(){
		return start_date;
	}
	public void setStartDate(Date start_date){
		this.start_date = start_date;
	}

	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getAditionalCost() {
		return aditional_cost;
	}

	public void setAditionalCost(int aditional_cost) {
		this.aditional_cost = aditional_cost;
	}




}
