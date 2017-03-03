package helpers.nodes;

import helpers.functions.Helper;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Svyatoslav on 14.01.2017.
 */
public class MyAlert extends FadingStage {

	double startX = -1, startY = -1;
	private boolean confirm = false;
	private AnchorPane root = new AnchorPane();
	private Region icon = new Region();
	private WindowControlPanel wp = new WindowControlPanel(30, 10, 0, false, false, false, null, null);
	private Label type = new Label();
	private TextArea messageField = new TextArea(), stackTraceField = new TextArea();
	private Button btnShowHide = new Button(), btnYes = new Button("OK"), btnNo = new Button("NO"), btnCopyToCB = new Button();
	private HBox yesNo = new HBox(btnYes, btnNo);

	public MyAlert() {
		super(StageStyle.TRANSPARENT);
		setScene(new Scene(root));
		getScene().setFill(Color.TRANSPARENT);
		initAnchors();
		initRoot();
		initModality(Modality.APPLICATION_MODAL);
		setResizable(false);
	}

	public static void showTestAlerts() {
		Button info = new Button("Info"), confirm = new Button("Confirm"), warning = new Button("Warning"), error = new Button("Error"), exception = new Button("Exception");
		info.setOnAction(action -> Helper.showInfo("test info message"));
		confirm.setOnAction(action -> Helper.showConfirm("test confirm question"));
		warning.setOnAction(action -> Helper.showWarning("test warning message"));
		error.setOnAction(action -> Helper.showError("test error message"));
		exception.setOnAction(action -> Helper.showException("test exception message", new Exception("Exception stack trace")));
		VBox root = new VBox(10, info, confirm, warning, error, exception);
		root.setAlignment(Pos.CENTER);
		Stage test = new Stage();
		test.setScene(new Scene(root, 100, 200));
		test.show();
	}

	public void initRoot() {
		initStyles();
		initAnchors();
		initOnMouse();
		initOnButtonsPressed();
		icon.setPrefSize(150, 150);
		messageField.setPrefSize(330, 170);
		messageField.setWrapText(true);
		messageField.setEditable(false);
		initFocusTraversable();
		stackTraceField.setPrefSize(480, 390);
		stackTraceField.setEditable(false);
		stackTraceField.setWrapText(true);
		btnShowHide.setPrefSize(40, 40);
		btnShowHide.setTooltip(new Tooltip(Helper.getI18nString("showDetails", Helper.LOCAL)));
		btnYes.setPrefSize(60, 60);
		btnYes.setDefaultButton(true);
		btnNo.setPrefSize(60, 60);
		yesNo.setSpacing(30);
		yesNo.setAlignment(Pos.CENTER);
		yesNo.setFillHeight(true);
		yesNo.setPrefWidth(150);
		btnCopyToCB.setPrefSize(40, 40);
		btnCopyToCB.setTooltip(new Tooltip(Helper.getI18nString("copyToClipboard", Helper.LOCAL)));
		setWidth(500);
		setHeight(300);
		root.getChildren().addAll(type, messageField, icon, wp, yesNo);
	}

	void initFocusTraversable() {
		messageField.setFocusTraversable(false);
		btnShowHide.setFocusTraversable(false);
		btnYes.setFocusTraversable(false);
		btnNo.setFocusTraversable(false);
		btnCopyToCB.setFocusTraversable(false);
	}

	public void initAnchors() {
		AnchorPane.setTopAnchor(icon, 75.0);
		AnchorPane.setRightAnchor(icon, 10.0);
		AnchorPane.setTopAnchor(type, -5.0);
		AnchorPane.setLeftAnchor(type, 30.0);
		AnchorPane.setTopAnchor(messageField, 50.0);
		AnchorPane.setLeftAnchor(messageField, 10.0);
		AnchorPane.setTopAnchor(stackTraceField, 300.0);
		AnchorPane.setLeftAnchor(stackTraceField, 10.0);
		AnchorPane.setTopAnchor(btnShowHide, 250.0);
		AnchorPane.setLeftAnchor(btnShowHide, 10.0);
		AnchorPane.setTopAnchor(yesNo, 220.0);
		AnchorPane.setLeftAnchor(yesNo, 175.0);
		AnchorPane.setTopAnchor(btnCopyToCB, 245.0);
		AnchorPane.setRightAnchor(btnCopyToCB, 10.0);
	}

