package gui;

import model.Prisoner;
import advocate.Advocate;
import database.DBConnection;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.sql.*;

public class AdvocateDashboard {

    private DBConnection dbc = new DBConnection();
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

        //tabel view for prisoner
        TableView<Prisoner> table = new TableView<>();
        TableColumn<Prisoner, Integer> colId = new TableColumn<>("Prisoner ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("prisonerId")); // Calls getPrisonerId()
        colId.setMinWidth(100);

        TableColumn<Prisoner, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName")); // Calls getFullName()
        colName.setMinWidth(150);

        TableColumn<Prisoner, String> colCrime = new TableColumn<>("Charge");
        colCrime.setCellValueFactory(new PropertyValueFactory<>("offence")); // Calls getOffence()
        colCrime.setMinWidth(150);

        TableColumn<Prisoner, Integer> colSentence = new TableColumn<>("Sentence (Months)");
        colSentence.setCellValueFactory(new PropertyValueFactory<>("sentenceYears")); // Calls getSentenceYears()
        colSentence.setMinWidth(120);

        table.getColumns().addAll(colId, colName, colCrime, colSentence);
        table.setPrefHeight(300);

        // db select for the prisoners
        String query = "SELECT p.prisonerid, p.name, p.crime, p.sentencedurationmonths " +
                       "FROM prisoners p " +
                       "JOIN advocate_prisoner ap ON p.prisonerid = ap.prisonerid " +
                       "WHERE ap.advocateid = ?";

        try (PreparedStatement pst = dbc.con.prepareStatement(query)) {
            pst.setInt(1, currentAdvocate.getAdvocateId()); 
            
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    // Creating an instance of the model class
                    Prisoner p = new Prisoner(
                        rs.getInt("prisonerid"),
                        rs.getString("name"),
                        rs.getString("crime"),
                        rs.getInt("sentencedurationmonths"),
                        "Cell B-1" // Mock cell number
                    );
                    //load prisoners into table
                    currentAdvocate.loadPrisoner(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading table data: " + e.getMessage());
        }

        // Add all loaded prisoners from the advocate container array into the table
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