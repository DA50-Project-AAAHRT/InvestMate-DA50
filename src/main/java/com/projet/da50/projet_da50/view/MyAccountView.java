package com.projet.da50.projet_da50.view;

import com.projet.da50.projet_da50.controller.LoginErrorHandler;
import com.projet.da50.projet_da50.controller.UserController;
import com.projet.da50.projet_da50.model.User;
import com.projet.da50.projet_da50.view.components.CustomButton;
import com.projet.da50.projet_da50.view.components.CustomTextField;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import static com.projet.da50.projet_da50.controller.TokenNotStayedLogin.*;

public class MyAccountView extends UI {

    private final Stage primaryStage;
    private final LoginErrorHandler loginErrorHandler = new LoginErrorHandler();
    UserController userController = new UserController();



    private User user = new User();
    public MyAccountView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        String token = getToken();
        user = userController.findUserByUsername(token);
    }

    public void show() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.getStyleClass().add("main-background");

        // Add a title label at the top of the page
        Label titleLabel = new Label("MyAccount");
        titleLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 24px;");
        GridPane.setColumnSpan(titleLabel, 4); // Span across four columns
        GridPane.setHalignment(titleLabel, HPos.CENTER); // Center horizontally
        grid.add(titleLabel, 0, 0);

        // Add a label for the current email (use actual current email from user profile)
        Label currentEmailLabel = new Label("Current Email: " + user.getMail());
        grid.add(currentEmailLabel, 0, 1);

        // Button to modify the email
        CustomButton btnModifyEmail = new CustomButton("Modify Email");
        btnModifyEmail.getStyleClass().add("button-blue");
        grid.add(btnModifyEmail, 1, 1);

        // Create a text field for entering the new email (initially hidden)
        CustomTextField newEmailField = new CustomTextField();
        newEmailField.setVisible(false);
        Label newEmailLabel = new Label("New Email:");
        newEmailLabel.setVisible(false);

        // Add new email input components to the grid, but keep them hidden initially
        grid.add(newEmailLabel, 2, 1);
        grid.add(newEmailField, 3, 1);

        // Save email button (initially hidden)
        CustomButton btnSaveEmail = new CustomButton("Save Email");
        btnSaveEmail.getStyleClass().add("button-blue");
        btnSaveEmail.setVisible(false);
        grid.add(btnSaveEmail, 3, 2);

        // When the modify email button is clicked, show the text field and save button
        btnModifyEmail.setOnAction(e -> {
            newEmailField.setVisible(true);
            newEmailLabel.setVisible(true);
            btnSaveEmail.setVisible(true);
            btnModifyEmail.setVisible(false);
        });

        btnSaveEmail.setOnAction(e -> {
            String newEmail = newEmailField.getText();

            // Clear previous messages
            grid.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 1 && GridPane.getColumnIndex(node) == 4);

            // Check mail format
            if (loginErrorHandler.validateCreateAccountFields("TestUser", "aaaaaaa", newEmail).equals("Mail format is invalid.")){
                System.out.println("Invalid email entered: " + newEmail);
                // Display an error message
                Label errorField = new Label();
                errorField.setText("Invalid email format.");
                grid.add(errorField, 4, 1);

            } else if (loginErrorHandler.validateCreateAccountFields("TestUser", "aaaaaaa", newEmail).equals("This mail is already used."))
            {
                // Check if this mail is already used
                Label errorField = new Label();
                errorField.setText("This mail is already used.");
                grid.add(errorField, 4, 1);

                // Check if empty
            } else if(newEmail.isEmpty()) {
                Label errorField = new Label();
                errorField.setText("Please fill in all fields.");
                grid.add(errorField, 4, 1);
            } else {
                // Save the new email to the user profile
                user.setMail(newEmail);
                // Update the user in the database
                userController.updateUser(user);
                // Display a success message
                Label successField = new Label();
                successField.setText("Email updated successfully.");
                grid.add(successField, 4, 1);
        }
        });


        // Add a label for the current email (use actual current email from user profile)
        Label currentUserNameLabel = new Label("Current Username: " + user.getUsername());
        grid.add(currentUserNameLabel, 0, 3);

        // Button to modify the email
        CustomButton btnModifyUsername = new CustomButton("Modify Username");
        btnModifyUsername.getStyleClass().add("button-blue");
        grid.add(btnModifyUsername, 1, 3);

        // Create a text field for entering the new email (initially hidden)
        CustomTextField newUsernameField = new CustomTextField();
        newUsernameField.setVisible(false);
        Label newUsernameLabel = new Label("New Username:");
        newUsernameLabel.setVisible(false);

        // Add new email input components to the grid, but keep them hidden initially
        grid.add(newUsernameLabel, 2, 3);
        grid.add(newUsernameField, 3, 3);

        // Save email button (initially hidden)
        CustomButton btnSaveUsername = new CustomButton("Save Username");
        btnSaveUsername.getStyleClass().add("button-blue");
        btnSaveUsername.setVisible(false);
        grid.add(btnSaveUsername, 3, 4);

        // When the modify email button is clicked, show the text field and save button
        btnModifyUsername.setOnAction(e -> {
            newUsernameField.setVisible(true);
            newUsernameLabel.setVisible(true);
            btnSaveUsername.setVisible(true);
            btnModifyUsername.setVisible(false);
        });

        btnSaveUsername.setOnAction(e -> {
            String newUsername = newUsernameField.getText();

            // Clear previous messages
            grid.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 3 && GridPane.getColumnIndex(node) == 4);

            if (loginErrorHandler.validateCreateAccountFields(newUsername, "aaaaaa", "testUser@test.fr").equals("This username is already taken.")) {
                    Label errorField = new Label();
                    errorField.setText("This username is already used.");
                    grid.add(errorField, 4, 3);
                } else if (newUsername.isEmpty()) {
                    Label errorField = new Label();
                    errorField.setText("Please fill in all fields.");
                    grid.add(errorField, 4, 3);
                } else {
                    // Save the new email to the user profile
                    user.setUsername(newUsername);
                    // Update the user in the database
                    userController.updateUser(user);

                    // Update the token with the username
                    deleteToken();
                    saveToken(newUsername);
                    // Display a success message
                    Label successField = new Label();
                    successField.setText("Username updated successfully.");
                    grid.add(successField, 4, 3);
                    System.out.println("New username saved: " + newUsername);
                }
        });


        // DO the same for update the password
        // Add a label for the current email (use actual current email from user profile)
        Label currentPasswordLabel = new Label("Password");
        grid.add(currentPasswordLabel, 0, 5);

        // Button to modify the email
        CustomButton btnModifyPassword = new CustomButton("Modify Password");
        btnModifyUsername.getStyleClass().add("button-blue");
        grid.add(btnModifyPassword, 1, 5);


        //Write last password
        Label lastPasswordLabel = new Label("Current Password: ");
        lastPasswordLabel.setVisible(false);
        CustomTextField lastPasswordField = new CustomTextField();
        lastPasswordField.setVisible(false);


        grid.add(lastPasswordLabel, 2, 5);
        grid.add(lastPasswordField, 3, 5);


        // Create a text field for entering the new email (initially hidden)
        CustomTextField newPasswordField = new CustomTextField();
        newPasswordField.setVisible(false);
        Label newPasswordLabel = new Label("New Password:");
        newPasswordLabel.setVisible(false);

        // Add new email input components to the grid, but keep them hidden initially
        grid.add(newPasswordLabel, 4, 5);
        grid.add(newPasswordField, 5, 5);

        // Save email button (initially hidden)
        CustomButton btnSavePassword = new CustomButton("Save Password");
        btnSavePassword.getStyleClass().add("button-blue");
        btnSavePassword.setVisible(false);
        grid.add(btnSavePassword, 3, 6);

        // When the modify email button is clicked, show the text field and save button
        btnModifyPassword.setOnAction(e -> {
            newPasswordField.setVisible(true);
            newPasswordLabel.setVisible(true);
            btnSavePassword.setVisible(true);
            btnModifyPassword.setVisible(false);
            lastPasswordField.setVisible(true);
            lastPasswordLabel.setVisible(true);
        });

        btnSavePassword.setOnAction(e -> {
            String newPassword = newPasswordField.getText();
            String lastPassword = lastPasswordField.getText();

            // Clear previous messages
            grid.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 5 && GridPane.getColumnIndex(node) == 6);

            // Check if a field is empty
            if(newPassword.isEmpty() || lastPassword.isEmpty()){
                Label errorField = new Label();
                errorField.setText("Please fill in all fields.");
                grid.add(errorField, 6, 5);

                // Check is the last password is correct
            } else if (userController.verifyUserCredentials(user.getUsername(), lastPasswordField.getText())) {

                // Check if the new password respect the 6 character minimal length
                if (loginErrorHandler.validateCreateAccountFields("aaaa", newPassword, "testUser@test.fr").equals("Password should be at least 6 characters long.")) {
                    Label errorField = new Label();
                    errorField.setText("Password should be at least 6 characters long.");
                    grid.add(errorField, 6, 5);


                } else if (userController.verifyUserCredentials(user.getUsername(), newPasswordField.getText())){
                    Label errorField = new Label();
                    errorField.setText("New password should be different from the last one.");
                    grid.add(errorField, 6, 5);
                }else {
                    System.out.println("Correct last password");
                    String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                    user.setPassword(hashedPassword);
                    userController.updateUser(user);
                    Label successField = new Label();
                    successField.setText("Password updated successfully.");
                    grid.add(successField, 6, 5);
                }
            } else {
                System.out.println("Wrong current password");
                Label errorField = new Label();
                errorField.setText("Wrong current password.");
                grid.add(errorField, 6, 5);
            }
        });

        // Delete Account
        //TODO: Check wallet or more prevention
        CustomButton btnDelete = new CustomButton("Delete Account");
        btnDelete.getStyleClass().add("button-blue");
        grid.add(btnDelete, 0, 7);
        CustomButton btnSureDelete = new CustomButton("Are you sure ?");
        btnSureDelete.getStyleClass().add("button-red");
        btnSureDelete.setVisible(false);
        grid.add(btnSureDelete, 1, 7);

        // To see the new button delete
        btnDelete.setOnAction(e -> {
            btnSureDelete.setVisible(true);
        });

        //To delete
        btnSureDelete.setOnAction(e -> {
            userController.deleteUserById(user.getId());
            deleteToken();
            new AuthenticationFormView(primaryStage).show();
        });

        //TODO : Full screen page
        //TODO : Frontend

        // Go back button
        CustomButton btnBack = new CustomButton("Go back");
        btnBack.getStyleClass().add("button-blue");
        grid.add(btnBack, 0, 8);
        // Press echap to go back or the button
        btnBack.setCancelButton(true);
        btnBack.setOnAction(e -> {
            new MainMenuView(primaryStage).show();
        });

        Scene scene = new Scene(grid, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setTitle("My Account - " + getToken());
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }


}
