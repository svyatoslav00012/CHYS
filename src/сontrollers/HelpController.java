package сontrollers;

import helpers.nodes.WindowResizer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


/**
 * Created by Святослав on 15.10.2016.
 */
public class HelpController {

	double startX = -1, startY = -1;


	@FXML
	private Label ys;
	@FXML
	private AnchorPane anch;
	@FXML
	private AnchorPane rootAnchor;

	WindowResizer wr = new WindowResizer(200, 200);

	@FXML
	public void initialize() {
	}

	public void close(ActionEvent actionEvent) {
		((Stage) ys.getScene().getWindow()).close();
	}

	public void icon(ActionEvent actionEvent) {
		((Stage) ys.getScene().getWindow()).setIconified(true);
	}

	public void onDragged(MouseEvent mouseEvent) {
		if (wr.curX != -1) {
			startX = -1;
			startY = -1;
		} else if (startX > -1 && startY > -1) {
			((Stage) rootAnchor.getScene().getWindow()).setX(mouseEvent.getScreenX() - startX);
			((Stage) rootAnchor.getScene().getWindow()).setY(mouseEvent.getScreenY() - startY);
		}
	}

	public void onPressed(MouseEvent mouseEvent) {
		if (((Stage) rootAnchor.getScene().getWindow()).isMaximized() || wr.isPressed()) return;
		startX = mouseEvent.getSceneX();
		startY = mouseEvent.getSceneY();
	}

	public void onReleased(MouseEvent mouseEvent) {
		startX = -1;
		startY = -1;
	}
}
