package main;

import helpers.functions.FileHelper;
import helpers.functions.Helper;
import helpers.nodes.MyStage;
import helpers.nodes.WindowControllPanel;
import helpers.nodes.WindowResizer;
import javafx.application.Application;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import library.AppData;

import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application implements Initializable {

	private static String logo = "/resources/images/icons/used/logo-3.png";

	@Override
	public void start(Stage primaryStage) throws Exception {
		//MyFonts.loadFonts();
        FileHelper.loadConfig();
        FileHelper.readData();
        Helper.initAlert();
        primaryStage = new MyStage(
                "/fxmls/main.fxml",
                null,
                null,
                new WindowResizer(600, 600),
                new WindowControllPanel(
                        40,
                        20.0,
                        10.0,
                        true,
                        true,
                        true,
                        logo,
                        "CHYS 1.0"));
		primaryStage.setOnCloseRequest(event -> {
            AppData.revalidKeys();
            System.exit(0);
        });
		((MyStage) primaryStage).getWCP().getCloseButton().setOnAction(event -> {
            AppData.revalidKeys();
            System.exit(0);
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
