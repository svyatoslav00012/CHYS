package helpers.nodes;

import helpers.functions.Helper;
import helpers.structures.WList;
import helpers.structures.Word;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseButton;
import library.AppData;
import сontrollers.MainController;
import сontrollers.MyListsController;

/**
 * Created by Student-6 on 12.10.2016.
 */
public class MyTitledPane extends TitledPane {
	private Table table;
	private MyContextMenu contextMenu = new MyContextMenu(MyContextMenu.CHOOSE, MyContextMenu.CHANGE, MyContextMenu.DUPLICATE, MyContextMenu.DELETE);
	private WList list;

	public MyTitledPane(WList wlist) {
		if(wlist == null){
			Helper.showError("Error in MyTitledPane(Wlist wlist)\nwlist is null");
			return;
		}
		if(wlist.getKey() == 0)contextMenu = new MyContextMenu(MyContextMenu.CHOOSE, MyContextMenu.DUPLICATE);
		if(wlist.getWords().isEmpty())contextMenu.getItem(MyContextMenu.CHOOSE).setDisable(true);
		list = wlist;
		setText(list.getName());
		table = new Table(list.getWords(), new MyContextMenu(MyContextMenu.GO_TO));
		table.getPopupMenu().getItem(MyContextMenu.GO_TO).setOnAction(action -> MainController.goToWord(((Word)table.getSelectionModel().getSelectedItem())));
		setContent(table);
		if(wlist.getKey() == 0)setId("allWords");
		setOnMouseReleased(action -> {
			if(action.getButton() == MouseButton.SECONDARY)contextMenu.show(this, action.getScreenX(), action.getScreenY());
		});
	}

	public WList getList(){return list;}

	public MyContextMenu getMyContextMenu(){
		return contextMenu;
	}
}
