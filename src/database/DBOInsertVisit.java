package database;

public interface DBOInsertVisit {
    public boolean bookNewVisit(int visitorId, int prisonerId, String date, String time);
}