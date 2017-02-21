package сontrollers;

import helpers.structures.MyLog;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ErrorLogController {

	@FXML
	private TextArea errorArea;

	@FXML
	public void initialize() {
        errorArea.setText(MyLog.getlogs());
        errorArea.requestFocus();
		errorArea.end();
	}
}
