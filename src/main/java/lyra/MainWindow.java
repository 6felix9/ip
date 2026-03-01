package lyra;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for MainWindow. Provides the layout for the other controls.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Lyra lyra;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/user.png"));
    private Image botImage = new Image(this.getClass().getResourceAsStream("/images/bot.png"));

    /**
     * Initializes the MainWindow controller.
     */
    @FXML
    public void initialize() {
        scrollPane.setFitToWidth(true);
        dialogContainer.heightProperty().addListener((observable, oldValue, newValue) ->
                Platform.runLater(() -> scrollPane.setVvalue(1.0)));
        showWelcome();
        Platform.runLater(() -> scrollPane.setVvalue(1.0));
    }

    /**
     * Sets the Lyra instance for this window.
     *
     * @param lyra The Lyra instance
     */
    public void setLyra(Lyra lyra) {
        this.lyra = lyra;
    }

    /**
     * Displays the welcome message.
     */
    private void showWelcome() {
        String welcomeText = "Hi there! I'm Lyra, your personal task helper.\nWhat would you like to get done today?";
        dialogContainer.getChildren().add(DialogBox.getLyraDialog(welcomeText, botImage));
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Lyra's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.trim().isEmpty()) {
            return;
        }

        // Create user dialog
        dialogContainer.getChildren().add(DialogBox.getUserDialog(input, userImage));

        // Get response from Lyra
        Lyra.LyraResponse response = lyra.getResponse(input);
        if (response.isError()) {
            dialogContainer.getChildren().add(DialogBox.getLyraErrorDialog(response.getMessage(), botImage));
        } else {
            dialogContainer.getChildren().add(DialogBox.getLyraDialog(response.getMessage(), botImage));
        }

        // Clear input field
        userInput.clear();

        // Check if user wants to exit
        if (input.trim().equalsIgnoreCase("bye")) {
            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                Platform.exit();
            }).start();
        }
    }
}
