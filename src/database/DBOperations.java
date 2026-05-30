package database;

public class DBOperations {

    private final DBOpperationImpl implementation;

    public DBOperations() {
        this.implementation = new DBOpperationImpl();
    }

    public boolean fileVisitRequest(int visitorId, int prisonerId, String date, String time) {
        return implementation.fileVisitRequest(visitorId, prisonerId, date, time);
    }

    public void viewMyVisits(int visitorId) {
        implementation.viewMyVisits(visitorId);
    }

    public void viewMyAssignedPrisoners(int advocateId) {
        implementation.viewMyAssignedPrisoners(advocateId);
    }
}