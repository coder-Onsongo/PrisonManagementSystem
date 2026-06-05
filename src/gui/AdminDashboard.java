package gui;

import systemsAdmin.SystemAdmin;
import database.DBOperations;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AdminDashboard {

     // instantiating opperations object to perform opperations 
    private SystemAdmin adminSession;
    private DBOperations dbOps = new DBOperations();

    //captures the session details passed forward from the login screen and creates an admin object 
    public AdminDashboard(SystemAdmin adminSession) {
        this.adminSession = adminSession;
    }

    public void show(Stage stage) {
        stage.setTitle("Prison Management System - Admin Panel");

        // Main BorderPane Container
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Top Greeting Header
        VBox topHeader = new VBox(5);
        Label lblWelcome = new Label("System Administrator Dashboard");
        lblWelcome.setFont(Font.font("Tahoma", FontWeight.BOLD, 24));
        Label lblUser = new Label("Active Session: " + adminSession.getName() + " (ID: " + adminSession.getUserId() + ")");
        lblUser.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        topHeader.getChildren().addAll(lblWelcome, lblUser);
        root.setTop(topHeader);

        // TabPane to cleanly separate Users management from Prisoners management
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        //~~ tab 1 systems user controls
        Tab userTab = new Tab("Manage System Users");
        HBox userSplitLayout = new HBox(20);
        userSplitLayout.setPadding(new Insets(15));

        // Left Side: ListView & Load Button
        VBox userLeftBox = new VBox(10);
        ListView<String> userListView = new ListView<>();
        Button btnRefreshUsers = new Button("View/Refresh Users");
        userLeftBox.getChildren().addAll(new Label("Database Records:"), userListView, btnRefreshUsers);
        HBox.setHgrow(userLeftBox, Priority.ALWAYS);
        userListView.setMaxWidth(Double.MAX_VALUE);

        // Right Side: Inputs for Add / Alter / Delete
         VBox userRightBox = new VBox(10);
        userRightBox.setMinWidth(280);
        TextField txtUserId = new TextField(); txtUserId.setPromptText("User ID (Integer)");
        TextField txtUserName = new TextField(); txtUserName.setPromptText("Full Name");
        TextField txtUserPass = new TextField(); txtUserPass.setPromptText("Password");
        ComboBox<String> cmbRole = new ComboBox<>();
        cmbRole.getItems().addAll("VISITOR", "ADVOCATE", "GUARD", "SYSTEM_ADMIN");
        cmbRole.setPromptText("Select Role");

        HBox userActions = new HBox(10);
        Button btnAddUser = new Button("Add");
        Button btnAlterUser = new Button("Alter");
        Button btnDeleteUser = new Button("Delete");
        userActions.getChildren().addAll(btnAddUser, btnAlterUser, btnDeleteUser);

        Label lblUserStatus = new Label();
        userRightBox.getChildren().addAll(
            new Label("User Controls:"), txtUserId, txtUserName, txtUserPass, cmbRole, userActions, lblUserStatus
        );
        userSplitLayout.getChildren().addAll(userLeftBox, userRightBox);
        userTab.setContent(userSplitLayout);

        // tab 2 prisoner side logic
        Tab prisonerTab = new Tab("Manage Prisoners");
        HBox prisonerSplitLayout = new HBox(20);
        prisonerSplitLayout.setPadding(new Insets(15));

        // Left Side: ListView & Load Button
        VBox prisLeftBox = new VBox(10);
        ListView<String> prisonerListView = new ListView<>();
        Button btnRefreshPrisoners = new Button("View/Refresh Prisoners");
        prisLeftBox.getChildren().addAll(new Label("Database Records:"), prisonerListView, btnRefreshPrisoners);
        HBox.setHgrow(prisLeftBox, Priority.ALWAYS);
        prisonerListView.setMaxWidth(Double.MAX_VALUE);

        // Right Side: Inputs for Add / Alter / Delete
        VBox prisRightBox = new VBox(10);
        prisRightBox.setMinWidth(280);
        TextField txtPrisId = new TextField(); txtPrisId.setPromptText("Prisoner ID (Integer)");
        TextField txtPrisName = new TextField(); txtPrisName.setPromptText("Full Name");
        TextField txtPrisCrime = new TextField(); txtPrisCrime.setPromptText("Crime (e.g. Cyber Fraud)");
        TextField txtPrisSentence = new TextField(); txtPrisSentence.setPromptText("Sentence Duration (Months)");

        HBox prisActions = new HBox(10);
        Button btnAddPris = new Button("Add");
        Button btnAlterPris = new Button("Alter");
        Button btnDeletePris = new Button("Delete");
        prisActions.getChildren().addAll(btnAddPris, btnAlterPris, btnDeletePris);

        Label lblPrisStatus = new Label();
        prisRightBox.getChildren().addAll(
            new Label("Prisoner Controls:"), txtPrisId, txtPrisName, txtPrisCrime, txtPrisSentence, prisActions, lblPrisStatus
        );
        prisonerSplitLayout.getChildren().addAll(prisLeftBox, prisRightBox);
        prisonerTab.setContent(prisonerSplitLayout);

        tabPane.getTabs().addAll(userTab, prisonerTab);
        root.setCenter(tabPane);
        
        //log out btn
        HBox bottomPane = new HBox();
        bottomPane.setPadding(new Insets(10, 0, 0, 0));
        bottomPane.setAlignment(Pos.CENTER_RIGHT);
        
        Button btnLogout = new Button("Log Out");
        btnLogout.setPrefSize(120, 35);
        btnLogout.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        bottomPane.getChildren().add(btnLogout);
        root.setBottom(bottomPane);
        

        // extensively researched event handling (04 -06- 2026) 😂😂
        // log out btn 
btnLogout.setOnAction(e -> {
            Login login = new Login();
            login.start(stage);
        });
        // Users Actions
        btnRefreshUsers.setOnAction(e -> {
            userListView.getItems().clear();
            userListView.getItems().addAll(dbOps.viewAllSystemUsers());
        });

        //system user add btn
        btnAddUser.setOnAction(e -> {
            try {
                int id = Integer.parseInt(txtUserId.getText().trim());
                String name = txtUserName.getText().trim();
                String pass = txtUserPass.getText().trim();
                String role = cmbRole.getValue();
                if(name.isEmpty() || pass.isEmpty() || role == null) {
                    lblUserStatus.setTextFill(Color.RED); lblUserStatus.setText("Error: Fields cannot be blank!");
                    return;
                }
                if (dbOps.addSystemUser(id, name, pass, role)) {
                    lblUserStatus.setTextFill(Color.GREEN); lblUserStatus.setText("User added successfully!");
                    btnRefreshUsers.fire();
                } else {
                    lblUserStatus.setTextFill(Color.RED); lblUserStatus.setText("Database rejected entry (ID taken/Admin exists).");
                }
            } catch (NumberFormatException ex) {
                lblUserStatus.setTextFill(Color.RED); lblUserStatus.setText("ID must be a clean number.");
            }
        });

        //system user alter btn
        btnAlterUser.setOnAction(e -> {
            try {
                int id = Integer.parseInt(txtUserId.getText().trim());
                String name = txtUserName.getText().trim();
                String pass = txtUserPass.getText().trim();
                String role = cmbRole.getValue();
                if (dbOps.alterSystemUser(id, name, pass, role)) {
                    lblUserStatus.setTextFill(Color.GREEN); lblUserStatus.setText("User altered cleanly!");
                    btnRefreshUsers.fire();
                } else {
                    lblUserStatus.setTextFill(Color.RED); lblUserStatus.setText("Alter failed: ID not found.");
                }
            } catch (Exception ex) {
                lblUserStatus.setTextFill(Color.RED); lblUserStatus.setText("Verify all fields are correctly formatted.");
            }
        });

        //system user delete btn
        btnDeleteUser.setOnAction(e -> {
            try {
                int id = Integer.parseInt(txtUserId.getText().trim());
                if (dbOps.deleteSystemUser(id)) {
                    lblUserStatus.setTextFill(Color.GREEN); lblUserStatus.setText("User dropped successfully.");
                    btnRefreshUsers.fire();
                } else {
                    lblUserStatus.setTextFill(Color.RED); lblUserStatus.setText("Delete failed: Check ID presence.");
                }
            } catch (Exception ex) { lblUserStatus.setTextFill(Color.RED); lblUserStatus.setText("Provide an integer ID."); }
        });


        // Prisoners Actions
        //view prisoners btn 
        btnRefreshPrisoners.setOnAction(e -> {
            prisonerListView.getItems().clear();
            prisonerListView.getItems().addAll(dbOps.viewAllPrisoners());
        });

        //add prisoner btn
        btnAddPris.setOnAction(e -> {
            try {
                int id = Integer.parseInt(txtPrisId.getText().trim());
                String name = txtPrisName.getText().trim();
                String crime = txtPrisCrime.getText().trim();
                int months = Integer.parseInt(txtPrisSentence.getText().trim());
                
                if (dbOps.addPrisoner(id, name, crime, months)) {
                    lblPrisStatus.setTextFill(Color.GREEN); lblPrisStatus.setText("Prisoner registered.");
                    btnRefreshPrisoners.fire();
                } else {
                    lblPrisStatus.setTextFill(Color.RED); lblPrisStatus.setText("Fail: Double check unique ID rules.");
                }
            } catch (Exception ex) { lblPrisStatus.setTextFill(Color.RED); lblPrisStatus.setText("Check data types formatting!"); }
        });

        // alter prisoner btn
        btnAlterPris.setOnAction(e -> {
            try {
                int id = Integer.parseInt(txtPrisId.getText().trim());
                String name = txtPrisName.getText().trim();
                String crime = txtPrisCrime.getText().trim();
                int months = Integer.parseInt(txtPrisSentence.getText().trim());
                
                if (dbOps.alterPrisoner(id, name, crime, months)) {
                    lblPrisStatus.setTextFill(Color.GREEN); lblPrisStatus.setText("Prisoner record modified!");
                    btnRefreshPrisoners.fire();
                } else {
                    lblPrisStatus.setTextFill(Color.RED); lblPrisStatus.setText("Target Prisoner ID not found.");
                }
            } catch (Exception ex) { lblPrisStatus.setTextFill(Color.RED); lblPrisStatus.setText("Verify type formats."); }
        });

        //delete prisoner
        btnDeletePris.setOnAction(e -> {
            try {
                int id = Integer.parseInt(txtPrisId.getText().trim());
                if (dbOps.deletePrisoner(id)) {
                    lblPrisStatus.setTextFill(Color.GREEN); lblPrisStatus.setText("Prisoner dropped cleanly.");
                    btnRefreshPrisoners.fire();
                } else {
                    lblPrisStatus.setTextFill(Color.RED); lblPrisStatus.setText("Record missing from data table.");
                }
            } catch (Exception ex) { lblPrisStatus.setTextFill(Color.RED); lblPrisStatus.setText("Input error on ID."); }
        });

        // list content at dashboard launch
        btnRefreshUsers.fire();
        btnRefreshPrisoners.fire();

        Scene scene = new Scene(root, 850, 550);
        scene.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}