package database;
//visitor to book an ew visit 
public interface DBOInsertVisit {
    public boolean bookNewVisit(int visitorId, int prisonerId, String date, String time);
}