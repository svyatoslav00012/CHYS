package helpers.nodes;

import helpers.functions.Helper;
import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Svyatoslav on 11.02.2017.
 */
public class MyNotification extends WritableStage {
	public static final int COMPLETE = 0;
	public static final int INFO = 1;
	public static final int WARNING = 2;
	public static final int ERROR = 3;
	private static final int LIMIT_SIZE = ((int) MyStage.SCREEN_HEIGHT - 30) / 210;
	private static final String[] ICONS = {
			"complete-icon",
			"info-icon",
			"warning-icon",
			"error-icon"
	};
	public static ArrayList<MyNotification> windows = new ArrayList<>();
	private static Timeline moveDown = new Timeline();
	private static Comparator<MyNotification> comparator = new Comparator<MyNotification>() {
		@Override
		public int compare(MyNotification o1, MyNotification o2) {
			if (o1.getType() < o2.getType()) return 1;
			if (o1.getType() > o2.getType()) return -1;
			return 0;
		}
	};
	private static int globalMoving = 0;                                                                                // when notification starting showing, it's increases, when finish - decreases(When all finished - it's 0)
	private static Queue<MyNotification> next = new PriorityQueue<>(comparator);
	public static final String WORD_ADDED = Helper.getI18nString("wordSuccAdded", Helper.NOTIFICATIONS);
	public static final String WORD_EDITED = Helper.getI18nString("wordSuccEdited", Helper.NOTIFICATIONS);
	public static final String WORD_DELETED = Helper.getI18nString("wordSuccDeleted", Helper.NOTIFICATIONS);
	public static final String LIST_CREATED = Helper.getI18nString("listSuccCreated", Helper.NOTIFICATIONS);
	public static final String LIST_EDITED = Helper.getI18nString("listSuccEdited", Helper.NOTIFICATIONS);
	public static final String LIST_DUPLICATED = Helper.getI18nString("listSuccDuplicated", Helper.NOTIFICATIONS);
	public static final String LIST_DELETED = Helper.getI18nString("listSuccDeleted", Helper.NOTIFICATIONS);
	public static final String CHANGES_APPLIED = Helper.getI18nString("changesApplied", Helper.NOTIFICATIONS);
	Thread notifThread = new Thread();

	//nodes
	private Label title = new Label(), message = new Label();
	private Region icon = new Region();
	private AnchorPane root = new AnchorPane();
	private int type;
	//animations
	private KeyValue moving = new KeyValue(getWritableX(), MyStage.SCREEN_WIDTH - 450);
	private KeyValue makeVisible = new KeyValue(getWritableOpacity(), 1.0);
	private KeyValue makeInvisible = new KeyValue(getWritableOpacity(), 0.0);
	private Timeline emission = new Timeline(new KeyFrame(Duration.millis(500), moving, makeVisible));
	private PauseTransition minimum, visiblePeriod;
	private Timeline hiding = new Timeline(new KeyFrame(Duration.millis(500), action -> close(), makeInvisible));
	private SequentialTransition existence;
	public MyNotification(int type, String message, double y) {
		this.type = type;
		this.message.setText(message);
		initAnchors();
		initElems();
		initAnimation();
		setAlwaysOnTop(true);
		setOpacity(0);
		setX(MyStage.SCREEN_WIDTH);
		setY(y);
		initStyle(StageStyle.TRANSPARENT);
		setScene(new Scene(root, 400, 200));
		getScene().setFill(Color.TRANSPARENT);
	}

	public static void showMessage(int type, String message) {
		if (type == ERROR) Helper.log(message);
		next.add(new MyNotification(type, message, 0));
		//if (windows.size() > 0 && windows.size() >= LIMIT_SIZE && !windows.get(0).isMinimunRunning()) windows.get(0).hideNotif();
		addFromQueue();
	}

	public static void push(int index) {
		moveDown = new Timeline();
		for (int i = index; i < windows.size(); ++i) {
			KeyValue moving = new KeyValue(windows.get(i).getWritableY(), MyStage.SCREEN_HEIGHT - (220 + i * 210));
			moveDown.getKeyFrames().add(new KeyFrame(Duration.millis(500), moving));
		}
		moveDown.setOnFinished(action -> globalMoving--);
		globalMoving++;
		moveDown.play();
	}

	public static void addFromQueue() {
		try {
			while (windows.size() < LIMIT_SIZE && !next.isEmpty()) {
				//if(globalMoving != 0)
				MyNotification n = next.poll();
				n.setY(Math.max(MyStage.SCREEN_HEIGHT - (220 + windows.size() * 210), 10));
				windows.add(n);
				if (windows.size() > 0) n.showNotif();
			}
			printStatus();
		} catch (NoSuchElementException e) {
			Helper.showException("Exception during adding Notification from 'next'(Queue) to 'windows'\nMyNotifications.addFromQueue", e);
		}
	}

