package сontrollers;

import helpers.functions.Helper;
import helpers.nodes.*;
import helpers.structures.WList;
import helpers.structures.Word;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import library.AppData;

/**
 * Created by Святослав on 08.10.2016.
 */
public class ListController {

	public static final int NEW = 0;
	public static final int CHANGE = 1;

	private static Word curWordAll, curWordThis;
	private static WList list, sample;
	private static TextFieldWithButton searchAll = new TextFieldWithButton(45, TextFieldWithButton.DEFAULT_ADD, "Search", TextFieldWithButton.TOOLTIP_ADD_WORD), searchThis = new TextFieldWithButton(45, TextFieldWithButton.DEFAULT_ADD, "Search", TextFieldWithButton.TOOLTIP_ADD_WORD);
	private static Table tableAll = new Table(), tableThis = new Table();
	private static int curType = -1;
	EventHandler<ActionEvent> addWord = action -> {                                                                        // add word - SearhAll
		int key = WordController.wordStage(WordController.ADD_WORD, Helper.makePossibleWord(searchThis.getTextField().getText()));
		refreshTables();
	};
	EventHandler<ActionEvent> addWordCur = action -> {                                                                    // add word - SearchThis
		int key = WordController.wordStage(WordController.ADD_WORD, Helper.makePossibleWord(searchThis.getTextField().getText()));
		list.add(key);
		refreshTables();
	};
	EventHandler<ActionEvent> replace_This = action -> {                                                                // addToCur - SearchThis
		list.add(AppData.getLists().get(0).getWordKey(searchThis.getTextField().getText()));
		refreshTables();
	};
	EventHandler<ActionEvent> replace_All = action -> {                                                                // addToCur - SearchAll
		list.add(AppData.getLists().get(0).getWordKey(searchAll.getTextField().getText()));
		refreshTables();
	};
	EventHandler<ActionEvent> remove_This = action -> {                                                                // removeFromCur - SearchThis
		list.remove(AppData.getLists().get(0).getWordKey(searchThis.getTextField().getText()));
		refreshTables();
	};
	EventHandler<ActionEvent> remove_All = action -> {                                                                // removeFromCur - SearchAll
		list.remove(AppData.getLists().get(0).getWordKey(searchAll.getTextField().getText()));
		refreshTables();
	};
	@FXML
	private AnchorPane root;
	@FXML
	private TextField fieldName;
	@FXML
	private Button btnSave;
	@FXML
	private Button btnAddAll;
	@FXML
	private Button btnRemoveAll;
	@FXML
	private Label lablNotFill;

	public static WList getList() {
		return list;
	}

	public static void destruct() {
		curWordAll = null;
		curWordThis = null;
		list = null;
		sample = null;
		curType = -1;
	}

	public static WList listStage(int type, WList wlist) {
		System.out.println("Enter in change");
		if (type == CHANGE) {
			if (wlist == null) return null;
			list = wlist;
			sample = wlist;
		} else list = new WList();
		System.out.println("changing...");
		MyStage newList = new MyStage("/fxmls/list.fxml", Modality.APPLICATION_MODAL, null, null, new WindowControllPanel(30, 10, 10, false, false, false, "/resources/images/icons/used/list-2.png", "%myLists.newList"));
		newList.getWCP().getCloseButton().setOnAction(action -> {
			if (curType == CHANGE && !list.isEqual(sample) && !Helper.showConfirm(Helper.getI18nString("exitConfirmation", Helper.LOCAL)))
				action.consume();
			newList.close();
		});
		newList.showAndWait();
		wlist = list;
		destruct();
		return wlist;
	}

	@FXML
	public void initialize() {
		initTables();
		initTFWBs();
		fieldName.setText(list.getName());
		root.getChildren().addAll(searchAll, searchThis, tableAll, tableThis);
	}

