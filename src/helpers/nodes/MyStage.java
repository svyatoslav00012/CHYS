package helpers.nodes;

import helpers.functions.Helper;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import library.AppData;
import main.Main;

import java.awt.*;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Created by Svyatoslav on 26.10.2016.
 */
public class MyStage extends Stage {

	public static final double SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static final double SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	private static double prevX = 0, prevY = 0;
	private double startX = -1, startY = -1;
	WindowControllPanel wcp;
	WindowResizer wr;

	public MyStage(String fxml, Modality modality, Window owner, WindowResizer wr, WindowControllPanel wcp) {
		try {
			this.wcp = wcp;
			this.wr = wr;
			if (getClass().getResource(fxml) != null)
				setScene(new Scene(new FXMLLoader(getClass().getResource(fxml), ResourceBundle.getBundle("resources.bundles.Locale", new Locale(AppData.getSettings().getLeng()))).load()));
			else {
				Helper.showError("Invalid scene for '" + wcp.getTitle() + "'\n" + fxml + " does not available!");
				return;
			}
			if(modality == null){
				setX(prevX = (prevX + 50) % (SCREEN_WIDTH - getWidth()));
				setY(prevY = (prevY + 50) % (SCREEN_HEIGHT - getHeight()));
			}
			setTitle(wcp.getTitle());
			if (!wcp.getTitle().equals("CHYS 1.0")) {
				if (modality == null) getScene().getRoot().setStyle("-fx-border-color: blue;");
				else getScene().getRoot().
						setStyle("-fx-border-color: red;" +
								"-fx-border-radius: 50px;" +
								"-fx-background-radius: 50px;");
			}
			getScene().getStylesheets().add("/styles/templateStyles/windowStyle.css");
			if (wcp.getLogo() != null) getIcons().add(wcp.getLogo());
			else if (Main.getLogo() != null) getIcons().add(Main.getLogo());
			initStyle(StageStyle.TRANSPARENT);
			initModality(modality);
			initOwner(owner);
			getScene().setFill(Color.TRANSPARENT);
			((AnchorPane) getScene().getRoot()).getChildren().add(wcp);
			if (wr != null) ((AnchorPane) getScene().getRoot()).getChildren().add(wr);
			else setResizable(false);
			getScene().setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (isMaximized() || (wr != null && wr.isPressed())) return;
					startX = event.getScreenX();
					startY = event.getScreenY();
				}
			});
			getScene().setOnMouseReleased(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					startX = -1;
					startY = -1;
				}
			});
			getScene().setOnMouseDragged(new EventHandler<MouseEvent>() {
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
		} catch (IOException e) {
			e.printStackTrace();
			Helper.showException(e.toString(), e);
		}
	}

	public WindowControllPanel getWCP() {
		return wcp;
	}

	public WindowResizer getWR() {
		return wr;
	}
}
