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
     * Creates a user dialog box (image on right).
     *
     * @param text The user's message
     * @param img The user avatar image
     * @return A DialogBox instance
     */
    public static DialogBox getUserDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img);
        db.flip();
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
        return new DialogBox(text, img);
    }

    /**
     * Flips the dialog box so the image is on the right (for user messages).
     */
    private void flip() {
        this.setAlignment(Pos.TOP_RIGHT);
        dialog.setAlignment(Pos.CENTER_RIGHT);
        getChildren().setAll(dialog, avatar);
    }
}
