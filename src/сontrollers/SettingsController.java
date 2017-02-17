package сontrollers;

import helpers.nodes.MyStage;
import helpers.nodes.WindowControllPanel;
import helpers.functions.FileHelper;
import helpers.functions.Helper;
import helpers.structures.Properties;
import helpers.structures.Settings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import library.AppData;

public class SettingsController {

	private double startX = -1, startY = -1;

	Stage errLog;

	private static Settings settings = new Settings("custom");
	@FXML
	private ChoiceBox lengSelect;
	@FXML
	private ChoiceBox tranSelect;
	@FXML
	private Button btnSave;
	@FXML
	private Button btnErr;
	@FXML
	private Rectangle clip;
	@FXML
	private ScrollPane scroll;
	@FXML
	private AnchorPane scrollAnchor;
	@FXML
	private AnchorPane root;
	@FXML
	private Button btnLenToDef;
	@FXML
	private Button btnTranToDef;
	@FXML
	private Button btnAllToDef;

	public static Settings getSettings() {
		return settings;
	}

	@FXML
	public void initialize() {
		btnLenToDef.setVisible(false);
		btnTranToDef.setVisible(false);
		btnLenToDef.setTooltip(new Tooltip(Helper.getI18nString("cancel")));
		btnTranToDef.setTooltip(new Tooltip(Helper.getI18nString("cancel")));
		btnAllToDef.setTooltip(new Tooltip(Helper.getI18nString("settings.resetAll")));
		btnErr.setTooltip(new Tooltip(Helper.getI18nString("settings.btnErr")));
		btnSave.setDisable(true);
		lengSelect.setItems(FXCollections.observableArrayList(Properties.getLengNames(0)));
		lengSelect.getSelectionModel().select(Properties.getID(AppData.getSettings().getLeng()));
		lengSelect.setTooltip(new Tooltip(Helper.getI18nString("settings.leng.tooltip")));
		lengSelect.getSelectionModel().selectedIndexProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				settings.setLeng(Properties.get(newValue.intValue()).getKey());
				if (settings.getLeng().equals(AppData.getSettings().getLeng())) {
					btnSave.setDisable(true);
					lengSelect.getParent().getStyleClass().remove("changed");
					btnLenToDef.setVisible(false);
				} else {
					btnSave.setDisable(false);
					lengSelect.getParent().getStyleClass().add("changed");
					btnLenToDef.setVisible(true);
				}
				if (settings.compare(AppData.getSettings())) btnSave.setDisable(true);
				else btnSave.setDisable(false);
			}
		});
		tranSelect.setItems(FXCollections.observableArrayList(Properties.getLengNames(1)));
		tranSelect.getSelectionModel().select(Properties.getID(AppData.getSettings().getTran()) - 1);
		tranSelect.setTooltip(new Tooltip(Helper.getI18nString("settings.tran.tooltip")));
		tranSelect.getSelectionModel().selectedIndexProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				settings.setTran(Properties.get(newValue.intValue() + 1).getKey());
				if (settings.getTran().equals(AppData.getSettings().getTran())) {
					tranSelect.getParent().getStyleClass().remove("changed");
					btnTranToDef.setVisible(false);
				} else {
					tranSelect.getParent().getStyleClass().add("changed");
					btnTranToDef.setVisible(true);
				}
				if (settings.compare(AppData.getSettings())) btnSave.setDisable(true);
				else btnSave.setDisable(false);
			}
		});
	}

	public void apply(ActionEvent actionEvent) {
		AppData.getSettings().set(settings);
		btnSave.setDisable(true);
		lengSelect.getParent().getStyleClass().remove("changed");
		tranSelect.getParent().getStyleClass().remove("changed");
		btnLenToDef.setVisible(false);
		btnTranToDef.setVisible(false);
		FileHelper.rewrite();
	}

	public void showErrorLog(ActionEvent actionEvent) {
		if (errLog != null) errLog.close();
		errLog = new MyStage("/fxmls/errorLog.fxml", null, null, null, new WindowControllPanel(30, 10, 0, true, false, false, "/resources/images/icons/used/log.png", "Log"));
		errLog.show();
	}

	public void allToCustom(ActionEvent actionEvent) {
		settings = new Settings("custom");
		refreshScreen(scrollAnchor.getScene().getRoot());
	}

	public void refreshScreen(Parent p) {
		lengSelect.getSelectionModel().select(Properties.getID(AppData.getSettings().getLeng()));
		tranSelect.getSelectionModel().select(Properties.getID(AppData.getSettings().getTran()) - 1);
		btnSave.setDisable(true);
	}

	public void lenToDef(ActionEvent actionEvent) {
		lengSelect.getSelectionModel().select(Properties.getID(AppData.getSettings().getLeng()));
		if (settings.compare(AppData.getSettings())) btnSave.setDisable(true);
	}

	public void tranToDef(ActionEvent actionEvent) {
		tranSelect.getSelectionModel().select(Properties.getID(AppData.getSettings().getTran()) - 1);
		if (settings.compare(AppData.getSettings())) btnSave.setDisable(true);
	}
}
