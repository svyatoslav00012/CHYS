package сontrollers;

import helpers.functions.Helper;
import helpers.nodes.MyStage;
import helpers.nodes.WindowControlPanel;
import helpers.nodes.WindowResizer;
import helpers.structures.Properties;
import helpers.structures.Result;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Created by Святослав on 13.10.2016.
 */
public class DictationOptionsController {

	private static int typ = 1;
	private static String tranLang = "ru";
	private static boolean random = false;
	String[] types = {Helper.getI18nString("dictation.type1", Helper.LOCAL), Helper.getI18nString("dictation.type2", Helper.LOCAL)};
	ObservableList<String> typs = FXCollections.observableArrayList(types);
	@FXML
	private ChoiceBox type;
	@FXML
	private ChoiceBox tran;

	public static boolean getRand() {
		return random;
	}

	public static int getTyp() {
		return typ;
	}

	public static String getTranLang() {
		return tranLang;
	}

	@FXML
	public void initialize() {
		typ = 0;
		tranLang = "ru";
		random = false;
		tran.setItems(FXCollections.observableArrayList(Properties.getLengNames(1)));
		tran.getSelectionModel().select(0);
		tran.setTooltip(new Tooltip(Helper.getI18nString("settings.tran.tooltip", Helper.LOCAL)));
		tran.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				tranLang = Properties.get(newValue.intValue() + 1).getKey();
			}
		});
		type.setItems(FXCollections.observableArrayList(typs));
		type.getSelectionModel().select(0);
		type.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				typ = newValue.intValue();
			}
		});
	}

	public void start(ActionEvent actionEvent) {
		MyStage dictation = new MyStage("/fxmls/dictat.fxml", null, null, new WindowResizer(700, 700), new WindowControlPanel(40, 20, 10, true, true, false, "/resources/images/icons/used/write.png", Helper.getI18nString("dictation", Helper.LOCAL) + " : " + MyListsController.getCur().getName()));
		//dictation.setAlwaysOnTop(true);
		AnchorPane.setRightAnchor(dictation.getWR(), 15.0);
		AnchorPane.setBottomAnchor(dictation.getWR(), 15.0);
		dictation.getWCP().getCloseButton().setOnAction(action -> DictationController.finish());
		dictation.setOnCloseRequest(action -> DictationController.finish());
		((Stage) type.getScene().getWindow()).close();
		dictation.showAndWait();
	}

	public void onClose(MyStage st) {
		if (Helper.showConfirm(Helper.getI18nString("finish", Helper.LOCAL) + " " + Helper.getI18nString("dictation", Helper.LOCAL) + "?"))
			return;
		else {
			st.close();
			MainController.setGausian(false);
			MyListsController.setGausian(false);
			new Result().showResults();
		}
	}

	public void rand(ActionEvent actionEvent) {
		random = ((CheckBox) actionEvent.getSource()).isSelected();
	}
}
