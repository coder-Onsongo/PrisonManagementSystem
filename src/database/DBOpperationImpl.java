package database;

import model.Prisoner;
import visitor.Visit;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import visitor.DBOInsertVisit;
import visitor.DBOSelectVisits;
import advocate.AdvocateOperations;


public class DBOpperationImpl implements AdvocateOperations, DBOInsertVisit,  DBOSelectVisits {
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
}