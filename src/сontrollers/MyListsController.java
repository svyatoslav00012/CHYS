package сontrollers;

import helpers.functions.Helper;
import helpers.nodes.*;
import helpers.structures.WList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import library.AppData;

import java.io.File;

/**
 * Created by Святослав on 07.10.2016.
 */
public class MyListsController{
	private static GaussianBlur blur = new GaussianBlur(10.0);
	private static WList cur;
	private static Accordion accordion = new Accordion();

	@FXML
	private Button btnChoose;
	@FXML
	private Button btnChange;
	@FXML
	private Button btnDuplicate;
	@FXML
	private Button btnDelete;
	@FXML
	private AnchorPane root;
	@FXML
	private AnchorPane scpAnch;
	@FXML
	private ScrollPane scrollPane;

	public static boolean accNotNull() {                                                                                //возвращает true если accordion не null
		return accordion != null;
	}

	public static WList getCur() {
		return cur;
	}                                                                        //возвращает текущую выборку

	public static void setGausian(boolean b) {                                                                            // !!! Не работает !!!
		if (b) {
			accordion.setEffect(blur);
			accordion.getScene().getRoot().setDisable(true);
		} else {
			accordion.setEffect(null);
			accordion.getScene().getRoot().setDisable(false);
		}
	}

	@FXML
	public void initialize() {
		root.setOnDragEntered(action -> {
			System.out.println("dragEntered");
			if (action.getDragboard().hasFiles()) {
				System.out.println("files in dropBox:");
				for (File f : action.getDragboard().getFiles()) {
					System.out.println("\t" + f.getName());
				}
			}
		});
		accordion.getStyleClass().add("accordion");
		for (int i = 0; i < AppData.getLists().getLists().size(); i++) {
			accordion.getPanes().add(new MyTitledPane(AppData.getLists().get(i)));
		}
		accordion.expandedPaneProperty().addListener(new ChangeListener<TitledPane>() {
			@Override
			public void changed(ObservableValue<? extends TitledPane> observable, TitledPane oldValue, TitledPane newValue) {

				if (newValue == null) {
					cur = null;
					btnChange.setDisable(true);
					btnDuplicate.setDisable(true);
					btnDelete.setDisable(true);
					btnChoose.setDisable(true);
				} else if (newValue.getText().equals(accordion.getPanes().get(0).getText())) {
					btnChange.setDisable(true);
					btnDuplicate.setDisable(false);
					btnDelete.setDisable(true);
					btnChoose.setDisable(false);
					cur = AppData.getLists().get(0);
				} else {
					cur = ((MyTitledPane)newValue).getList();
					btnChange.setDisable(false);
					btnDuplicate.setDisable(false);
					btnDelete.setDisable(false);
					btnChoose.setDisable(false);
				}
				if (cur != null && cur.getWords().isEmpty()) btnChoose.setDisable(true);
			}
		});
		accordion.setEffect(new DropShadow(BlurType.ONE_PASS_BOX, Color.BLACK, 8, 0, 2, 0));
		AnchorPane.setTopAnchor(accordion, 0.0);
		AnchorPane.setBottomAnchor(accordion, 0.0);
		AnchorPane.setRightAnchor(accordion, 0.0);
		AnchorPane.setLeftAnchor(accordion, 0.0);
		initContextMenu();
		scpAnch.getChildren().add(accordion);
	}

	public void initContextMenu() {                                                                                        // init actions for titled panes !!! DURING INITIALIZATION !!!
		for (int i = 0; i < accordion.getPanes().size(); ++i) {
			initContextMenuActions(i);
		}
	}

