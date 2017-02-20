package сontrollers;

import helpers.functions.FileHelper;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import library.AppData;

public class ErrorLogController {

	@FXML
	private TextArea errorArea;

	@FXML
	public void initialize() {
		errorArea.setText(FileHelper.readFile(AppData.getLog().getPath()));
		errorArea.requestFocus();
		errorArea.end();
	}
}
