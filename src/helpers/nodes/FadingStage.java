package helpers.nodes;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Created by Svyatoslav on 19.02.2017.
 */
public class FadingStage extends WritableStage {
	double fin = getOpacity();
	Duration duration = new Duration(300);
	private Timeline emission;
	private Timeline hiding;

	public FadingStage(StageStyle style) {
		super();
		initStyle(style);
	}

	public void setFinalOpacity(double value) {        // USE THIS METHOD TO SET OPACITY! super method setOpacity(double value) DOESN'T WORK!
		fin = value;
		setOpacity(value);
	}

	public void setDuration(int millis) {
		duration = new Duration(millis);
	}

	public void smoothlyShow(double millis) {
		duration = new Duration(millis);
		smoothlyShow();
	}

	public void smoothlyShow() {                //you should use it if you want to Show Stage smoothly
		setOpacity(0.0);
		show();
		new Timeline(new KeyFrame(duration, new KeyValue(getWritableOpacity(), fin))).play();
	}

	public void smoothlyClose(double millis) {
		duration = new Duration(millis);
		smoothlyClose();
	}

	public void smoothlyClose() {            //you should use it if you want to Close Stage smoothly
		new Timeline(new KeyFrame(duration, new KeyValue(getWritableOpacity(), 0.0))).play();
		close();
	}
}
