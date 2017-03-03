package helpers.structures;

import helpers.functions.Helper;
import helpers.nodes.MyStage;
import helpers.nodes.WindowControlPanel;
import helpers.nodes.WindowResizer;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;

/**
 * Created by Святослав on 16.10.2016.
 */
public class Result extends HBox {
	Label word, yourWord, correct;

	public Result(String word, String yourWord, String correct) {
		if (yourWord.length() == 0) yourWord = "-";
		setFillHeight(true);
		this.word = new Label(word);
		this.word.setPrefWidth(700 / 3);
		this.yourWord = new Label(yourWord);
		this.yourWord.setPrefWidth(700 / 3);
		this.correct = new Label(correct);
		this.correct.setPrefWidth(700 / 3);
		getStylesheets().add("/styles/stageStyles/results.css");
		setId("correctPane");
		if (!yourWord.toLowerCase().equals(Helper.getCleanString(correct.toLowerCase()))) {
			setId("incorrectPane");
			this.yourWord.setId("incorrect");
			this.correct.setId("correct");
		}
		getChildren().addAll(this.word, new Separator(Orientation.VERTICAL), this.yourWord, new Separator(Orientation.VERTICAL), this.correct);
	}

	public Result() {
	}

	public void showResults() {
		new MyStage("/fxmls/results.fxml", null, null, new WindowResizer(400, 600), new WindowControlPanel(40, 10, 10, true, true, false, "/resources/images/icons/used/star-1.png", "%results")).show();
	}
}