	public static void printStatus() {
		System.out.println("-------\nADDED!\nwindows:");
		for (int i = 0; i < windows.size(); ++i) System.out.println(printNotif(windows.get(i)));
		System.out.println("next:");
		for (MyNotification n : next) System.out.println(printNotif(n));
		System.out.println("-------");
	}

	public static String printNotif(MyNotification notifocation) {
		return ICONS[notifocation.getType()].substring(0, ICONS[notifocation.getType()].indexOf('-')) + " " + notifocation.getTime();
	}

	public static void showTestNotifications() {
		Button showComplete = new Button("Complete"), showInfo = new Button("Info"), showWarning = new Button("Warning"), showError = new Button("Error");
		showComplete.setOnAction(action -> showMessage(COMPLETE, "complete test message"));
		showInfo.setOnAction(action -> showMessage(INFO, "info test message"));
		showWarning.setOnAction(action -> showMessage(WARNING, "warning test message"));
		showError.setOnAction(action -> showMessage(ERROR, "error test message"));
		VBox buttons = new VBox(showComplete, showInfo, showWarning, showError);
		buttons.setSpacing(20);
		buttons.setAlignment(Pos.CENTER);
		Stage test = new Stage();
		test.setScene(new Scene(buttons, 200, 200));
		test.show();
	}

	public boolean isMinimunRunning() {
		return minimum.getStatus() == Animation.Status.RUNNING;
	}

	public int getType() {
		return type;
	}

	public String getTime() {
		return title.getText();
	}

	public Animation getExistense() {
		return existence;
	}

	public void initAnchors() {
		AnchorPane.setLeftAnchor(icon, 0.0);
		AnchorPane.setTopAnchor(icon, 20.0);
		AnchorPane.setLeftAnchor(title, 10.0);
		AnchorPane.setTopAnchor(title, -5.0);
		AnchorPane.setLeftAnchor(message, 190.0);
		AnchorPane.setTopAnchor(message, 0.0);
	}

	public void initElems() {
		title.setText(new SimpleDateFormat("HH:mm:ss").format(new Date()));
		title.getStyleClass().add("notif-title");
		message.setPrefSize(200.0, 200.0);
		message.setAlignment(Pos.CENTER_LEFT);
		message.setWrapText(true);
		message.getStyleClass().add("notif-message");
		icon.setPrefSize(170.0, 170.0);
		icon.setId(ICONS[type]);
		System.out.println(icon.getId());
		icon.getStyleClass().add("notif-icon");
		Tooltip.install(root, new Tooltip(Helper.getI18nString("press2xToClose", Helper.LOCAL)));
		root.getStylesheets().addAll("/styles/templateStyles/windowStyle.css", "/styles/templateStyles/myNotifications.css");
		root.setOnMouseClicked(action -> {
			if(action.getClickCount() >= 2){
				existence.playFrom("end");
			}
		});
		root.getChildren().addAll(title, message, icon);
	}

	public void initAnimation() {
		emission.statusProperty().addListener(new ChangeListener<Animation.Status>() {
			@Override
			public void changed(ObservableValue<? extends Animation.Status> observable, Animation.Status oldValue, Animation.Status newValue) {
				if(newValue == Animation.Status.RUNNING)globalMoving++;
				if(newValue == Animation.Status.STOPPED)globalMoving--;
			}
		});
		minimum = new PauseTransition(Duration.seconds(2));
		minimum.setOnFinished(action -> {
			if (!next.isEmpty() && windows.size() == LIMIT_SIZE && windows.indexOf(this) == 0)
				existence.playFrom("end");
		});
		visiblePeriod = new PauseTransition(Duration.seconds(3 + countWordsInMessage()));
		existence = new SequentialTransition(emission, minimum, visiblePeriod);
		existence.setOnFinished(action -> {
			int index = windows.indexOf(this);
			if (index == -1) {
				Helper.showError("Error in MyNotification.initAnimation() existence.setOnFinished()\n No such notification in 'windows'");
				return;
			}
			//windows.remove(index);
			windows.remove(this);
			hiding.play();
			push(index);    // Может не работать
			addFromQueue();
		});
	}

	public void showNotif() {
		show();
		existence.play();
	}

	public void hideNotif() {
		if (visiblePeriod.getStatus() == Animation.Status.RUNNING) existence.playFrom("end");
	}

	public int countWordsInMessage() {
		StringBuffer s = new StringBuffer(message.getText() + " ");
		int wordCount = 0;
		while (s.length() != 0) {
			while (s.length() > 0 && s.charAt(0) == ' ') s.deleteCharAt(0);
			if (s.length() != 0) wordCount++;
			else break;
			s.delete(0, s.indexOf(" "));
		}
		return wordCount;
	}

}
