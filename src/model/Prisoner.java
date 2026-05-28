package model;

public class Prisoner {
    private int prisonerId;
    private String fullName;
    private String offence;
    private int sentenceYears;
    private String cellNumber;

    public Prisoner(int prisonerId, String fullName, String offence, int sentenceYears, String cellNumber) {
        this.prisonerId = prisonerId;
        this.fullName = fullName;
        this.offence = offence;
        this.sentenceYears = sentenceYears;
        this.cellNumber = cellNumber;
    }

    public int getPrisonerId() {
        return prisonerId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getOffence() {
        return offence;
    }

    public int getSentenceYears() {
        return sentenceYears;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setOffence(String offence) {
        this.offence = offence;
    }

    public void setSentenceYears(int sentenceYears) {
        this.sentenceYears = sentenceYears;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }
}