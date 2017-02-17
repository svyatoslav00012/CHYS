package helpers.nodes;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import сontrollers.DictationController;

/**
 * Created by Svyatoslav on 05.11.2016.
 */
public class WindowResizer extends Rectangle {

	private boolean isDict = false, isFirstTry = true;
	private int index = -1;

	public double curX = -1, curY = -1;

	public WindowResizer(int minW, int minH) {
		setStyle("-fx-effect: dropshadow( one-pass-box , black , 8 , 0.0 , 0 , 3 );");
		AnchorPane.setBottomAnchor(((Rectangle) this), 0.0);
		AnchorPane.setRightAnchor(((Rectangle) this), 0.0);
		setHeight(40);
		setWidth(40);
		setStrokeWidth(0);
		setCursor(Cursor.SE_RESIZE);
		setRotate(-45);
		setFill(Color.TRANSPARENT);
		setFill(new ImagePattern(new Image("/resources/images/icons/used/arrow.png")));
		setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				curX = event.getScreenX();
				curY = event.getScreenY();
			}
		});
		setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				curX = -1;
				curY = -1;
			}
		});
		setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (curX > -1 && curY > -1) {
					getScene().getWindow().setWidth(getScene().getWindow().getWidth() + (event.getScreenX() - curX));
					getScene().getWindow().setHeight(getScene().getWindow().getHeight() + (event.getScreenY() - curY));
					if (getScene().getWindow().getWidth() < minW) getScene().getWindow().setWidth(minW);
					else curX = getScene().getWindow().getWidth() + getScene().getWindow().getX() - 20;
					if (getScene().getWindow().getHeight() < minH) getScene().getWindow().setHeight(minH);
					else curY = getScene().getWindow().getHeight() + getScene().getWindow().getY() - 20;
					((Rectangle) getScene().getRoot().getClip()).setWidth(getScene().getWindow().getWidth());
					((Rectangle) getScene().getRoot().getClip()).setHeight(getScene().getWindow().getHeight());
				}
			}
		});
	}
}
