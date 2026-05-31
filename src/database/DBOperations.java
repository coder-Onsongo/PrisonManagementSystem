package database;

import model.Prisoner;
import visitor.Visit;
import java.util.List;

// 💡 Importing your custom feature interfaces
import advocate.AdvocateOperations;
import visitor.DBOInsertVisit;
import visitor.DBOSelectVisits;

public class DBOperations implements AdvocateOperations, DBOInsertVisit, DBOSelectVisits {

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
        // FIXED: Changed return type to List<Prisoner> and routed to getPrisonersForAdvocate
        return implementation.getPrisonersForAdvocate(advocateId);
    }

    

    

    
}