	public void initContextMenuActions(int index) {                                                                        // initialize actions for TitledPane with index 'index'
		MyTitledPane titledPane = (MyTitledPane) accordion.getPanes().get(index);
		((MyTitledPane) accordion.getPanes().get(index)).getMyContextMenu().getItem(MyContextMenu.CHOOSE).setOnAction(action -> choose(titledPane.getList()));                                                            //choose
		if (titledPane.getList().getKey() > 0)
			((MyTitledPane) accordion.getPanes().get(index)).getMyContextMenu().getItem(MyContextMenu.CHANGE).setOnAction(action -> change(titledPane.getList()));                        //change
		((MyTitledPane) accordion.getPanes().get(index)).getMyContextMenu().getItem(MyContextMenu.DUPLICATE).setOnAction(action -> duplicate(titledPane.getList()));                                                        //duplicate
		if (titledPane.getList().getKey() > 0)
			((MyTitledPane) accordion.getPanes().get(index)).getMyContextMenu().getItem(MyContextMenu.DELETE).setOnAction(action -> delete(titledPane.getList()));                        //delete
	}

	public void newList() {                                                                                                //создаёт выборку и добавляет в конец
		WList wlist = ListController.listStage(ListController.NEW, null);
		if (wlist == null) return;
		if (AppData.getLists().add(wlist) && add(wlist))
			MyNotification.showMessage(MyNotification.COMPLETE, MyNotification.LIST_CREATED);
	}

	public void change(WList wList) {                                                                                    //перегруженная функция (см. ниже)
		cur = wList;
		changeList();
	}

	public void changeList() {                                                                                            //редактирует выборку
		WList wlist = ListController.listStage(ListController.CHANGE, cur);
		AppData.getLists().set(wlist);
		MyNotification.showMessage(MyNotification.COMPLETE, MyNotification.CHANGES_APPLIED);
	}

	public void duplicate() {
		if (cur != null) duplicate(cur);
	}                                                            //перегруженная функция (см. ниже)

	public void duplicate(WList list) {                                                                                    //делает дубликат выборки и вставляет его в аккордион после образца
		int curIndex = AppData.getLists().getLists().indexOf(list);
		AppData.getLists().insert(curIndex + 1, list.makeDuplicate());
		accordion.getPanes().add(curIndex + 1, new MyTitledPane(AppData.getLists().get(curIndex + 1)));
		initContextMenuActions(curIndex + 1);
		MyNotification.showMessage(MyNotification.COMPLETE, MyNotification.LIST_DUPLICATED);
	}

	public void delete(WList list) {                                                                                        //перегруженная функция (см. ниже)
		cur = list;
		delete();
	}

	public void delete() {                                                                                                //удаляет выборку
		if (cur == null) return;
		System.out.println("delete : " + cur.getName());
		if (Helper.showConfirm(Helper.getI18nString("delete", Helper.LOCAL) + " : " + cur.getName() + "?")) {
			AppData.getLists().remove(cur.getKey());
			for (int i = 0; i < accordion.getPanes().size(); ++i)
				if (accordion.getPanes().get(i).getText().equals(cur.getName())) {
					accordion.getPanes().remove(i);
					MyNotification.showMessage(MyNotification.COMPLETE, MyNotification.LIST_DELETED);
					return;
				}
			Helper.showError("Error in MyListsContoller.delete()\nPane with name " + cur.getName() + " doesn't exist");
		}
	}

	public boolean add(WList list) {                                                                                //добавляет MyTitledPane в конец акордиона
		accordion.getPanes().add(new MyTitledPane(list));
		initContextMenuActions(accordion.getPanes().size() - 1);
		scrollPane.setVvalue(1);
		return true;
	}

	public void choose(WList list) {                                                                                        //перегруженная функция (см. ниже)
		cur = list;
		choose();
	}

	public void choose() {                                                                                                // Открывает окно с настройками диктанта
		if (cur == null) return;
		if(cur.getWords().isEmpty()){
			Helper.showInfo("List \""+cur.getName()+"\"is empty!");
			return;
		}
		Stage start = new MyStage("/fxmls/dictationOptions.fxml", Modality.WINDOW_MODAL, accordion.getScene().getWindow(), null, new WindowControlPanel(30, 15, 0, false, false, false, "/resources/images/icons/used/options.png", "%options"));
		start.getScene().getStylesheets().add("/styles/templateStyles/choiceBoxStyle.css");
		start.show();
	}
}
