package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Date;


import de.dis2011.data.DB2ConnectionManager;

/**
 * PurchaseContract-Be
 *
 * Beispiel-Tabelle:
f
 CREATE TABLE PurchaseContract(contract_number INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
 installments INTEGER,
 interest INTEGER),
 FOREIGN KEY(contract_number) REFERENCES Contract(contract_number)
 );
 */
public class PurchaseContract extends Contract{
	private int installments;
	private int interest;


	public int getInstallments() {
		return installments;
	}
	public void setInstallments(int installments) {
		this.installments = installments;
	}
	public int getInterest() {
		return interest;
	}

	public void setInterest(int interest) {
		this.interest = interest;
	}




}
