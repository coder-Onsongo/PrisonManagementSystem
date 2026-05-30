package gui;

import database.DBConnection;
import visitor.Visitor;
import visitor.Visit;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.sql.*;

public class VisitorDashboard {

    private DBConnection dbc = new DBConnection();
    private Visitor currentVisitor;
    private TableView<Visit> table = new TableView<>();
    private Label lblFeedback = new Label();

    public VisitorDashboard(Visitor visitor) {
        this.currentVisitor = visitor;
    }

    public void show(Stage stage) {
        stage.setTitle("Prison Management System - Visitor Dashboard");

        // Main Layout Container Stack
        VBox mainRoot = new VBox(25);
        mainRoot.setPadding(new Insets(30, 30, 30, 30));
        mainRoot.setAlignment(Pos.TOP_CENTER);

        // Header Section
        Label lblTitle = new Label("Welcome, " + currentVisitor.getName());
        lblTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 22));

        //logged visits table view
        Label lblSection1 = new Label("Your Booking Records:");
        lblSection1.setFont(Font.font("Tahoma", FontWeight.SEMI_BOLD, 14));
        lblSection1.setAlignment(Pos.CENTER_LEFT);

        TableColumn<Visit, Integer> colId = new TableColumn<>("Visit ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("visitId"));
        colId.setMinWidth(70);

        TableColumn<Visit, String> colDate = new TableColumn<>("Scheduled Date");
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDate.setMinWidth(110);

        TableColumn<Visit, String> colTime = new TableColumn<>("Time");
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colTime.setMinWidth(90);

        TableColumn<Visit, Integer> colPrisoner = new TableColumn<>("Prisoner ID");
        colPrisoner.setCellValueFactory(new PropertyValueFactory<>("prisonerId"));
        colPrisoner.setMinWidth(100);

        TableColumn<Visit, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStatus.setMinWidth(110);

        table.getColumns().addAll(colId, colDate, colTime, colPrisoner, colStatus);
        table.setPrefHeight(180);

        // Populate Table View with Active sessions
        refreshTableData();

        // BOOKING NEW VISIT FORM
        Label lblSection2 = new Label("Request a New Visit Window:");
        lblSection2.setFont(Font.font("Tahoma", FontWeight.SEMI_BOLD, 14));

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(12);
        formGrid.setAlignment(Pos.CENTER);

        Label lblDateInput = new Label("Date (YYYY-MM-DD):");
        TextField txtDate = new TextField();
        txtDate.setPromptText("e.g. 2026-06-15");

        Label lblTimeInput = new Label("Time (HH:MM:SS):");
        TextField txtTime = new TextField();
        txtTime.setPromptText("e.g. 14:30:00");

        Button btnSubmit = new Button("Submit Request");
        btnSubmit.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));

        formGrid.add(lblDateInput, 0, 0);
        formGrid.add(txtDate, 1, 0);
        formGrid.add(lblTimeInput, 0, 1);
        formGrid.add(txtTime, 1, 1);
        formGrid.add(btnSubmit, 1, 2);

        lblFeedback.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));

        //Submit Request to PostgreSQL Database
        btnSubmit.setOnAction(e -> {
            String dateVal = txtDate.getText().trim();
            String timeVal = txtTime.getText().trim();

            if (dateVal.isEmpty() || timeVal.isEmpty()) {
                lblFeedback.setTextFill(Color.RED);
                lblFeedback.setText("Error: Input fields cannot be empty!");
                return;
            }

            // Execute Insert Query
            String insertQuery = "INSERT INTO visits (visitdate, visittime, prisonerid, visitorid, staffid, status) VALUES (?, CAST(? AS time), ?, ?, NULL, 'PENDING')";
            
            try (PreparedStatement pst = dbc.con.prepareStatement(insertQuery)) {
                pst.setDate(1, Date.valueOf(dateVal));
                pst.setString(2, timeVal);
                pst.setInt(3, currentVisitor.getTargetPrisonerId());
                pst.setInt(4, currentVisitor.getVisitorId());

                pst.executeUpdate();
                
                lblFeedback.setTextFill(Color.GREEN);
                lblFeedback.setText("Success: Visit request filed under PENDING status.");
                txtDate.clear();
                txtTime.clear();
                refreshTableData();
            } catch (IllegalArgumentException dateEx) {
                lblFeedback.setTextFill(Color.RED);
                lblFeedback.setText("Format Error: Ensure date matches YYYY-MM-DD pattern.");
            } catch (SQLException sqlEx) {
                lblFeedback.setTextFill(Color.RED);
                lblFeedback.setText("Database Error: Could not save request. " + sqlEx.getMessage());
            }
        });

        // Logout Button
        Button btnLogout = new Button("Log Out");
        btnLogout.setOnAction(e -> {
            Login login = new Login();
            login.start(stage);
        });

        HBox bottomControls = new HBox(15, btnLogout, lblFeedback);
        bottomControls.setAlignment(Pos.CENTER_LEFT);

        // Add everything to layout stack
        mainRoot.getChildren().addAll(lblTitle, lblSection1, table, lblSection2, formGrid, bottomControls);

        // Display Window
        Scene scene = new Scene(mainRoot, 600, 520);
        stage.setScene(scene);
        stage.show();
    }

    // Direct database refresh
    private void refreshTableData() {
        table.getItems().clear();
        String selectQuery = "SELECT visitid, visitdate, visittime, prisonerid, visitorid, staffid, status " +
                             "FROM visits WHERE visitorid = ? ORDER BY visitid DESC";

        try (PreparedStatement pst = dbc.con.prepareStatement(selectQuery)) {
            pst.setInt(1, currentVisitor.getVisitorId());

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Visit v = new Visit(
                        rs.getInt("visitid"),
                        rs.getString("visitdate"),
                        rs.getString("visittime"),
                        rs.getInt("prisonerid"),
                        rs.getInt("visitorid"),
                        rs.getInt("staffid"),
                        rs.getString("status")
                    );
                    table.getItems().add(v);
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to synchronize table data: " + e.getMessage());
        }
    }
}