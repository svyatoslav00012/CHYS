package сontrollers;

import helpers.nodes.MyStage;
import helpers.nodes.WindowControllPanel;
import helpers.nodes.WindowResizer;
import helpers.functions.Helper;
import helpers.structures.Properties;
import helpers.structures.Result;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * Created by Святослав on 13.10.2016.
 */
public class DictationOptionsController {

	String[] types = {Helper.getI18nString("dictation.type1"), Helper.getI18nString("dictation.type2")};

	ObservableList<String> typs = FXCollections.observableArrayList(types);

	private static int typ = 1;
	private static String tranLeng = "ru";
	private static boolean random = false;

	public static boolean getRand() {
		return random;
	}

	public static int getTyp() {
		return typ;
	}

	public static String getTranLeng() {
		return tranLeng;
	}


	@FXML
	private ChoiceBox type;
	@FXML
	private ChoiceBox tran;

	@FXML
	public void initialize() {
		typ = 0;
		tranLeng = "ru";
		random = false;
		tran.setItems(FXCollections.observableArrayList(Properties.getLengNames(1)));
		tran.getSelectionModel().select(0);
		tran.setTooltip(new Tooltip(Helper.getI18nString("settings.tran.tooltip")));
		tran.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				tranLeng = Properties.get(newValue.intValue() + 1).getKey();
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
		MyStage dictation = new MyStage("/fxmls/dictat.fxml", null, null, new WindowResizer(700, 700), new WindowControllPanel(40, 20, 10, true, true, false, "/resources/images/icons/used/write.png", Helper.getI18nString("dictation") + " : " + MyListsController.getCur().getName()));
		//dictation.setAlwaysOnTop(true);
		AnchorPane.setRightAnchor(dictation.getWR(), 15.0);
		AnchorPane.setBottomAnchor(dictation.getWR(), 15.0);
		dictation.getWCP().getCloseButton().setOnAction(action -> {
			DictationController.finish();
		});
		dictation.setOnCloseRequest(action-> {
			DictationController.finish();
		});
		((Stage) type.getScene().getWindow()).close();
		dictation.showAndWait();
	}

	public void onClose(MyStage st) {
		if (Helper.showConfirm(Helper.getI18nString("finish") + " " + Helper.getI18nString("dictation") + "?")) return;
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
