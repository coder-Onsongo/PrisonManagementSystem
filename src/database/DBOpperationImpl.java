package database;

import java.sql.*;
import visitor.DBOInsertVisitor;
import visitor.DBOSelectVisitor;
import advocate.AdvocateOperations;

public class DBOpperationImpl implements DBOInsertVisitor, DBOSelectVisitor, AdvocateOperations {
    
    public DBConnection dbc;

    public DBOpperationImpl() {
        this.dbc = new DBConnection();
    }
    
    // 1. VISITOR: Book a New Visit 
    @Override
    public boolean fileVisitRequest(int visitorId, int prisonerId, String date, String time) {
        String query = "INSERT INTO visits (visitdate, visittime, prisonerid, visitorid, status) " +
                       "VALUES (CAST(? AS date), CAST(? AS time), ?, ?, 'PENDING')";
        
        try (PreparedStatement pst = dbc.con.prepareStatement(query)) {
            pst.setString(1, date);       
            pst.setString(2, time);        
            pst.setInt(3, prisonerId);
            pst.setInt(4, visitorId);
            
            int rowsInserted = pst.executeUpdate();
            return rowsInserted > 0;
            
        } catch (SQLException e) {
            System.err.println("SQL Error while filing visit request: " + e.getMessage());
            return false;
        }
    }

    // 2. VISITOR :View Personal Visit History & Statuses
    @Override
    public void viewMyVisits(int visitorId) {
        String query = "SELECT visitid, visitdate, visittime, prisonerid, staffid, status " +
                       "FROM visits WHERE visitorid = ? ORDER BY visitdate DESC";
                       
        try (PreparedStatement pst = dbc.con.prepareStatement(query)) {
            pst.setInt(1, visitorId);
            
            try (ResultSet rs = pst.executeQuery()) {
                System.out.println("=== MY FILED VISIT REQUESTS ===");
                boolean hasRecords = false;
                
                while (rs.next()) {
                    hasRecords = true;
                    int visitId = rs.getInt("visitid");
                    String date = rs.getString("visitdate");
                    String time = rs.getString("visittime");
                    int prisonerId = rs.getInt("prisonerid");
                    int staffId = rs.getInt("staffid"); 
                    String status = rs.getString("status");
                    
                    System.out.print("Visit ID: " + visitId + " | Date: " + date + " | Time: " + time);
                    System.out.print(" | Prisoner ID: " + prisonerId + " | Status: [" + status + "]");
                    if (staffId != 0) {
                        System.out.println(" | Reviewed By Guard ID: " + staffId);
                    } else {
                        System.out.println(" | Awaiting Guard Action");
                    }
                }
                if (!hasRecords) {
                    System.out.println("No visit requests filed under this account.");
                }
                System.out.println("=================================");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error while fetching visitor history: " + e.getMessage());
        }
    }

    // 3. ADVOCATE :View Only Assigned Prisoners
    @Override
    public void viewMyAssignedPrisoners(int advocateId) {
        String query = "SELECT p.prisonerid, p.name, p.crime, p.sentencedurationmonths " +
                       "FROM prisoners p " +
                       "JOIN advocate_prisoner ap ON p.prisonerid = ap.prisonerid " +
                       "WHERE ap.advocateid = ?";
                       
        try (PreparedStatement pst = dbc.con.prepareStatement(query)) {
            pst.setInt(1, advocateId);
            
            try (ResultSet rs = pst.executeQuery()) {
                System.out.println("=== ADVOCATE PANEL: SECURE PRISONER ROSTER ===");
                boolean hasPrisoners = false;
                
                while (rs.next()) {
                    hasPrisoners = true;
                    int id = rs.getInt("prisonerid");
                    String name = rs.getString("name");
                    String crime = rs.getString("crime");
                    int months = rs.getInt("sentencedurationmonths");
                    
                    System.out.println("ID: " + id + " | Name: " + name + " | Charge: " + crime + " | Sentence: " + months + " Months");
                }
                if (!hasPrisoners) {
                    System.out.println("No prisoners are currently assigned to your profile.");
                }
                System.out.println("==============================================");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error while loading advocate roster: " + e.getMessage());
        }
    }
    
}