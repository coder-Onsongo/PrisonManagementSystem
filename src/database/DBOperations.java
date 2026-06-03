package database;

import interfaces.DBOLogin;
import model.Prisoner;
import visitor.Visit;
import java.util.List;

// 💡 Importing your custom feature interfaces
import interfaces.AdvocateOperations;
import interfaces.DBOInsertVisit;
import interfaces.DBOSelectVisits;

public class DBOperations implements DBOLogin, AdvocateOperations, DBOInsertVisit, DBOSelectVisits {

    private final DBOpperationImpl implementation;

    public DBOperations() {
        this.implementation = new DBOpperationImpl();
    }

    // implements visitor booking  interface logic
   @Override
    public boolean bookNewVisit(int visitorId, int prisonerId, String date, String time) {
        return implementation.bookNewVisit(visitorId, prisonerId, date, time);
    }

    // implements select all operations from interface
   @Override
    public List<Visit> getVisitHistoryForVisitor(int visitorId) {
        return implementation.getVisitHistoryForVisitor(visitorId);
    }

    // interface impl that getting prisoners logic from other classes 
   @Override
    public List<Prisoner> getPrisonersForAdvocate(int advocateId) {
        return implementation.getPrisonersForAdvocate(advocateId);
    }  

    @Override
    public Object authenticateUser(int userId, String password) {
        return implementation.authenticateUser(userId, password);
    }
}