package main;

import helpers.functions.Helper;
import resources.fonts.MyFonts;
import helpers.nodes.MyStage;
import helpers.nodes.WindowControllPanel;
import helpers.nodes.WindowResizer;
import helpers.functions.FileHelper;
import helpers.structures.Properties;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import library.AppData;

import java.io.File;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application implements Initializable {

	public static String logo = "/resources/images/icons/used/logo-3.png";

	@Override
	public void start(Stage primaryStage) throws Exception {
		MyFonts.loadFonts();
		Properties.initProperties();
		AppData.initFiles("data.txt", "log.txt");
		FileHelper.readData();
		Helper.initAlert();
		primaryStage = new MyStage("/fxmls/main.fxml", null, null, new WindowResizer(600, 600), new WindowControllPanel(40, 20.0, 10.0, true, true, true, logo, "CHYS 1.0"));
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				AppData.revalidKeys();
				System.exit(0);
			}
		});
		((MyStage) primaryStage).getWCP().getCloseButton().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				AppData.revalidKeys();
				System.exit(0);
			}
		});
		primaryStage.show();
		//Helper.combineTranslates("rus", "en", new File("translate.txt"));
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public static Image getLogo() {
		return new Image(logo);
	}
}
