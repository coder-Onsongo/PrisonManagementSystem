package visitor;

public interface DBOInsertVisitor {
    boolean fileVisitRequest(int visitorId, int prisonerId, String date, String time);
}