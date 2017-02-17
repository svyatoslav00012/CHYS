package helpers.nodes;

import helpers.functions.Helper;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Svyatoslav on 07.02.2017.
 */
public class MyContextMenu extends ContextMenu{
	public static final String CHANGE = Helper.getI18nString("change");
	public static final String DELETE = Helper.getI18nString("delete");
	public static final String GO_TO = Helper.getI18nString("goToWord");
	public static final String REMOVE_FROM_CUR = Helper.getI18nString("removeWordFromCur");
	public static final String ADD_TO_CUR = Helper.getI18nString("addWordToCur");
	public static final String CHOOSE = Helper.getI18nString("writeDictation");
	public static final String DUPLICATE = Helper.getI18nString("duplicate");

	public MyContextMenu(String... items) {
		getStyleClass().add("contextMenu");
		for (int i = 0; i < items.length; ++i) {
			getItems().add(new MenuItem(items[i]));
		}
	}

	public MenuItem getItem(String label)
	{
		for(int i = 0;i<getItems().size();++i)
		if(getItems().get(i).getText().equals(label))return getItems().get(i);
		Helper.showError("Error in MyContextMenu.getItem(String label)\nMenuItem with label "+label+" not found in current ContextMenu");
		return null;
	}
}
