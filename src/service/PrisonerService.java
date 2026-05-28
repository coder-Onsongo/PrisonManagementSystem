package service;

import model.Prisoner;
import java.util.ArrayList;

public class PrisonerService {

    private ArrayList<Prisoner> prisoners = new ArrayList<>();

    public void addPrisoner(Prisoner prisoner) {
        prisoners.add(prisoner);
        System.out.println("Prisoner added successfully: " + prisoner.getFullName());
    }

    public void viewAllPrisoners() {
        if (prisoners.isEmpty()) {
            System.out.println("No prisoners found.");
        } else {
            for (Prisoner prisoner : prisoners) {
                System.out.println("----------------------");
                System.out.println("ID: " + prisoner.getPrisonerId());
                System.out.println("Name: " + prisoner.getFullName());
                System.out.println("Offence: " + prisoner.getOffence());
                System.out.println("Sentence Years: " + prisoner.getSentenceYears());
                System.out.println("Cell Number: " + prisoner.getCellNumber());
            }
        }
    }

    public void updatePrisonerDetails(int prisonerId, String newOffence, int newSentenceYears, String newCellNumber) {
        for (Prisoner prisoner : prisoners) {
            if (prisoner.getPrisonerId() == prisonerId) {
                prisoner.setOffence(newOffence);
                prisoner.setSentenceYears(newSentenceYears);
                prisoner.setCellNumber(newCellNumber);

                System.out.println("Prisoner details updated successfully.");
                return;
            }
        }

        System.out.println("Prisoner not found.");
    }
}