package advocate;

import model.Prisoner;
import java.util.ArrayList;
import java.util.List;

public class Advocate {
    private int userId;
    private String name;
    private List<Prisoner> assignedPrisoners; //prisoners advocating for 

    public Advocate(int advocateId, String name) {
        this.userId = advocateId;
        this.name = name;
        this.assignedPrisoners = new ArrayList<>();
    }

    // adds prisoners incharge of
    void loadPrisoner(Prisoner prisoner) {
        this.assignedPrisoners.add(prisoner);
    }

   // returns all prisoners incharge of
    public List<Prisoner> getAssignedPrisoners() {
        return new ArrayList<>(assignedPrisoners); // Returns a copy
    }

    // Getters
    public int getAdvocateId() { return userId; }
    public String getName() { return name; }
}