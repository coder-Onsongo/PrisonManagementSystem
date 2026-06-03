package database;

import interfaces.DBOLogin;
import model.Prisoner;
import visitor.Visit;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import interfaces.DBOInsertVisit;
import interfaces.DBOSelectVisits;
import interfaces.AdvocateOperations;


public class DBOpperationImpl implements DBOLogin, AdvocateOperations, DBOInsertVisit,  DBOSelectVisits {
    private DBConnection dbc = new DBConnection();

// advocate view their prisoners
    @Override
    public List<Prisoner> getPrisonersForAdvocate(int advocateId) {
        List<Prisoner> list = new ArrayList<>();
        String query = "SELECT p.prisonerid, p.name, p.crime, p.sentencedurationmonths " +
                       "FROM prisoners p " +
                       "JOIN advocate_prisoner ap ON p.prisonerid = ap.prisonerid " +
                       "WHERE ap.advocateid = ?";
        try (PreparedStatement pst = dbc.con.prepareStatement(query)) {
            pst.setInt(1, advocateId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(new Prisoner(
                        rs.getInt("prisonerid"),
                        rs.getString("name"),
                        rs.getString("crime"),
                        rs.getInt("sentencedurationmonths"),
                        "Cell B-1"
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("DB Error fetching advocate prisoners: " + e.getMessage());
        }
        return list;
    }

    //visitor previous visits
    @Override
    public List<Visit> getVisitHistoryForVisitor(int visitorId) {
        List<Visit> list = new ArrayList<>();
        String query = "SELECT visitid, visitdate, visittime, prisonerid, visitorid, staffid, status " +
                       "FROM visits WHERE visitorid = ? ORDER BY visitid DESC";
        try (PreparedStatement pst = dbc.con.prepareStatement(query)) {
            pst.setInt(1, visitorId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(new Visit(
                        rs.getInt("visitid"),
                        rs.getString("visitdate"),
                        rs.getString("visittime"),
                        rs.getInt("prisonerid"),
                        rs.getInt("visitorid"),
                        rs.getInt("staffid"),
                        rs.getString("status")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("DB Error fetching visit history: " + e.getMessage());
        }
        return list;
    }

    // visitor book visits db
    @Override
    public boolean bookNewVisit(int visitorId, int prisonerId, String date, String time) {
        String query = "INSERT INTO visits (visitdate, visittime, prisonerid, visitorid, staffid, status) " +
                       "VALUES (?, CAST(? AS time), ?, ?, NULL, 'PENDING')";
        try (PreparedStatement pst = dbc.con.prepareStatement(query)) {
            pst.setDate(1, Date.valueOf(date));
            pst.setString(2, time);
            pst.setInt(3, prisonerId);
            pst.setInt(4, visitorId);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("DB Error booking visit: " + e.getMessage());
            return false;
        }
    }

    // 💡 MOVE ALL SQL QUERIES FROM LOGIN HERE
    @Override
    public Object authenticateUser(int userId, String password) {
        String query = "SELECT role, name FROM systemusers WHERE userid = ? AND password = ?";
        
        try (PreparedStatement pst = dbc.con.prepareStatement(query)) {
            pst.setInt(1, userId);
            pst.setString(2, password);
            
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String databaseRole = rs.getString("role");
                    String realName = rs.getString("name"); 
                    
                    if ("VISITOR".equalsIgnoreCase(databaseRole)) {
                        int linkedPrisonerId = 1; // Default fallback
                        
                        // Visitor secondary target prisoner query
                        String lookupQuery = "SELECT prisonerid FROM visits WHERE visitorid = ? LIMIT 1";
                        try (PreparedStatement pstLookup = dbc.con.prepareStatement(lookupQuery)) {
                            pstLookup.setInt(1, userId);
                            try (ResultSet rsLookup = pstLookup.executeQuery()) {
                                if (rsLookup.next()) {
                                    linkedPrisonerId = rsLookup.getInt("prisonerid");
                                }
                            }
                        }
                        // Instantiates and returns the exact visitor type
                        return new visitor.Visitor(userId, realName, password, linkedPrisonerId);
                        
                    } else if ("ADVOCATE".equalsIgnoreCase(databaseRole)) {
                        // Instantiates and returns the exact advocate type
                        return new advocate.Advocate(userId, realName, password);
                    }
                    
                    // Add Guard instantiation here later if you build a Guard class
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Authentication Error: " + e.getMessage());
        }
        return null; // Denied access or exception caught
    }
}