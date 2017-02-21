package helpers.nodes;

import javafx.scene.control.TextField;
import сontrollers.DictationController;

/**
 * Created by Svyatoslav on 22.12.2016.
 */
public class DigitField extends TextField {

	private int value, maxValue;
	private String prevText;

	public DigitField(String text, int maxValue) {
		setText(text);
		prevText = text;
		init();
		value = Integer.parseInt(text.replaceAll("\\D", ""));
		this.maxValue = maxValue;
	}

	public DigitField(int value, int maxValue) {
		setText(value + "");
		init();
		prevText = value + "";
		this.value = value;
		this.maxValue = maxValue;
	}

	public void init() {
		setFocusTraversable(false);
		setMinWidth(50);
		setPrefWidth(getText().length() * getFont().getSize() * 0.7 + 30);
		positionCaret(getText().length());
		setOnKeyReleased(event -> {
			//setMaxWidth(DictationController.getPp().prefWidth(0)-100);
			setText(setToNormInt(getText().replaceAll("\\D", "")));
			value = Integer.parseInt(getText());
			setPrefWidth(getText().length() * getFont().getSize() * 0.7 + 30);
			positionCaret(getText().length());
			prevText = getText();
			((DictationController.ProgressPanel) getParent().getParent().getParent().getParent()).chooseThis(value);
		});
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
		setText(value + "");
		setPrefWidth(getText().length() * getFont().getSize() * 0.7 + 30);
		positionCaret(getText().length());
	}

	private String setToNormInt(String text) {
		if (text == null || text.length() == 0 || Long.parseLong(text) == 0) text = "1";
		if (Long.parseLong(text) > maxValue) text = maxValue + "";
		if (Long.parseLong(text) > Integer.MAX_VALUE) text = prevText;
		return text;
	}

}
