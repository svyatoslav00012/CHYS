package helpers.nodes;

import helpers.functions.Helper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * Created by Svyatoslav on 16.01.2017.
 */
public class TextFieldWithButton extends AnchorPane {

	private static String hotkeys = "\nCtrl + ENTER\nCtrl + SPACE\nCtrl + 'A'";

	public static final Tooltip TOOLTIP_ADD_WORD = new Tooltip(Helper.getI18nString("addWord") + hotkeys);
	public static final Tooltip TOOLTIP_BACK_WORD = new Tooltip(Helper.getI18nString("removeWordFromCur") + hotkeys);
	public static final Tooltip TOOLTIP_REPLACE = new Tooltip(Helper.getI18nString("addWordToCur") + hotkeys);

	public static final String DEFAULT_ADD = "/resources/images/icons/used/add.png";
	public static final String RIGHT_ARROW = "/resources/images/icons/used/arrow-right-2.png";
	public static final String LEFT_ARROW = "/resources/images/icons/used/arrow-left-2.png";

	private Button button = new Button();
	private TextField textField = new TextField();

	public TextFieldWithButton(int height, String buttonImageURI, String promptText, Tooltip tooltip) {
		getStylesheets().add("/styles/nodeStyles/tFWBStyle.css");
		getStyleClass().add("t-f-w-b");
		textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) setId("focus");
				else setId("");
			}
		});
		setTopAnchor(button, 5.0);
		setRightAnchor(button, 5.0);
		setBottomAnchor(button, 5.0);
		setTopAnchor(textField, 0.0);
		setRightAnchor(textField, (double) height);
		setBottomAnchor(textField, 0.0);
		setLeftAnchor(textField, 0.0);
		button.setTooltip(tooltip);
		Helper.hackTooltip(button.getTooltip(), Duration.millis(500), Duration.INDEFINITE);
		button.setVisible(false);
		button.setStyle("-fx-background-image: url(" + buttonImageURI + ");");
		button.setFocusTraversable(false);
		button.setPrefSize(height - 10, height - 10);
		button.setMinSize(height - 10, height - 10);
		button.setMaxSize(height - 10, height - 10);
		textField.setPromptText(promptText);
		textField.addEventHandler(KeyEvent.KEY_PRESSED, action -> {
			if (action.getCode() == KeyCode.CONTROL) {
				button.getStyleClass().add("hovered");
				if (button.isVisible()) {
					Bounds bounds = button.localToScreen(button.getBoundsInLocal());
					button.getTooltip().show(getScene().getWindow(), bounds.getMaxX() - 20, bounds.getMaxY() - 20);
				}
			}
			if (action.isControlDown() && (action.getCode() == KeyCode.ENTER || action.getCode() == KeyCode.SPACE || action.getCode() == KeyCode.A)) {
				button.getStyleClass().remove("hovered");
				button.getStyleClass().add("pressed");
				if (button.isVisible()) button.fire();
			}
		});
		textField.addEventHandler(KeyEvent.KEY_RELEASED, action -> {
			if (action.getCode() == KeyCode.CONTROL) {
				button.getStyleClass().remove("hovered");
				button.getTooltip().hide();
			}
			if (action.getCode() == KeyCode.ENTER || action.getCode() == KeyCode.SPACE || action.getCode() == KeyCode.A)
				button.getStyleClass().remove("pressed");
		});
		button.addEventHandler(ActionEvent.ACTION, action -> {
			System.out.println("hide Tooltip");
			button.getTooltip().hide();
		});
		getChildren().addAll(textField, button);
	}

	public TextField getTextField() {
		return textField;
	}

	public Button getButton() {
		return button;
	}

	public void showButton(String uri, Tooltip tp, EventHandler<ActionEvent> action) {
		if (tp != null) button.setTooltip(tp);
		if (action != null) button.setOnAction(action);
		if (uri != null) button.setStyle("-fx-background-image: url(\"" + uri + "\");");
		button.setVisible(true);
	}

	public void hideButton() {
		button.setVisible(false);
	}
}
