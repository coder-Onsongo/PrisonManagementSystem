package model;

public class VisitRequest {

    private int requestId;
    private String visitorName;
    private int prisonerId;
    private String status;

    public VisitRequest(int requestId, String visitorName, int prisonerId, String status) {
        this.requestId = requestId;
        this.visitorName = visitorName;
        this.prisonerId = prisonerId;
        this.status = status;
    }

    public int getRequestId() {
        return requestId;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public int getPrisonerId() {
        return prisonerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}