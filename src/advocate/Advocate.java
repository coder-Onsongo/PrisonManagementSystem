package advocate;

import model.Prisoner;
import java.util.ArrayList;
import java.util.List;

public class Advocate {
   private int userId;
    private String name;
    private String password; 
    private List<Prisoner> assignedPrisoners; 

    public Advocate(int userId, String name, String password) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.assignedPrisoners = new ArrayList<>();
    }

    // adds prisoners incharge of
    public void loadPrisoner(Prisoner prisoner) {
        this.assignedPrisoners.add(prisoner);
    }

   // returns all prisoners incharge of
    public List<Prisoner> getAssignedPrisoners() {
        return new ArrayList<>(assignedPrisoners); // Returns a copy
    }

    // Getters
    public int getAdvocateId() { return userId; }
    public String getName() { return name; }
    public String getPassword() { return password; }
}