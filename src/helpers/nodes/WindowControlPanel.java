package helpers.nodes;

import helpers.functions.Helper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Created by Svyatoslav on 01.12.2016.
 */
public class WindowControlPanel extends AnchorPane {
	private Button btnClose = new Button();
	private Button btnIconif = new Button();
	private Button btnMxmize = new Button();
	private HBox buttons = new HBox();
	private Rectangle logoArea = new Rectangle();
	private Image logoImage;
	private Label title = new Label();

	public WindowControlPanel(int height, double rlAnchor, double spacing, boolean iconif,
							  boolean mxmize, boolean closeAll, String logo, String title) {
		btnClose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				((Stage) getScene().getWindow()).close();
				if (closeAll) System.exit(0);
			}
		});
		btnMxmize.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (((Stage) getScene().getWindow()).isMaximized())
					((Stage) getScene().getWindow()).setMaximized(false);
				else ((Stage) getScene().getWindow()).setMaximized(true);
				((MyStage) getScene().getWindow()).getWR().setVisible(!((Stage) getScene().getWindow()).isMaximized());
				((Rectangle) getScene().getRoot().getClip()).setWidth(getScene().getWindow().getWidth());
				((Rectangle) getScene().getRoot().getClip()).setHeight(getScene().getWindow().getHeight());
			}
		});
		btnIconif.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				((Stage) getScene().getWindow()).setIconified(true);
			}
		});
		if (logo != null) {
			logoImage = new Image(logo);
			if (logoImage != null)
				logoArea = new Rectangle(height, height, new ImagePattern(new Image(logo)));
		}
		if (title != null) {
			if (title.charAt(0) == '%') title = Helper.getI18nString(title.substring(1), Helper.LOCAL);
			this.title.setText(title);
		}
		getStylesheets().add("/styles/nodeStyles/windowCPStyle.css");
		btnIconif.setFocusTraversable(false);
		btnMxmize.setFocusTraversable(false);
		btnClose.setFocusTraversable(false);
		btnIconif.setTooltip(new Tooltip(Helper.getI18nString("iconified", Helper.LOCAL)));
		btnMxmize.setTooltip(new Tooltip(Helper.getI18nString("mxmized", Helper.LOCAL)));
		btnClose.setTooltip(new Tooltip(Helper.getI18nString("close", Helper.LOCAL)));
		btnClose.setCancelButton(true);
		setId("root");
		btnClose.setId("btnClose");
		btnMxmize.setId("btnMxmize");
		btnIconif.setId("btnIconif");
		logoArea.setId("logoArea");
		buttons.setId("buttons");
		setAllHeight(height);
		buttons.setSpacing(spacing);
		this.title.setStyle("-fx-font-size: " + height + "px;");
		if (iconif) buttons.getChildren().add(btnIconif);
		if (mxmize) buttons.getChildren().add(btnMxmize);
		buttons.getChildren().add(btnClose);
		setTopAnchor(buttons, 0.0);
		setRightAnchor(buttons, rlAnchor);
		setBottomAnchor(buttons, 0.0);
		setTopAnchor(logoArea, 0.0);
		setLeftAnchor(logoArea, rlAnchor + 5);
		setBottomAnchor(logoArea, 0.0);
		setTopAnchor(this.title, -15.0);
		setLeftAnchor(this.title, rlAnchor + 10 + logoArea.getWidth());
		setBottomAnchor(this.title, 0.0);
		AnchorPane.setRightAnchor(this, 0.0);
		AnchorPane.setTopAnchor(this, 10.0);
		AnchorPane.setLeftAnchor(this, 0.0);
		getChildren().addAll(logoArea, this.title, buttons);
	}

	public void setAllHeight(double height) {
		btnIconif.setPrefWidth(height);
		btnIconif.setPrefHeight(height);
		btnMxmize.setPrefWidth(height);
		btnMxmize.setPrefHeight(height);
		btnClose.setPrefWidth(height);
		btnClose.setPrefHeight(height);
		buttons.setPrefHeight(height);
	}

	public Button getCloseButton() {
		return btnClose;
	}

	public Image getLogo() {
		return logoImage;
	}

	public String getTitle() {
		return title.getText();
	}
}