	public void initTables(){
		tableThis.setItems(list.getWords());
		tableAll.setItems(AppData.getLists().get(0).getSubList(searchAll.getTextField().getText(), list.getWords()));
		initPopup();
		AnchorPane.setTopAnchor(tableAll, 200.0);
		AnchorPane.setLeftAnchor(tableAll, 20.0);
		AnchorPane.setBottomAnchor(tableAll, 150.0);
		AnchorPane.setTopAnchor(tableThis, 200.0);
		AnchorPane.setRightAnchor(tableThis, 20.0);
		AnchorPane.setBottomAnchor(tableThis, 150.0);
		tableAll.setPrefWidth(350);
		tableThis.setPrefWidth(350);
		tableAll.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				if (newValue == null) curWordAll = null;
				else curWordAll = (Word) newValue;
			}
		});
		tableThis.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				if (newValue == null) curWordThis = null;
				else curWordThis = (Word) newValue;
			}
		});
		tableAll.setOnMouseClicked(event ->  {
			if(event.getClickCount() == 2 && curWordAll != null) addToList();
		});
		tableThis.setOnMouseClicked(action -> {
			if(action.getClickCount() == 2 && curWordThis != null) removeFromList();
		});
		tableAll.setOnKeyPressed(event -> {
			if(event.getCode() == KeyCode.ENTER && curWordAll != null) addToList();
		});
		tableThis.setOnKeyPressed(event -> {
			if(event.getCode() == KeyCode.ENTER && curWordThis != null) removeFromList();
		});
	}

	public void initPopup(){
		tableThis.setPopupMenu(new MyContextMenu(MyContextMenu.REMOVE_FROM_CUR, MyContextMenu.CHANGE, MyContextMenu.DELETE));
		tableThis.getPopupMenu().getItem(MyContextMenu.REMOVE_FROM_CUR).setOnAction(action -> removeFromList());
		tableThis.getPopupMenu().getItem(MyContextMenu.CHANGE).setOnAction(action -> changeWord(curWordThis));
		tableThis.getPopupMenu().getItem(MyContextMenu.DELETE).setOnAction(action -> {
			if(curWordThis != null){
				list.remove(curWordThis);
				curWordThis.delete();
				refreshTables();
			}});
		tableAll.setPopupMenu(new MyContextMenu(MyContextMenu.ADD_TO_CUR, MyContextMenu.CHANGE, MyContextMenu.DELETE));
		tableAll.getPopupMenu().getItem(MyContextMenu.ADD_TO_CUR).setOnAction(action -> addToList());
		tableAll.getPopupMenu().getItem(MyContextMenu.CHANGE).setOnAction(action -> changeWord(curWordAll));
		tableAll.getPopupMenu().getItem(MyContextMenu.DELETE).setOnAction(action -> {if(curWordAll != null)curWordAll.delete(); refreshTables();});
	}

	public void initTFWBs(){
		searchAll.setPrefWidth(350);
		AnchorPane.setTopAnchor(searchAll, 150.0);
		AnchorPane.setLeftAnchor(searchAll, 20.0);
		searchAll.getTextField().setOnKeyReleased(action -> {
			tableAll.setItems(AppData.getLists().get(0).getSubList(searchAll.getTextField().getText(), list.getWords()));
			if(Helper.presence(searchAll.getTextField().getText(), MainController.getFullTable()) == Helper.NOT_AVAILABLE) searchAll.showButton(TextFieldWithButton.DEFAULT_ADD, TextFieldWithButton.TOOLTIP_ADD_WORD, addWord);
			else{
				int ans = Helper.presence(searchAll.getTextField().getText(), tableThis);
				if(ans == Helper.NOT_AVAILABLE)searchAll.showButton(TextFieldWithButton.RIGHT_ARROW, TextFieldWithButton.TOOLTIP_REPLACE, replace_All);
				else if(ans == Helper.AVAILABLE)searchAll.showButton(TextFieldWithButton.LEFT_ARROW, TextFieldWithButton.TOOLTIP_BACK_WORD, remove_All);
				else searchAll.hideButton();
			}
		});
		searchThis.setPrefWidth(350);
		AnchorPane.setTopAnchor(searchThis, 150.0);
		AnchorPane.setRightAnchor(searchThis, 20.0);
		searchThis.getTextField().setOnKeyReleased(action -> {
			tableThis.setItems(list.getSubList(searchThis.getTextField().getText()));
			if(Helper.presence(searchThis.getTextField().getText(), MainController.getFullTable()) == Helper.NOT_AVAILABLE) searchThis.showButton(TextFieldWithButton.DEFAULT_ADD, TextFieldWithButton.TOOLTIP_ADD_WORD, addWordCur);
			else {
				int ans = Helper.presence(searchThis.getTextField().getText(), tableThis);
				if(ans == Helper.NOT_AVAILABLE)searchThis.showButton(TextFieldWithButton.RIGHT_ARROW, TextFieldWithButton.TOOLTIP_REPLACE, replace_This);
				else if(ans == Helper.AVAILABLE)searchThis.showButton(TextFieldWithButton.LEFT_ARROW, TextFieldWithButton.TOOLTIP_BACK_WORD, remove_This);
				else searchThis.hideButton();
			}
		});
	}

	public void addToList() {																							// add selected(curWordAll) word to list
		if (curWordAll == null) return;
		list.add(curWordAll.getKey());
		refreshTables();
		if (curType == CHANGE) check();
	}

	public void addAllToList(){																							// add all words to list (From AppData.getLists.get(0) - All Words)
		list.getWords().setAll(AppData.getLists().get(0).getWords());
		refreshTables();
	}

	public void removeFromList() {																						// remove selected(curWordThis) words from list
		if (curWordThis == null) return;
		list.remove(curWordThis.getKey());
		refreshTables();
		if (curType == CHANGE) check();
	}

	public void removeAllFromList() {																					//remove all words from list
		list.getWords().clear();
		refreshTables();
	}

	public void changeWord(Word w){
		WordController.wordStage(WordController.CHANGE_WORD, w);
		refreshTables();
	}

	public void saveList(ActionEvent actionEvent) {
		if (list.getName().isEmpty()) {
			fieldName.getStyleClass().add("incorrectField");
			lablNotFill.setText(Helper.getI18nString("list.notFill", Helper.LOCAL));
			return;
		} else if(AppData.getLists().find(list.getName(), sample) != -1) {
			fieldName.getStyleClass().add("incorrectField");
			lablNotFill.setText(Helper.getI18nString("list.alreadyAdded", Helper.LOCAL));
			return;
		} else if(curType == CHANGE)sample.setName(fieldName.getText());
		if (curType == NEW) {
			list.setKey(AppData.getFreeListKey());
			((Stage) fieldName.getScene().getWindow()).close();
		} else sample = list;
		btnSave.setDisable(true);
	}

	public void refreshTables(){
		tableThis.setItems(list.getSubList(searchThis.getTextField().getText()));
		tableAll.setItems(AppData.getLists().get(0).getSubList(searchAll.getTextField().getText(), list.getWords()));
	}

	public void check() {
		if (list.isEqual(sample)) btnSave.setDisable(true);
		else btnSave.setDisable(false);
	}

	public void nameChanged(Event event) {
		list.setName(fieldName.getText());
		btnSave.setDisable(false);
	}
}
