package gui;

import database.DBConnection;
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
import java.sql.*;

public class Login extends Application {

    // Instantiate dbconnection so as to check credentials
    private DBConnection dbc = new DBConnection();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Prison Management System - Login");

        // UI layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(15);
        grid.setVgap(20);
        grid.setPadding(new Insets(40, 40, 40, 40));

        // Title Header
        Label sceneTitle = new Label("Secure System Login");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 30));
        grid.add(sceneTitle, 0, 0, 2, 1);

        // User ID Input
        Label lblUserId = new Label("User ID:");
        grid.add(lblUserId, 0, 1);
        TextField txtUserId = new TextField();
        txtUserId.setPromptText("Enter your ID");
        grid.add(txtUserId, 1, 1);

        // Password Input
        Label lblPassword = new Label("Password:");
        grid.add(lblPassword, 0, 2);
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Enter your password");
        grid.add(txtPassword, 1, 2);

        // Login Button
        Button btnLogin = new Button("Sign In");
        btnLogin.setMaxWidth(Double.MAX_VALUE / 2);
        grid.add(btnLogin, 1, 3);

        // Status Feedback Message Label
        Label lblMessage = new Label();
        lblMessage.setFont(Font.font("Arial", FontWeight.MEDIUM, 20));
        grid.add(lblMessage, 1, 4);

        // BUTTON CLICK ACTION 
        btnLogin.setOnAction(e -> {
            String inputId = txtUserId.getText().trim();
            String inputPassword = txtPassword.getText().trim();

            // validation check
            if (inputId.isEmpty() || inputPassword.isEmpty()) {
                lblMessage.setTextFill(Color.RED);
                lblMessage.setText("Error: Fields cannot be blank!");
                return;
            }
            
            String query = "SELECT role, name FROM systemusers WHERE userid = ? AND password = ?";
            
            try (PreparedStatement pst = dbc.con.prepareStatement(query)) {
                pst.setInt(1, Integer.parseInt(inputId));
                pst.setString(2, inputPassword);
                
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        String databaseRole = rs.getString("role");
                        String realName = rs.getString("name"); 
                        
                        lblMessage.setTextFill(Color.GREEN);
                        lblMessage.setText("Success! Loading " + databaseRole + " Panel...");
                        
                        if ("VISITOR".equalsIgnoreCase(databaseRole)) {
                            System.out.println("Switching to: Visitor Dashboard ");
                            int authenticatedId = Integer.parseInt(inputId);
                            
                            int linkedPrisonerId = 1; 
                            
                            String lookupQuery = "SELECT prisonerid FROM visits WHERE visitorid = ? LIMIT 1";
                            try (PreparedStatement pstLookup = dbc.con.prepareStatement(lookupQuery)) {
                                pstLookup.setInt(1, authenticatedId);
                                try (ResultSet rsLookup = pstLookup.executeQuery()) {
                                    if (rsLookup.next()) {
                                        linkedPrisonerId = rsLookup.getInt("prisonerid");
                                    }
                                }
                            } catch (SQLException ex) {
                                System.err.println("Target prisoner fallback error: " + ex.getMessage());
                            }

                            // Instantiate your real Visitor object wrapper
                            visitor.Visitor activeVisitorSession = new visitor.Visitor(authenticatedId, realName, inputPassword, linkedPrisonerId);
                            
                            // Switch window layout
                            VisitorDashboard dashboard = new VisitorDashboard(activeVisitorSession);
                            dashboard.show(primaryStage);
                        } 
                        else if ("ADVOCATE".equalsIgnoreCase(databaseRole)) {
                            System.out.println("Switching to: Advocate Dashboard ");
                            int authenticatedId = Integer.parseInt(inputId);
                            
                            // Inherits the same clean realName from above safely!
                            advocate.Advocate activeSession = new advocate.Advocate(authenticatedId, realName, inputPassword);
                            AdvocateDashboard dashboard = new AdvocateDashboard(activeSession);
                            dashboard.show(primaryStage);
                        } 
                        else if ("GUARD".equalsIgnoreCase(databaseRole)) {
                            System.out.println("Switching to: Guard Dashboard ");
                        }
                    } else {
                        lblMessage.setTextFill(Color.RED);
                        lblMessage.setText("Access Denied: Invalid ID or Password.");
                    }
                }
            } catch (NumberFormatException ex) {
                lblMessage.setTextFill(Color.RED);
                lblMessage.setText("Format Error: User ID must be a number!");
            } catch (SQLException ex) {
                lblMessage.setTextFill(Color.RED);
                lblMessage.setText("Database Connection Error: " + ex.getMessage());
            }
        });

        // Show the window panel scene
        Scene scene = new Scene(grid, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}