package database;

public class Test {
    // Added the missing String[] args parameter here
    public static void main(String[] args) {
        System.out.println("=== INITIALIZING SYSTEM TESTING ===");
        
        // Instantiate the DIP wrapper class
        DBOperations dbOps = new DBOperations();
        
        // Dummy IDs matching your live PostgreSQL database data
        int mockVisitorId = 10;
        int mockPrisonerId = 1;
        int mockAdvocateId = 20;

        System.out.println("\n--- RUNNING TEST 1: Visitor Filing a Visit Request ---");
        String testDate = "2026-06-15";
        String testTime = "14:30:00";
        
        // Calls pass through the wrapper to the underlying separated interfaces cleanly
        boolean requestSuccess = dbOps.fileVisitRequest(mockVisitorId, mockPrisonerId, testDate, testTime);
        if (requestSuccess) {
            System.out.println("SUCCESS: Visit request successfully added to PostgreSQL!");
        } else {
            System.out.println("FAILURE: Visit request insertion failed. Check your foreign key IDs.");
        }

        System.out.println("\n--- RUNNING TEST 2: Visitor Fetching History & Statuses ---");
        dbOps.viewMyVisits(mockVisitorId);

        System.out.println("\n--- RUNNING TEST 3: Advocate Checking Secure Roster ---");
        dbOps.viewMyAssignedPrisoners(mockAdvocateId);

        System.out.println("=== TESTING SEQUENCE COMPLETED ===");
    }
}