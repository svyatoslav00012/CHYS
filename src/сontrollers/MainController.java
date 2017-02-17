package сontrollers;

import helpers.functions.Helper;
import helpers.nodes.*;
import helpers.structures.Word;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import library.AppData;
import resources.fonts.MyFonts;

public class MainController {

	private static boolean add = false;
	private static MyStage myLists = new MyStage("/fxmls/myLists.fxml", null, null, new WindowResizer(600, 600), new WindowControllPanel(30, 20, 10, true, true, false, "/resources/images/icons/used/list-1.png", "%myLists"));
	private static MyStage settings, help;
	private static GaussianBlur blur = new GaussianBlur(10.0);
	private static Word curWord;
	private static String curSource;
	private static String curSearch = "";
	private static Table table = new Table(AppData.getLists().get(0).getWords(), new MyContextMenu(MyContextMenu.CHANGE, MyContextMenu.DELETE));
	private static TableColumn eng = new TableColumn(), tran = new TableColumn();
	private static TextFieldWithButton textField = new TextFieldWithButton(50, TextFieldWithButton.DEFAULT_ADD, "Search", TextFieldWithButton.TOOLTIP_ADD_WORD);
	EventHandler<ActionEvent> action = action -> {
		WordController.wordStage(WordController.ADD_WORD, Helper.makePossibleWord(textField.getTextField().getText()));
	};
	@FXML
	private Button btnChange;
	@FXML
	private Button btnDelete;
	@FXML
	private Button btnAdd;
	@FXML
	private Rectangle clip;
	@FXML
	private AnchorPane rootAnchor;

	public static Word getCurWord() {
		return curWord;
	}

	public static String getCurSource() {
		return curSource;
	}

	public static MyStage getSettingsStage() {
		return settings;
	}

	public static MyStage getListsStage() {
		return myLists;
	}

	public static boolean isAdd() {
		return add;
	}

	public static void setAdd(boolean b) {
		add = b;
	}

	public static Table getTable() {
		return table;
	}

	public static TextFieldWithButton getTextField() {
		return textField;
	}

	public static void setGausian(boolean b) {
		if (b) {
			table.setId(null);
			table.setEffect(blur);
			table.getScene().getRoot().setDisable(true);
		} else {
			table.setId("table");
			table.setEffect(null);
			table.getScene().getRoot().setDisable(false);
		}
	}

	public static void upDate() {
		table.setItems(AppData.getLists().get(0).getSubList(curSearch));
	}

	public static Table getFullTable() {
		Table table = new Table(AppData.getLists().get(0).getWords(), null);
		return table;
	}

	public static void goToWord(Word w) {
		textField.getTextField().setText(w.getEng());
		table.setItems(AppData.getLists().get(0).getSubList(textField.getTextField().getText()));
		for (int i = 0; i < table.getItems().size(); ++i)
			if (((Word) table.getItems().get(i)).getKey() == w.getKey()) {
				table.getSelectionModel().select(i);
				table.getScene().getWindow().centerOnScreen();
				((MyStage) table.getScene().getWindow()).toFront();
				return;
			}
		Helper.showError("Error in MainController.goToWord(Word w)\nCan't find word " + w.print() + " in All words");
	}

	@FXML
	public void initialize() {
		System.out.println(new MenuItem().getStyleClass().get(0));
		btnChange.setDisable(true);
		btnDelete.setDisable(true);
		initTable();
		rootAnchor.getChildren().addAll(table, textField);
		initTextField();
		btnAdd.setOnAction(action -> {
			WordController.wordStage(WordController.ADD_WORD, null);
		});
		btnChange.setOnAction(action -> {
			WordController.wordStage(WordController.CHANGE_WORD, curWord);
		});
	}

