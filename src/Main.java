import model.Prisoner;
import model.VisitRequest;
import service.PrisonerService;
import service.VisitRequestService;

public class Main {
    public static void main(String[] args) {

        PrisonerService prisonerService = new PrisonerService();
        VisitRequestService visitRequestService = new VisitRequestService();

        Prisoner prisoner1 = new Prisoner(1, "John Kamau", "Robbery", 5, "A12");
        Prisoner prisoner2 = new Prisoner(2, "Peter Otieno", "Fraud", 3, "B07");

        prisonerService.addPrisoner(prisoner1);
        prisonerService.addPrisoner(prisoner2);

        prisonerService.viewAllPrisoners();

        prisonerService.updatePrisonerDetails(1, "Armed Robbery", 7, "C03");

        prisonerService.viewAllPrisoners();

        VisitRequest request1 = new VisitRequest(1, "Mary Wanjiku", 1, "Pending");
        VisitRequest request2 = new VisitRequest(2, "Brian Otieno", 2, "Pending");

        visitRequestService.addVisitRequest(request1);
        visitRequestService.addVisitRequest(request2);

        visitRequestService.viewAllVisitRequests();

        visitRequestService.approveRequest(1);
        visitRequestService.rejectRequest(2);

        visitRequestService.viewAllVisitRequests();
    }
}