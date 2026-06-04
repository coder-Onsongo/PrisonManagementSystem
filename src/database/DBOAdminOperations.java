package database;

import java.util.List;

public interface DBOAdminOperations {
    
    // --- System Users Management Methods ---
    
    // View all system users
    List<String> viewAllSystemUsers();
    
    // Add a new system user 
    boolean addSystemUser(int userId, String name, String password, String role);
    
    // Alter/Update an existing system user's details
    boolean alterSystemUser(int userId, String newName, String newPassword, String newRole);
    
    // Delete a system user from the database
    boolean deleteSystemUser(int userId);

    
    // --- Prisoners Management Methods ---
    
    // View all prisoners currently tracked in the system
    List<String> viewAllPrisoners();
    
    // Add a brand new prisoner record
    boolean addPrisoner(int prisonerId, String name, String crime, int sentenceMonths);
    
    // Alter/Update an existing prisoner record
    boolean alterPrisoner(int prisonerId, String newName, String newCrime, int newSentenceMonths);
    
    // Delete a prisoner record permanently
    boolean deletePrisoner(int prisonerId);
}