package fanta;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI window.
 */
public class MainWindow {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Fanta fanta;

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/User.png"));
    private final Image fantaImage = new Image(this.getClass().getResourceAsStream("/images/Fanta.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Fanta instance. */
    public void setFanta(Fanta fanta) {
        this.fanta = fanta;
    }

    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = fanta.respond(input);
        if (!input.isEmpty()) {
            dialogContainer.getChildren().add(DialogBox.getUserDialog(input, userImage));
        }
        if (!response.isEmpty()) {
            dialogContainer.getChildren().add(DialogBox.getFantaDialog(response, fantaImage));
        }
        userInput.clear();
    }
}
