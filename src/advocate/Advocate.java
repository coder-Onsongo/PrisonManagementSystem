package advocate;

import java.util.ArrayList;
import java.util.List;

public class Advocate {
    private int advocateId;
    private String name;
    private List<Prisoner> assignedPrisoners; //prisoners advocating for 

    public Advocate(int advocateId, String name) {
        this.advocateId = advocateId;
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
    public int getAdvocateId() { return advocateId; }
    public String getName() { return name; }
}