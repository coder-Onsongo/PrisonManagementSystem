package visitor;

public class VisitManager {//srp each class hass their own logic, logic for creating a visit
    public VisitManager(){}
    public static Visit bookVisit(int visitorId, int prisonerId, String date, String time) {
        
        if (date.isEmpty() || time.isEmpty()) {
            System.out.println("Error: Date and time cannot be empty.");
            return null;
        }
        
        Visit newVisit = new Visit(date, time, prisonerId , visitorId);
        System.out.println("Visit successfully created for Prisoner ID: " + prisonerId);
        
        return newVisit;
    }
}

