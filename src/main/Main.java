package main;

import helpers.functions.FileHelper;
import helpers.functions.Helper;
import helpers.nodes.FadingStage;
import helpers.nodes.MyStage;
import helpers.nodes.WindowControlPanel;
import helpers.nodes.WindowResizer;
import helpers.structures.Properties;
import helpers.structures.WList;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import library.AppData;
import resources.fonts.MyFonts;

import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application implements Initializable {

	public static String SPLASH = "/resources/images/icons/used/check_yourself.png";
	public static String LOGO = "/resources/images/icons/used/logo-3.png";
	private static MyStage mainStage;

	Task bckgrndLoading = new Task() {                                                    //background loading
		@Override
		protected Object call() {
			try {
				System.out.println("loading...");
				updateMessage("Loading fonts...");
				MyFonts.loadFonts();
				Thread.sleep(500);
				updateMessage("Initializing properties...");
				Properties.initProperties();
				Thread.sleep(500);
				updateMessage("Initializing files...");
				AppData.initFiles("data.txt", "log.txt");
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						System.out.println("adding allWords...");
						AppData.getLists().add(new WList(0, Helper.getI18nString("allWords", Helper.LOCAL)));
					}
				});
				Thread.sleep(500);
				updateMessage("Reading data...");
				FileHelper.readData();
				Thread.sleep(500);
				updateMessage("Initializing Alert...");
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Helper.initAlert();
					}
				});
				Thread.sleep(500);
				System.out.println("finish loading");
			} catch (InterruptedException e) {
				e.printStackTrace();
				Helper.showError(e.getMessage());
			}
			return null;
		}
	};

	public static void main(String[] args) {
//		try {
		launch(args);
//		} catch (Exception e) {
//			e.printStackTrace();
//			//Helper.showException("Exception in Application start method", e);
//		}
	}

	public static Image getLogo() {
		return new Image(LOGO);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage = new FadingStage(StageStyle.TRANSPARENT);
		primaryStage.getIcons().add(new Image(LOGO));
		Region image = new Region();
		Label status = new Label("...");
		status.setStyle("" +
				"-fx-font-size: 30px;" +
				"-fx-effect: dropshadow(gaussian , white, 3, 1.0, 0, 0);"
		);
		image.setStyle(
				"-fx-background-image: url(" + SPLASH + ");" +
						"-fx-background-size: 90%;" +
						"-fx-background-position: center;" +
						"-fx-background-repeat: no-repeat;" +
						"-fx-effect: dropshadow(gaussian , white, 3, 1.0, 0, 0);" +
						"-fx-background-color: transparent;"
		);
		image.setPrefWidth(MyStage.SCREEN_WIDTH / 2);
		image.setPrefHeight((image.getPrefWidth() / 1540) * 650);
		ProgressIndicator indicator = new ProgressIndicator(ProgressIndicator.INDETERMINATE_PROGRESS);
		indicator.setPrefSize(200, 200);
		indicator.setStyle("" +
				"-fx-progress-color: red;" +
				"-fx-effect: dropshadow(gaussian , white, 3, 1.0, 0, 0);"
		);
		VBox root = new VBox(10, image, indicator, status);
		root.setAlignment(Pos.CENTER);
		root.setStyle("-fx-background-color: transparent;");
		primaryStage.setScene(new Scene(root));
		primaryStage.getScene().setFill(Color.TRANSPARENT);
		FadeTransition emission = new FadeTransition(Duration.millis(1500), root);
		emission.setFromValue(0.0);
		emission.setToValue(1.0);
		bckgrndLoading.messageProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue != null) {
					//if(status.equals("1"))
					status.setText(newValue);
					System.out.println("status updated");
				}
			}
		});
		bckgrndLoading.setOnSucceeded(success -> {
			((FadingStage) root.getScene().getWindow()).smoothlyClose();
			showMainWindow();
		});
		//bckgrndLoading.setOnFailed(fail -> Helper.showError(fail.getTarget()+"\n"+fail.getSource()+"\n"+"Fail loading application!"));
		emission.setOnFinished(action -> new Thread(bckgrndLoading).start());
		primaryStage.setOnShown(action -> emission.play());
		primaryStage.show();
	}

	public void showMainWindow() {
		mainStage = new MyStage("/fxmls/main.fxml", null, null, new WindowResizer(600, 600), new WindowControlPanel(40, 20.0, 10.0, true, true, true, LOGO, "CHYS 1.0"));
		mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				AppData.revalidKeys();
				System.exit(0);
			}
		});
		mainStage.getWCP().getCloseButton().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				AppData.revalidKeys();
				System.exit(0);
			}
		});
		mainStage.smoothlyShow();
		//Helper.combineTranslates("rus", "en", new File("translate.txt"));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}
