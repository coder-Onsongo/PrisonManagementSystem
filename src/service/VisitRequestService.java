package service;

import model.VisitRequest;
import java.util.ArrayList;

public class VisitRequestService {

    private ArrayList<VisitRequest> visitRequests = new ArrayList<>();

    public void addVisitRequest(VisitRequest request) {
        visitRequests.add(request);
    }

    public void viewAllVisitRequests() {
        if (visitRequests.isEmpty()) {
            System.out.println("No visit requests found.");
        } else {
            for (VisitRequest request : visitRequests) {
                System.out.println("----------------------");
                System.out.println("Request ID: " + request.getRequestId());
                System.out.println("Visitor Name: " + request.getVisitorName());
                System.out.println("Prisoner ID: " + request.getPrisonerId());
                System.out.println("Status: " + request.getStatus());
            }
        }
    }

    public void approveRequest(int requestId) {
        for (VisitRequest request : visitRequests) {
            if (request.getRequestId() == requestId) {
                request.setStatus("Approved");
                System.out.println("Visit request approved successfully.");
                return;
            }
        }

        System.out.println("Visit request not found.");
    }

    public void rejectRequest(int requestId) {
        for (VisitRequest request : visitRequests) {
            if (request.getRequestId() == requestId) {
                request.setStatus("Rejected");
                System.out.println("Visit request rejected successfully.");
                return;
            }
        }

        System.out.println("Visit request not found.");
    }
}