	public void initStyles() {
		getScene().getStylesheets().addAll("/styles/templateStyles/windowStyle.css", "/styles/templateStyles/alertStyle.css");
		root.setId("alert");
		btnShowHide.getStyleClass().add("button-icon");
		btnYes.getStyleClass().add("button-icon");
		btnNo.getStyleClass().add("button-icon");
		btnCopyToCB.getStyleClass().add("button-icon");
		icon.getStyleClass().add("icon");
		messageField.setId("messageField");
		stackTraceField.setId("stackTraceField");
		btnShowHide.setId("btnShow");
		btnYes.setId("btnYes");
		btnNo.setId("btnNo");
		btnCopyToCB.setId("btnCopyToCB");
	}

	public void showInfo(String message) {
		type.setText("INFO");
		type.setId("typeNorm");
		messageField.setText(message);
		icon.setId("info-icon");
		root.getChildren().removeAll(btnShowHide, btnCopyToCB, stackTraceField);
		yesNo.getChildren().remove(btnNo);
		showAndWait();
	}

	public void showWarning(String message) {
		type.setText("WARNING");
		type.setId("typeWarning");
		messageField.setText(message);
		icon.setId("warning-icon");
		root.getChildren().removeAll(btnShowHide, btnCopyToCB, stackTraceField);
		yesNo.getChildren().remove(btnNo);
		btnShowHide.setTooltip(new Tooltip(Helper.getI18nString("showDetails", Helper.LOCAL)));
		showAndWait();
	}

	public boolean showConfirm(String message) {
		confirm = false;
		btnYes.setText("YES");
		type.setText(Helper.getI18nString("sure", Helper.LOCAL));
		type.setId("typeNorm");
		messageField.setText(message);
		icon.setId("confirm-icon");
		root.getChildren().removeAll(btnShowHide, btnCopyToCB, stackTraceField);
		if (yesNo.getChildren().indexOf(btnNo) == -1) yesNo.getChildren().add(btnNo);
		showAndWait();
		btnYes.setText("OK");
		return confirm;
	}

	public void showError(String message, Exception e) {
		type.setId("typeWarning");
		root.getChildren().removeAll(btnShowHide, btnCopyToCB, stackTraceField);
		yesNo.getChildren().remove(btnNo);
		messageField.setText(message);
		messageField.setStyle("-fx-text-fill: red;");
		icon.setId("error-icon");
		if (e != null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String exceptionText = sw.toString();
			stackTraceField.setText(exceptionText);
			root.getChildren().addAll(btnShowHide);
			btnShowHide.setId("btnShow");
			type.setText("Exception catched");
		} else type.setText("ERROR!");
		showAndWait();
		setHeight(300);
		messageField.setStyle("-fx-text-fill: green;");
	}

	public void initOnButtonsPressed() {
		btnShowHide.setOnAction(action -> {
			if (root.getChildren().indexOf(stackTraceField) > -1) hideTrace();
			else showTrace();
		});
		btnYes.setOnAction(action -> {
			confirm = true;
			close();
		});
		btnNo.setOnAction(action -> {
			close();
		});
		btnCopyToCB.setOnAction(action -> {
			StringSelection ss = new StringSelection(stackTraceField.getText());
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
		});
	}

	public void showTrace() {
		btnShowHide.setTooltip(new Tooltip(Helper.getI18nString("hide", Helper.LOCAL)));
		root.getChildren().addAll(btnCopyToCB, stackTraceField);
		btnShowHide.setId("btnHide");
		setHeight(700);
	}

	public void hideTrace() {
		btnShowHide.setTooltip(new Tooltip(Helper.getI18nString("showDetails", Helper.LOCAL)));
		btnShowHide.setId("btnShow");
		root.getChildren().removeAll(btnCopyToCB, stackTraceField);
		setHeight(300);
	}

	public void initOnMouse() {
		root.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				startX = event.getScreenX();
				startY = event.getScreenY();
			}
		});
		root.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				startX = -1;
				startY = -1;
			}
		});
		root.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (startX > -1 && startY > -1) {
					setX(getX() + event.getScreenX() - startX);
					setY(getY() + event.getScreenY() - startY);
					startX = event.getScreenX();
					startY = event.getScreenY();
				}
			}
		});
	}
}
