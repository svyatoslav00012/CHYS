package helpers.nodes;

import helpers.functions.Helper;
import helpers.structures.Word;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import library.AppData;


/**
 * Created by Svyatoslav on 31.01.2017.
 */
public class Table extends TableView {

	private TableColumn column1 = new TableColumn();
	private TableColumn column2 = new TableColumn();
	private MyContextMenu contextMenu;

	public MyContextMenu getPopupMenu(){return contextMenu;}

	public Table(){
		initTable(null);
	}

	public Table(ObservableList items, MyContextMenu cmenu) {
		initTable(items);
		contextMenu = cmenu;
	}

	public void initTable(ObservableList items) {
		getStylesheets().add("/styles/nodeStyles/tableStyle.css");
		setPlaceholder(new Label(Helper.getI18nString("wordsNotFound")));
		getPlaceholder().setId("place-holder");
		getPlaceholder().autosize();
		column1.setText(Helper.getI18nString("en"));
		column1.setCellValueFactory(new PropertyValueFactory<Word, String>("eng"));
		if (AppData.getSettings().getTran().equals("ru")) {
			column2.setText(Helper.getI18nString("ru"));
			column2.setCellValueFactory(new PropertyValueFactory<Word, String>("rus"));
		} else {
			column2.setText(Helper.getI18nString("ukr"));
			column2.setCellValueFactory(new PropertyValueFactory<Word, String>("ukr"));
		}
		setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		getColumns().addAll(column1, column2);
		if (items != null) setItems(items);
		setRowFactory( tv -> {
			TableRow<Word> row = new TableRow<>();
			row.setOnMouseReleased(event -> {
				if (event.getButton() == MouseButton.SECONDARY && (! row.isEmpty()) ) {
					contextMenu.show(this, event.getScreenX(), event.getScreenY());
				}
			});
			return row ;
		});
	}

	public void setPopupMenu(MyContextMenu menu){
		contextMenu = menu;
	}
}
