package сontrollers;

import helpers.functions.Helper;
import helpers.structures.Result;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Created by Святослав on 16.10.2016.
 */
public class ResultsController {

    String word, correct;
    int r;
    @FXML
    private VBox vbox;
    @FXML
    private Label lblAns;
    @FXML
    private Label lblCt;

    @FXML
    public void initialize() {
        r = 0;
		lblAns.setText(Helper.getI18nString("results", Helper.LOCAL) + ":");
		for (int i = 0; i < DictationController.getCurList().getWords().size(); i++) {
            if (DictationOptionsController.getTyp() == 0) {
                word = DictationController.getCurList().get(i).get(DictationOptionsController.getTranLeng());
                correct = DictationController.getCurList().get(i).getEng();
            } else {
                word = DictationController.getCurList().get(i).getEng();
                correct = DictationController.getCurList().get(i).get(DictationOptionsController.getTranLeng());
            }
            if (DictationController.getAnswers()[i].toLowerCase().equals(Helper.getCleanString(correct.toLowerCase())))
                ++r;
            vbox.getChildren().add(new Result(word, DictationController.getAnswers()[i], correct));
        }
        vbox.setPrefHeight(vbox.getChildren().size() * vbox.getChildren().get(0).prefHeight(-1));
        lblCt.setText(r + "/" + DictationController.getCurList().getWords().size());
    }
}
