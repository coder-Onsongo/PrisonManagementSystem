package gui;

import model.Prisoner;
import advocate.Advocate;
import database.DBOperations; 

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.List;

public class AdvocateDashboard {

    // instantiating opperations object to perform opperations 
    private DBOperations dbOps = new DBOperations();
    private Advocate currentAdvocate; 

    public AdvocateDashboard(Advocate advocate) {
        this.currentAdvocate = advocate;
    }

    public void show(Stage stage) {
        stage.setTitle("Prison Management System - Advocate Dashboard");

        VBox root = new VBox();
        root.setSpacing(20);
        root.setPadding(new Insets(30, 30, 30, 30));
        root.setAlignment(Pos.TOP_CENTER);

        Label lblTitle = new Label("Secure Prisoner Roster");
        lblTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 24));

        TableView<Prisoner> table = new TableView<>();
        TableColumn<Prisoner, Integer> colId = new TableColumn<>("Prisoner ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("prisonerId"));
        colId.setMinWidth(100);

        TableColumn<Prisoner, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colName.setMinWidth(150);

        TableColumn<Prisoner, String> colCrime = new TableColumn<>("Charge");
        colCrime.setCellValueFactory(new PropertyValueFactory<>("offence"));
        colCrime.setMinWidth(150);

        TableColumn<Prisoner, Integer> colSentence = new TableColumn<>("Sentence Duration (Months)");
        colSentence.setCellValueFactory(new PropertyValueFactory<>("sentenceYears"));
        colSentence.setMinWidth(120);

        table.getColumns().addAll(colId, colName, colCrime, colSentence);
        table.setPrefHeight(300);

        //FETCH CLEAN LIST
        List<Prisoner> prisoners = dbOps.getPrisonersForAdvocate(currentAdvocate.getAdvocateId());
        
        for (Prisoner p : prisoners) {
            currentAdvocate.loadPrisoner(p); // Loading directly into domain logic model
        }

        // Display prisoners collections
        table.getItems().addAll(currentAdvocate.getAssignedPrisoners());

        // Logout Button
        Button btnLogout = new Button("Log Out");
        btnLogout.setPrefSize(120, 35);
        btnLogout.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        btnLogout.setOnAction(e -> {
            Login login = new Login();
            login.start(stage);
        });

        root.getChildren().addAll(lblTitle, table, btnLogout);

        Scene scene = new Scene(root, 600, 500);
        stage.setScene(scene);
        stage.show();
    }
}