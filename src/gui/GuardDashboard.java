package gui;

import database.DBOperations;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import visitor.Visit;
import java.util.List;

public class GuardDashboard {

    private final int loggedInGuardId;
    private final String guardName;
    private final DBOperations dbOps = new DBOperations();

    private ListView<String> visitListView;
    private ObservableList<String> visitObservableList;
    private List<Visit> currentVisitsList; 

    private Label lblSelectedVisit;
    private Button btnApprove;
    private Button btnReject;
    private Label lblStatusMessage;

    public GuardDashboard(int guardId, String guardName) {
        this.loggedInGuardId = guardId;
        this.guardName = guardName;
    }

    public void start(Stage stage) {
        stage.setTitle("Prison Management System - Guard Dashboard");

        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #fcfcfc;");

        HBox topBar = new HBox();
        topBar.setPadding(new Insets(0, 0, 20, 0));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-border-color: #eeeeee; -fx-border-width: 0 0 1 0;");
        
        Label lblWelcome = new Label("Welcome, Officer " + guardName + " (ID: " + loggedInGuardId + ")");
        lblWelcome.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        lblWelcome.setTextFill(Color.web("#333333"));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button btnLogout = new Button("Log Out");
        btnLogout.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15 8 15;");
        btnLogout.setOnAction(e -> {
            stage.close();
            System.out.println("Guard session ended safely. Logged out.");
        });
        
        topBar.getChildren().addAll(lblWelcome, spacer, btnLogout);
        mainLayout.setTop(topBar);

        VBox leftBox = new VBox(12);
        HBox.setHgrow(leftBox, Priority.ALWAYS);
        
        Label lblListHeader = new Label("Incoming & Historical Visit Requests:");
        lblListHeader.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        lblListHeader.setTextFill(Color.web("#555555"));
        
        visitListView = new ListView<>();
        visitObservableList = FXCollections.observableArrayList();
        visitListView.setItems(visitObservableList);
        visitListView.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 13px;");
        VBox.setVgrow(visitListView, Priority.ALWAYS);
        
        Button btnRefresh = new Button("🔄 Refresh Bookings");
        btnRefresh.setMaxWidth(Double.MAX_VALUE);
        btnRefresh.setStyle("-fx-padding: 10; -fx-font-weight: bold;");
        btnRefresh.setOnAction(e -> populateVisitsList());
        
        leftBox.getChildren().addAll(lblListHeader, visitListView, btnRefresh);

        VBox rightBox = new VBox(15);
        rightBox.setMinWidth(320);
        rightBox.setPadding(new Insets(0, 0, 0, 20));
        rightBox.setAlignment(Pos.TOP_CENTER);
        rightBox.setStyle("-fx-border-color: #e0e0e0; -fx-border-width: 0 0 0 1;");

        Label lblActionsHeader = new Label("Review Selection");
        lblActionsHeader.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        lblActionsHeader.setTextFill(Color.web("#444444"));

        lblSelectedVisit = new Label("No visit selected.\nSelect a pending record from the left panel.");
        lblSelectedVisit.setWrapText(true);
        lblSelectedVisit.setAlignment(Pos.CENTER);
        lblSelectedVisit.setMinHeight(120);
        lblSelectedVisit.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 15; -fx-border-color: #dddddd; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-alignment: center;");
        lblSelectedVisit.setMaxWidth(Double.MAX_VALUE);

        btnApprove = new Button("✔ Approve Visit");
        btnApprove.setStyle("-fx-background-color: #5cb85c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10;");
        btnApprove.setMaxWidth(Double.MAX_VALUE);
        btnApprove.setDisable(true);
        btnApprove.setOnAction(e -> handleStatusUpdate("APPROVED"));

        btnReject = new Button("❌ Reject Visit");
        btnReject.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10;");
        btnReject.setMaxWidth(Double.MAX_VALUE);
        btnReject.setDisable(true);
        btnReject.setOnAction(e -> handleStatusUpdate("REJECTED"));

        lblStatusMessage = new Label();
        lblStatusMessage.setFont(Font.font("Segoe UI", FontWeight.MEDIUM, 13));
        lblStatusMessage.setWrapText(true);

        rightBox.getChildren().addAll(lblActionsHeader, lblSelectedVisit, btnApprove, btnReject, lblStatusMessage);

        HBox splitLayout = new HBox();
        splitLayout.setPadding(new Insets(15, 0, 0, 0));
        splitLayout.getChildren().addAll(leftBox, rightBox);
        mainLayout.setCenter(splitLayout);

        visitListView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            int index = newValue.intValue();
            if (index >= 0 && currentVisitsList != null && index < currentVisitsList.size()) {
                Visit selectedVisit = currentVisitsList.get(index);
                displaySelectedVisit(selectedVisit);
            } else if (index == -1 && visitListView.getItems().isEmpty()) {
                System.out.println("List is refreshing, skipping structural panel wipe.");
            } else {
                resetActionPanel();
            }
        });

        populateVisitsList();

        Scene scene = new Scene(mainLayout, 900, 550);
        stage.setScene(scene);
        stage.show();
    }

    private void populateVisitsList() {
        visitObservableList.clear();
        try {
            currentVisitsList = dbOps.getAllVisitsForGuard();

            if (currentVisitsList == null || currentVisitsList.isEmpty()) {
                visitObservableList.add("No visit logs found in the database.");
                resetActionPanel();
                return;
            }

            for (Visit v : currentVisitsList) {
                String formatRow = String.format("ID: %02d | Date: %s @ %s | Prisoner: #%02d | Visitor: #%02d | [%s]",
                        v.getVisitId(), v.getDate(), v.getTime(), v.getPrisonerId(), v.getVisitorId(), v.getStatus().toUpperCase());
                visitObservableList.add(formatRow);
            }
        } catch (Exception ex) {
            System.err.println("Error rendering PostgreSQL data rows: " + ex.getMessage());
            visitObservableList.add("Error loading data from database connection.");
        }
    }

    private void displaySelectedVisit(Visit visit) {
        if (visit == null) {
            resetActionPanel();
            return;
        }

        String info = String.format("Visit Record Details:\n\n• Visit ID: %d\n• Schedule Date: %s\n• Schedule Time: %s\n• Target Prisoner ID: %d\n• Submitting Visitor ID: %d\n\nStatus Context: %s",
                visit.getVisitId(), visit.getDate(), visit.getTime(), visit.getPrisonerId(), visit.getVisitorId(), visit.getStatus().toUpperCase());
        lblSelectedVisit.setText(info);
        lblSelectedVisit.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 15; -fx-border-color: #dddddd; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-alignment: left; -fx-font-family: 'Segoe UI';");

        if ("PENDING".equalsIgnoreCase(visit.getStatus())) {
            btnApprove.setDisable(false);
            btnReject.setDisable(false);
            lblStatusMessage.setText("Action Required: Awaiting decision...");
            lblStatusMessage.setTextFill(Color.BLUE);
        } else {
            btnApprove.setDisable(true);
            btnReject.setDisable(true);
            lblStatusMessage.setText("Finalized Record (Signed by Staff ID: " + visit.getStaffId() + ").");
            lblStatusMessage.setTextFill(Color.GRAY);
        }
    }

    private void handleStatusUpdate(String targetStatus) {
        int selectedIndex = visitListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) return;

        Visit selectedVisit = currentVisitsList.get(selectedIndex);
        
        boolean success = dbOps.updateVisitStatus(selectedVisit.getVisitId(), targetStatus, loggedInGuardId);

        if (success) {
            lblStatusMessage.setText("Success! Status updated to " + targetStatus);
            lblStatusMessage.setTextFill(Color.GREEN);
            populateVisitsList(); 
        } else {
            lblStatusMessage.setText("Database Exception: Error processing update status write.");
            lblStatusMessage.setTextFill(Color.RED);
        }
    }

    private void resetActionPanel() {
        lblSelectedVisit.setText("No visit selected.\nSelect a pending record from the left panel.");
        lblSelectedVisit.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 15; -fx-border-color: #dddddd; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-alignment: center;");
        btnApprove.setDisable(true);
        btnReject.setDisable(true);
        lblStatusMessage.setText("");
    }
}