	public void initTable() {
		table.getPopupMenu().getItem(MyContextMenu.CHANGE).setOnAction(action -> {
			btnChange.fire();
		});
		table.getPopupMenu().getItem(MyContextMenu.DELETE).setOnAction(action -> {
			btnDelete.fire();
		});
		table.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.isControlDown()) {
					if (event.getCode() == KeyCode.ENTER && !WordController.getJustSaved()) {
						btnAdd.fire();
					}
					if (event.getCode() == KeyCode.C) {
						btnChange.fire();
					}
					if (event.getCode() == KeyCode.D) {
						btnDelete.fire();
					}
					WordController.setJustSaved(false);
				}
			}
		});
		table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				//System.out.println(((Word)table.getSelectionModel().getSelectedItem()).print());
				curWord = (Word) newValue;
				if (curWord == null) {
					btnChange.setDisable(true);
					btnDelete.setDisable(true);
				} else {
					btnChange.setDisable(false);
					btnDelete.setDisable(false);
				}
			}
		});
		AnchorPane.setLeftAnchor(table, 20.0);
		AnchorPane.setTopAnchor(table, 120.0);
		AnchorPane.setBottomAnchor(table, 20.0);
		AnchorPane.setRightAnchor(table, 190.0);
	}

	public void initTextField() {
		textField.getTextField().setOnMouseReleased(event -> search());
		AnchorPane.setLeftAnchor(textField, 20.0);
		AnchorPane.setTopAnchor(textField, 60.0);
		AnchorPane.setRightAnchor(textField, 190.0);
		textField.getTextField().setOnKeyReleased(event -> {
			search();
		});
	}

	public void search() {
		if (textField.getTextField().getText().equalsIgnoreCase("-=")) {
			MyNotification.showTestNotifications();
			textField.getTextField().clear();
		} else if (textField.getTextField().getText().equals("123")) {
			MyAlert.showTestAlerts();
			textField.getTextField().clear();
		} else if (textField.getTextField().getText().equals("showFonts()")) {
			MyFonts.showFonts();
			textField.getTextField().clear();
		}
		table.setItems(AppData.getLists().get(0).getSubList(textField.getTextField().getText()));
		if (Helper.presence(textField.getTextField().getText(), table) == Helper.NOT_AVAILABLE)
			textField.showButton(TextFieldWithButton.DEFAULT_ADD, TextFieldWithButton.TOOLTIP_ADD_WORD, action);
		else textField.hideButton();
		curSearch = textField.getTextField().getText();
	}

	public void settings(ActionEvent actionEvent) {
		if (settings != null) {
			if (settings.isIconified()) settings.setIconified(false);
			if (settings.isShowing()) settings.toFront();
			else settings.show();
			return;
		}
		settings = new MyStage("/fxmls/settings.fxml", null, null, new WindowResizer(500, 500), new WindowControllPanel(30, 10.0, 10.0, true, true, false, "/resources/images/icons/used/settings-2.png", "%settings"));
		settings.getWCP().getCloseButton().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (!SettingsController.getSettings().compare(AppData.getSettings())) {
					if (Helper.showConfirm(Helper.getI18nString("exitConfirmation", Helper.LOCAL))) settings.close();
					else return;
				}
				settings.close();
			}
		});
		settings.getScene().getStylesheets().add("/styles/templateStyles/choiceBoxStyle.css");
		settings.show();
	}

	public void myLists(ActionEvent actionEvent) {
		if (myLists.isIconified()) myLists.setIconified(false);
		if (myLists.isShowing()) myLists.toFront();
		else myLists.show();
		return;
	}

	public void help(ActionEvent actionEvent) {
		if (help != null) {
			if (help.isIconified()) help.setIconified(false);
			if (help.isShowing()) help.toFront();
			else help.show();
			return;
		}
		help = new MyStage("/fxmls/help.fxml", null, null, null, new WindowControllPanel(30, 10.0, 10.0, true, false, false, "resources/images/icons/used/help-1.png", "%help"));
		help.show();
	}

	public void delete(ActionEvent actionEvent) {
		if (curWord == null) return;
		curWord.delete();
		table.setItems(AppData.getLists().get(0).getSubList(textField.getTextField().getText()));
	}
}