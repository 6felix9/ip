package lyra;

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
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        scrollPane.setFitToWidth(true);
        showWelcome();
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
        String welcomeText = "Hello! I'm Lyra\nWhat can I do for you?";
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
        String response = lyra.getResponse(input);
        dialogContainer.getChildren().add(DialogBox.getLyraDialog(response, botImage));

        // Clear input field
        userInput.clear();

        // Check if user wants to exit
        if (input.trim().equalsIgnoreCase("bye")) {
            // Could add exit logic here if needed
        }
    }
}
