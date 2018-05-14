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

CREATE TABLE TENANCYCONTRACT(
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
    start_date DATE,
    duration INTEGER,
    additional_costs INTEGER,
    person_id INTEGER NOT NULL,
    apartment_id INTEGER UNIQUE NOT NULL,
    FOREIGN KEY(id) REFERENCES Contract(id) ON DELETE CASCADE,
    FOREIGN KEY(person_id) REFERENCES Person(id) ON DELETE CASCADE,
    FOREIGN KEY(apartment_id) REFERENCES Apartment(id) ON DELETE CASCADE
);
 
 */
public class TenancyContract extends Contract{
    private Date start_date;
    private int duration;
    private int additional_costs;


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
    public int getAdditionalCosts() {
        return additional_costs;
    }

    public void setAdditionalCosts(int additional_costs) {
        this.additional_costs = additional_costs;
    }


    /**
     Saves TenancyContract in the database. If no ID has yet been assigned,
     the enerated id is fetched from DB2 and passed to the model.
     */
    public void save() {
        // Get connected
        Connection con = DB2ConnectionManager.getInstance().getConnection();

        super.save();

        try {
            // Add a new element if the object does not already have an ID.
            if ( super.getId() == -1 ) {
                //Attention, here a parameter is given, so that later generated IDs are returned!
                String insertSQL = "INSERT INTO TenancyContract(start_date, duration, additional_costs) VALUES (?,?,?)";

                PreparedStatement pstmt = con.prepareStatement(insertSQL,
                                Statement.RETURN_GENERATED_KEYS);

                // Set request parameters and execute request
                pstmt.setDate(1, new java.sql.Date(getStartDate().getTime()));
                pstmt.setInt(2, getDuration());
                pstmt.setInt(3, getAdditionalCosts());
                pstmt.executeUpdate();

                // Get the id of the tight set
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                        super.setId(rs.getInt(1));
                }

                rs.close();
                pstmt.close();
            } else {
                // If an ID already exists, make an update ...
                String updateSQL = "UPDATE TenancyContract SET start_date = ?, duration = ?, additional_costs = ? HERE id = ?";
                PreparedStatement pstmt = con.prepareStatement(updateSQL);

                // Set request parameters
                pstmt.setDate(1, new java.sql.Date(getStartDate().getTime()));
                pstmt.setInt(2, getDuration());
                pstmt.setInt(3, getAdditionalCosts());
                pstmt.setInt(4, super.getId());
                pstmt.executeUpdate();

                pstmt.close();
            }
        } catch (SQLException e) {
                e.printStackTrace();
        }
    }


    public static TenancyContract load(int id) {
        // Hole Verbindung
        Connection con = DB2ConnectionManager.getInstance().getConnection();
        
        try {
            Contract c_sup = Contract.load(id);
            
            if( c_sup != null ){
                String selectSQL2 = "SELECT * FROM TenancyContract WHERE id = ?";
                PreparedStatement pstmt2 = con.prepareStatement(selectSQL2);
                pstmt2.setInt(1, id);

                // Check if the TenancyContract exists
                ResultSet rs2 = pstmt2.executeQuery();
                if (rs2.next()) {
                    TenancyContract c = new TenancyContract();
                    c.setId(id);
                    c.setDate(c_sup.getDate());
                    c.setPlace(c_sup.getPlace());
                    c.setStartDate(rs2.getDate("start_date"));
                    c.setDuration(rs2.getInt("duration"));
                    c.setAdditionalCosts(rs2.getInt("additional_costs"));

                    rs2.close();
                    pstmt2.close();
                    return c;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void print(){
        System.out.println("Contract number: " + getId());
        System.out.println("Date: " + getDate());
        System.out.println("Place: " + getPlace());
        System.out.println("Start Date: " + getStartDate());
        System.out.println("Duration: " + getDuration());
        System.out.println("Additional Cost: " + getAdditionalCosts());
        System.out.println();
    }
}
