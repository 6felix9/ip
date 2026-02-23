package lyra;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a dialog box for chat messages.
 */
public class DialogBox extends HBox {
    private static final String BOT_STYLE = "-fx-background-color: #e8e8e8; -fx-background-radius: 12; "
            + "-fx-text-fill: #1a1a1a; -fx-padding: 8 12 8 12;";
    private static final String USER_STYLE = "-fx-background-color: #0078D4; -fx-background-radius: 12; "
            + "-fx-text-fill: white; -fx-padding: 8 12 8 12;";
    private static final String ERROR_STYLE = "-fx-background-color: #fde8e8; -fx-background-radius: 12; "
            + "-fx-text-fill: #b00020; -fx-padding: 8 12 8 12;";

    @FXML
    private Label dialog;
    @FXML
    private ImageView avatar;

    /**
     * Creates a DialogBox with the specified text and image.
     *
     * @param text The message text
     * @param img The avatar image
     */
    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DialogBox.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        avatar.setImage(img);
    }

    /**
     * Creates a user dialog box (text on left, avatar on right).
     *
     * @param text The user's message
     * @param img The user avatar image
     * @return A DialogBox instance
     */
    public static DialogBox getUserDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img);
        db.flip();
        db.dialog.setStyle(USER_STYLE);
        return db;
    }

    /**
     * Creates a Lyra dialog box (image on left).
     *
     * @param text Lyra's response message
     * @param img The bot avatar image
     * @return A DialogBox instance
     */
    public static DialogBox getLyraDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img);
        db.dialog.setStyle(BOT_STYLE);
        return db;
    }

    /**
     * Creates a Lyra dialog box for error messages with light red bubble.
     *
     * @param text Lyra's error message
     * @param img The bot avatar image
     * @return A DialogBox instance with error styling
     */
    public static DialogBox getLyraErrorDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img);
        db.dialog.setStyle(ERROR_STYLE);
        return db;
    }

    /**
     * Flips the dialog box so the text is on the left and image is on the right (for user messages).
     */
    private void flip() {
        this.setAlignment(Pos.TOP_RIGHT);
        dialog.setAlignment(Pos.CENTER_RIGHT);
        // Reorder children so text appears on left, avatar on right
        getChildren().clear();
        getChildren().addAll(dialog, avatar);
    }
}
