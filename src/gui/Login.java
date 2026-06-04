package gui;

import database.DBOperations; 
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Login extends Application {
    private DBOperations dbOps = new DBOperations();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Prison Management System - Login");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(15);
        grid.setVgap(20);
        grid.setPadding(new Insets(40, 40, 40, 40));

        Label sceneTitle = new Label("Secure System Login");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 30));
        grid.add(sceneTitle, 0, 0, 2, 1);

        Label lblUserId = new Label("User ID:");
        grid.add(lblUserId, 0, 1);
        TextField txtUserId = new TextField();
        txtUserId.setPromptText("Enter your ID");
        grid.add(txtUserId, 1, 1);

        Label lblPassword = new Label("Password:");
        grid.add(lblPassword, 0, 2);
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Enter your password");
        grid.add(txtPassword, 1, 2);

        Button btnLogin = new Button("Sign In");
        btnLogin.setMaxWidth(Double.MAX_VALUE / 2);
        grid.add(btnLogin, 1, 3);

        Label lblMessage = new Label();
        lblMessage.setFont(Font.font("Arial", FontWeight.MEDIUM, 20));
        grid.add(lblMessage, 1, 4);

        btnLogin.setOnAction(e -> {
            String inputId = txtUserId.getText().trim();
            String inputPassword = txtPassword.getText().trim();

            if (inputId.isEmpty() || inputPassword.isEmpty()) {
                lblMessage.setTextFill(Color.RED);
                lblMessage.setText("Error: Fields cannot be blank!");
                return;
            }
            
            try {
                int authenticatedId = Integer.parseInt(inputId);
                Object userSession = dbOps.authenticateUser(authenticatedId, inputPassword);
                
                if (userSession != null) {
                    lblMessage.setTextFill(Color.GREEN);
                    lblMessage.setText("Success! Loading Dashboard...");
                    
                    // Check instance type and route seamlessly 
                    if (userSession instanceof visitor.Visitor) {
                        System.out.println("Switching to: Visitor Dashboard");
                        VisitorDashboard dashboard = new VisitorDashboard((visitor.Visitor) userSession);
                        dashboard.show(primaryStage);
                    } 
                    else if (userSession instanceof advocate.Advocate) {
                        System.out.println("Switching to: Advocate Dashboard");
                        AdvocateDashboard dashboard = new AdvocateDashboard((advocate.Advocate) userSession);
                        dashboard.show(primaryStage);
                    }
                    else if (userSession instanceof systemsAdmin.SystemAdmin) {
                        System.out.println("Switching to: System Admin Dashboard");
                        gui.AdminDashboard dashboard = new gui.AdminDashboard((systemsAdmin.SystemAdmin) userSession);
                        dashboard.show(primaryStage);
                        }
                } else {
                    lblMessage.setTextFill(Color.RED);
                    lblMessage.setText("Access Denied: Invalid ID or Password.");
                }
                
            } catch (NumberFormatException ex) {
                lblMessage.setTextFill(Color.RED);
                lblMessage.setText("Format Error: User ID must be a number!");
            }
        });

        Scene scene = new Scene(grid, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}