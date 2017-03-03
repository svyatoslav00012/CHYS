package сontrollers;

import helpers.functions.FileHelper;
import helpers.functions.Helper;
import helpers.nodes.MyNotification;
import helpers.nodes.MyStage;
import helpers.nodes.WindowControlPanel;
import helpers.structures.Word;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import library.AppData;

/**
 * Created by Святослав on 05.10.2016.
 */
public class WordController {

	public static final int CHANGE_WORD = 1;
	public static final int ADD_WORD = 2;

	private static int type = -1;
	private static Word curWord;

	private static boolean justSaved = false;
	@FXML
	private TextField eng;
	@FXML
	private TextField rus;
	@FXML
	private TextField ukr;
	@FXML
	private Label lablNotFill;
	@FXML
	private Button save;

	public static boolean getJustSaved() {
		return justSaved;
	}

	public static void setJustSaved(boolean b) {
		justSaved = b;
	}

	public static int wordStage(int type, Word w) {            // returns word index
		if (type == CHANGE_WORD && w == null || type != ADD_WORD && type != CHANGE_WORD) return -1;
		WordController.type = type;
		WordController.curWord = w;
		String tit = "%addWord";
		if (type == CHANGE_WORD)
			tit = Helper.getI18nString("change", Helper.LOCAL) + " " + Helper.getI18nString("word", Helper.LOCAL);
		new MyStage("/fxmls/word.fxml", Modality.APPLICATION_MODAL, null, null, new WindowControlPanel(30, 15, 0, false, false, false, "/resources/images/icons/used/checkmark-1.png", tit)).showAndWait();
		int c = -1;
		if (curWord != null) c = curWord.getKey();
		destruct();
		System.out.println(c);
		return c;
	}

	public static void destruct() {
		curWord = null;
		type = -1;
	}

	@FXML
	public void initialize() {
		if (curWord != null) {
			eng.setText(curWord.getEng());
			rus.setText(curWord.getRus());
			ukr.setText(curWord.getUkr());
		}
		save.setOnAction(action -> {
				justSaved = true;
				eng.getStyleClass().remove("incorrectField");
				rus.getStyleClass().remove("incorrectField");
				ukr.getStyleClass().remove("incorrectField");
				if (eng.getText().isEmpty() || rus.getText().isEmpty() || ukr.getText().isEmpty()) {					// check if empty
					if (eng.getText().isEmpty()) eng.getStyleClass().add("incorrectField");
					if (rus.getText().isEmpty()) rus.getStyleClass().add("incorrectField");
					if (ukr.getText().isEmpty()) ukr.getStyleClass().add("incorrectField");
					lablNotFill.setText(Helper.getI18nString("addWord.notfill", Helper.LOCAL));
				} else if (!Helper.isEnglish(eng.getText().toLowerCase()) || !Helper.isRussian(rus.getText().toLowerCase()) || !Helper.isUkrainian(ukr.getText().toLowerCase())) {	// check if incorrect language
					if (!Helper.isEnglish(eng.getText().toLowerCase())) eng.getStyleClass().add("incorrectField");
					if (!Helper.isRussian(rus.getText().toLowerCase())) rus.getStyleClass().add("incorrectField");
					if (!Helper.isUkrainian(ukr.getText().toLowerCase())) ukr.getStyleClass().add("incorrectField");
					lablNotFill.setText(Helper.getI18nString("addWord.invalid", Helper.LOCAL));
				} else if (type == ADD_WORD && AppData.getLists().get(0).find(eng.getText(), rus.getText(), ukr.getText()) != -1) {			// check if already added
					eng.getStyleClass().add("incorrectField");
					rus.getStyleClass().add("incorrectField");
					ukr.getStyleClass().add("incorrectField");
					lablNotFill.setText(Helper.getI18nString("addWord.alreadyAdded", Helper.LOCAL));
				} else {																								// ADDING
					if (type == ADD_WORD) {																				// if add Word
						curWord = new Word(AppData.getFreeWordKey(), eng.getText(), rus.getText(), ukr.getText());
						AppData.getLists().get(0).add(curWord);
					}
					else {																					//if change word
						curWord = new Word(curWord.getKey(), eng.getText(), rus.getText(), ukr.getText());
						for (int i = 0; i < AppData.getLists().getLists().size(); ++i)
							if (curWord.inside(AppData.getLists().get(i).getWords())) {
								AppData.getLists().get(i).set(curWord);
								//if (MyListsController.accNotNull()) MyListsController.setTitledPane(i);
							}
						AppData.getLists().get(0).set(curWord);
					}
					FileHelper.rewrite();
					MainController.upDate();
					if (type == ADD_WORD)
						MyNotification.showMessage(MyNotification.COMPLETE, MyNotification.WORD_ADDED);
					else MyNotification.showMessage(MyNotification.COMPLETE, MyNotification.WORD_EDITED);
					((Stage) (lablNotFill.getScene().getWindow())).close();
				}
			});
	}

	public void close(ActionEvent actionEvent) {
		((Stage) (lablNotFill.getScene().getWindow())).close();
	}
}
