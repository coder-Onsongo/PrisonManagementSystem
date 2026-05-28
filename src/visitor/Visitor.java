package visitor;

public class Visitor {
    private int visitorId;
    private String name;
    private int targetPrisonerId; // 1:1 relationship, one visitor for one prisoner

    public Visitor(int visitorId, String name, int targetPrisonerId) {
        this.visitorId = visitorId;
        this.name = name;
        this.targetPrisonerId = targetPrisonerId;
    }
// making a visit
    public Visit requestVisit(String date, String time) {
        return VisitManager.bookVisit(this.visitorId,this.targetPrisonerId, date, time);
    }
    
    // Getters
    public int getVisitorId() { return visitorId; }
    public String getName() { return name; }
    public int getTargetPrisonerId() { return targetPrisonerId; }
}
