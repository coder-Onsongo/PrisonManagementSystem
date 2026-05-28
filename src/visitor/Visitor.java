package visitor;

public class Visitor {
    private int userId;
    private String name;
    private int targetPrisonerId; // 1:1 relationship, one visitor for one prisoner

    public Visitor(int visitorId, String name, int targetPrisonerId) {
        this.userId = visitorId;
        this.name = name;
        this.targetPrisonerId = targetPrisonerId;
    }
// making a visit
    public Visit requestVisit(String date, String time) {
        return VisitManager.bookVisit(this.userId,this.targetPrisonerId, date, time);
    }
    
   public void viewMyVisitStatus(Visit currentVisit) {
        VisitManager.viewVisitDetails(currentVisit);
    }
    
    // Getters
    public int getVisitorId() { return userId; }
    public String getName() { return name; }
    public int getTargetPrisonerId() { return targetPrisonerId; }
}
