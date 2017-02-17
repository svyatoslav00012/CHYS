package сontrollers;

import helpers.functions.Helper;
import helpers.nodes.DigitField;
import helpers.nodes.MyStage;
import helpers.structures.Result;
import helpers.structures.WList;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Created by Святослав on 13.10.2016.
 */
public class DictationController {

	FadeTransition ftLblHide, ftLblShow, ftField;
	private static int curIndex = 0;
	private static String[] answers;
	private static WList curList;
	private static ProgressPanel pp;
	private boolean justStarted = true;

	public static String[] getAnswers() {
		return answers;
	}

	public static WList getCurList() {
		return curList;
	}

	@FXML
	private AnchorPane root;
	@FXML
	private TextField fieldTran;
	@FXML
	private Label lenFrom;
	@FXML
	private Label lenTo;
	@FXML
	private Label lblWord;
	@FXML
	private Button btnPrev;
	@FXML
	private Button btnNext;

	@FXML
	public void initialize() {
		skipAll();
		curList = new WList();
		curList.clone(MyListsController.getCur());
		answers = new String[MyListsController.getCur().getWords().size()];
		answers[0] = "";
		for (int i = 0; i < answers.length; i++) answers[i] = "";
		if (DictationOptionsController.getRand()) curList.mix();
		MainController.setGausian(true);
		MyListsController.setGausian(true);
		if (DictationOptionsController.getTyp() == 0) {
			lenFrom.setText(Helper.getI18nString(DictationOptionsController.getTranLeng()));
			lblWord.setText(curList.get(curIndex).get(DictationOptionsController.getTranLeng()));
			lenTo.setText(Helper.getI18nString("en"));
		} else {
			lenFrom.setText(Helper.getI18nString("en"));
			lblWord.setText(curList.get(curIndex).getEng());
			lenTo.setText(Helper.getI18nString(DictationOptionsController.getTranLeng()));
		}
		btnPrev.setVisible(false);
		btnPrev.setTooltip(new Tooltip(Helper.getI18nString("prevWord")));
		btnNext.setTooltip(new Tooltip(Helper.getI18nString("nextWord")));
		if (curList.getWords().size() < 2) btnNext.setVisible(false);
		initFieldTran();
		initFades();
		pp = new ProgressPanel(40);
		root.getChildren().add(pp);
	}

	public void skipAll(){
		justStarted = true;
		curIndex = 0;
	}

	public void initFades() {
		ftLblHide = new FadeTransition(Duration.millis(250), lblWord);
		ftLblShow = new FadeTransition(Duration.millis(450), lblWord);
		ftField = new FadeTransition(Duration.millis(700), fieldTran);
		ftLblHide.setFromValue(1.0);
		ftLblHide.setToValue(0.0);
		ftLblShow.setFromValue(0.0);
		ftLblShow.setToValue(1.0);
		ftField.setFromValue(0.0);
		ftField.setToValue(1.0);
		ftLblHide.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				setLblWord();
				if(justStarted){
					pp.initWR();
					justStarted = false;
				}
				ftLblShow.play();
			}
		});
	}

	public void setLblWord() {
		if (DictationOptionsController.getTyp() == 0)
			lblWord.setText(curList.get(curIndex).get(DictationOptionsController.getTranLeng()));
		else lblWord.setText(curList.get(curIndex).getEng());
	}

	public void initFieldTran() {
		fieldTran.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB || event.isControlDown() && event.getCode() == KeyCode.RIGHT) {
					if (btnNext.isVisible()) btnNext.fire();
					event.consume();
				}
				if (event.isControlDown() && event.getCode() == KeyCode.LEFT) {
					if (btnPrev.isVisible()) btnPrev.fire();
					event.consume();
				}
			}
		});
		fieldTran.setOnKeyReleased(event -> {
			answers[curIndex] = fieldTran.getText();
			pp.progressRectangles.get(curIndex).setFilled(!fieldTran.getText().isEmpty());
		});
	}

	public void nextW(ActionEvent actionEvent) {
		pp.getPrRects().get(curIndex + 1).chooseThis();
	}

	public void prevW(ActionEvent actionEvent) {
		pp.getPrRects().get(curIndex - 1).chooseThis();
	}

	public void reload() {
		btnNext.setVisible(true);
		btnPrev.setVisible(true);
		if (curIndex == MyListsController.getCur().getWords().size() - 1) btnNext.setVisible(false);
		if (curIndex == 0) btnPrev.setVisible(false);
		ftLblHide.playFromStart();
		ftField.playFromStart();
		fieldTran.setText(answers[curIndex]);
	}

	public static boolean equalTitle(String title) {
		if (pp == null || pp.getParent() == null || pp.getScene().getWindow() == null || !((MyStage) pp.getScene().getWindow()).getWCP().getTitle().equals(title))
			return false;
		return true;
	}

	public static void finish(){
		if(curList == null || answers == null)return;
		if (!Helper.showConfirm(Helper.getI18nString("finish") + " " + Helper.getI18nString("dictation") + "?")) return;
		((Stage) pp.getScene().getWindow()).close();
		MyListsController.setGausian(false);
		MainController.setGausian(false);
		new Result().showResults();
	}

	public class ProgressPanel extends VBox {
		private HBox cur = new HBox(), notAns = new HBox(), rects = new HBox(), scoreHBox = new HBox(), rectsHBox = new HBox(), bottomPanel = new HBox();
		private ScrollPane rectsScroll = new ScrollPane(rects);
		private ObservableList<Item> progressRectangles = FXCollections.observableArrayList();
		private Label lblSize, lblNotAnswerCount, lblBegin, lblEnd;
		private DigitField fieldCurrent;
		private Button btnBack = new Button(), btnBackToCurr = new Button(), btnForwardToCurr = new Button(), btnFinish = new Button();
		private VBox score = new VBox(), left = new VBox(), right = new VBox();
		private int height;

		public ObservableList<Item> getPrRects() {
			return progressRectangles;
		}

		public ProgressPanel(int height) {
			this.height = height;
			initElems();
			initStyleClasses();
			initProgRects();
			progressRectangles.get(0).chooseThis();
			initBeginEnd();
		}

		public void initBeginEnd(){
			if(rectsScroll.getWidth() > 10+(height+rects.getSpacing())*progressRectangles.size())
			{
				lblBegin.setVisible(false);
				lblEnd.setVisible(false);
			}else
			lblEnd.setText(""+Math.min(10, progressRectangles.size()));
		}

		public void initElems() {
			initBottomPanel();
			initRectsHBox();
			initOnButtonPressed();
			AnchorPane.setBottomAnchor(this, 20.0);
			AnchorPane.setLeftAnchor(this, 100.0);
			AnchorPane.setRightAnchor(this, 100.0);
			VBox.setMargin(score, new Insets(0, 0, 20, 0));
			setFillWidth(true);
			setAlignment(Pos.CENTER);
			setSpacing(20);
			getChildren().addAll(rectsHBox, bottomPanel);
		}

		public void initWR(){
			((MyStage)lblWord.getScene().getWindow()).getWR().addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					adaptWidth();
					checkIfCurVisible();
					setBeginEnd();
				}
			});
		}

		public void initRects() {
			rects.setAlignment(Pos.CENTER);
			rects.setFillHeight(false);
			rects.setPrefHeight(height * 2);
			rects.setPadding(new Insets(0, 10, 0, 10));
			rects.setSpacing(10.0);
			rects.setPrefWidth(Math.max(480, getRectsHBoxWidth()));
		}

		public int getRectsHBoxWidth() {
			return curList.getWords().size() * (height + 10) + 10;
		}

		public void initRectsHBox() {
			initRects();
			lblBegin = new Label("0");
			lblEnd = new Label("10");
			btnBackToCurr.setMinSize(50, 50);
			btnBackToCurr.setVisible(false);
			btnBackToCurr.setTooltip(new Tooltip(Helper.getI18nString("dictation.backToCur")));
			btnForwardToCurr.setMinSize(50, 50);
			btnForwardToCurr.setVisible(false);
			btnForwardToCurr.setTooltip(new Tooltip(Helper.getI18nString("dictation.backToCur")));
			right.setFillWidth(true);
			right.setAlignment(Pos.CENTER_LEFT);
			right.setSpacing(0);
			right.getChildren().addAll(btnForwardToCurr, lblEnd);
			left.setFillWidth(true);
			left.setAlignment(Pos.CENTER_RIGHT);
			left.setSpacing(0);
			left.getChildren().addAll(btnBackToCurr, lblBegin);
			rectsScroll.setFitToHeight(true);
			rectsHBox.setAlignment(Pos.CENTER);
			rectsHBox.getChildren().addAll(left, rectsScroll, right);
			rectsScroll.hvalueProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					checkIfCurVisible();
					setBeginEnd();
				}
			});
			rectsScroll.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					if (event.isControlDown()) {
						if (event.getCode() == KeyCode.RIGHT && btnNext.isVisible()) {
							btnNext.fire();
							event.consume();
						}
						if (event.getCode() == KeyCode.LEFT && btnPrev.isVisible()) {
							btnPrev.fire();
							event.consume();
						}
					}
				}
			});
			rectsScroll.setOnScroll(new EventHandler<ScrollEvent>() {
				@Override
				public void handle(ScrollEvent event) {
					rectsScroll.setHvalue(rectsScroll.getHvalue() + (event.getDeltaY() / 1000));
				}
			});
		}

		public void initBottomPanel(){
			initScoreHBox();
			btnFinish.setMinSize(110,110);
			btnFinish.setFocusTraversable(false);
			btnFinish.setTooltip(new Tooltip(Helper.getI18nString("finish") + " " + Helper.getI18nString("dictation")));
			bottomPanel.setAlignment(Pos.CENTER);
			bottomPanel.setSpacing(100);
			bottomPanel.getChildren().addAll(score, btnFinish);
		}

		public void initScoreHBox() {
			fieldCurrent = new DigitField(1, curList.getWords().size());
			lblSize = new Label("/ " + curList.getWords().size());
			cur.getChildren().addAll(new Label(Helper.getI18nString("dictation.current")), fieldCurrent, lblSize);
			lblNotAnswerCount = new Label(curList.getWords().size() + "");
			btnBack.setPrefSize(40, 40);
			btnBack.setFocusTraversable(false);
			btnBack.setTooltip(new Tooltip(Helper.getI18nString("dictation.backToNotAns")));
			notAns.getChildren().addAll(new Label(Helper.getI18nString("dictation.lblNotAns")), lblNotAnswerCount, btnBack);
			notAns.setSpacing(10.0);
			score.setSpacing(10.0);
			score.getChildren().addAll(cur, notAns);
			scoreHBox.getChildren().add(score);
			scoreHBox.setAlignment(Pos.CENTER);
		}

		public void initStyleClasses() {
			getStylesheets().add("/styles/nodeStyles/progressPanelStyle.css");
			getStyleClass().add("panel");
			rectsScroll.getStyleClass().add("rectsScroll");
			rectsScroll.getContent().getStyleClass().add("scrollAnchor");
			score.getStyleClass().add("score");
			cur.getStyleClass().add("cur");
			btnBack.getStyleClass().add("button-icon");
			btnBackToCurr.getStyleClass().add("button-icon");
			btnForwardToCurr.getStyleClass().add("button-icon");
			btnBackToCurr.setId("btnBackToCurr");
			btnForwardToCurr.setId("btnForwardToCurr");
			btnBack.setId("btnBack");
			lblNotAnswerCount.setId("lblNotAnswerCount");
			btnFinish.getStyleClass().add("button-icon");
			btnFinish.setId("btnFinish");
		}

		public void initProgRects() {
			for (int i = 0; i < curList.getWords().size(); ++i) {
				progressRectangles.add(new Item(i, height));
			}
			rects.getChildren().addAll(progressRectangles);
		}

		public void adaptWidth() {
			rects.setPrefWidth(Math.max(((AnchorPane) getParent()).getWidth() - 320, curList.getWords().size() * (height + 10) + 10));
		}

		public void initOnButtonPressed() {
			btnBack.setOnAction(event -> {
						for (int i = 0; i < answers.length; ++i)
							if (answers[i].isEmpty()) {
								progressRectangles.get(i).chooseThis();
								if(!checkIfCurVisible())moveScrollTo(curIndex);
								return;
							}
					}
			);
			btnBackToCurr.setOnAction(action -> {
				moveScrollTo(curIndex);
			});
			btnForwardToCurr.setOnAction(action -> {
				moveScrollTo(curIndex);
			});
			btnFinish.setOnAction(action -> {
				finish();
			});
		}

		public void moveScrollTo(int index) {
			Timeline timeline = new Timeline();
			KeyValue kv = new KeyValue(rectsScroll.hvalueProperty(), getRectPosition(index));
			KeyFrame kf = new KeyFrame(Duration.millis(700), kv);
			timeline.getKeyFrames().add(kf);
			timeline.play();
		}

		public double getRectPosition(int index) {
			if(curIndex == 0)return 0.0;
			return (10 + (index) * (height + 10)) /(rects.getWidth() - 50);
		}

		public double getVisibleDiapason() {
			return rectsScroll.getWidth() / rects.getWidth();
		}

		public boolean checkIfCurVisible(){
			btnBackToCurr.setVisible(false);
			btnForwardToCurr.setVisible(false);
			Bounds c = getCurBounds(), s = getScrollBounds();
			if(c.getMaxX() < s.getMinX()){
				btnBackToCurr.setVisible(true);
				return false;
			}
			if(c.getMinX() > s.getMaxX()){
				btnForwardToCurr.setVisible(true);
				return false;
			}
			return true;
		}

		public void setBeginEnd(){
			if(rectsScroll.getWidth() > 10+(height+rects.getSpacing())*progressRectangles.size())
			{
				lblBegin.setVisible(false);
				lblEnd.setVisible(false);
				return;
			}
			lblBegin.setVisible(true);
			lblEnd.setVisible(true);
			int one = (int)(height + rects.getSpacing());
			int begin = ((int)(Math.abs(getRectsX()) + 10))/one + 1;
			int w = (int)rectsScroll.getWidth() - 10;
			int end = Math.min(begin+w/one, progressRectangles.size());
			lblBegin.setText(""+begin);
			lblEnd.setText(""+end);
			lblBegin.setTooltip(new Tooltip(begin+""));
			lblEnd.setTooltip(new Tooltip(end+""));
		}

		public double getRectsX(){
			return progressRectangles.get(0).localToScene(progressRectangles.get(0).getBoundsInLocal()).getMinX() - getScrollBounds().getMinX();
		}

		public Bounds getCurBounds(){
			return progressRectangles.get(curIndex).localToScene(progressRectangles.get(curIndex).getBoundsInLocal());
		}

		public Bounds getScrollBounds()
		{
			return rectsScroll.localToScene(rectsScroll.getBoundsInLocal());
		}

		class Item extends Region {
			private int num;
			private boolean isFilled = false, isCurr = false;

			public Item(int num, int height) {
				Tooltip tp = new Tooltip((num + 1) + " : " + DictationController.getCurList().get(num).getTask());
				Helper.hackTooltip(tp, Duration.ZERO, Duration.INDEFINITE);
				tp.setAutoHide(false);
				Tooltip.install(this, tp);
				setPrefHeight(height);
				setPrefWidth(height);
				getStyleClass().add("region");
				this.num = num;
				setOnMouseClicked(new javafx.event.EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						chooseThis();
					}
				});
			}

			public void setFilled(boolean condition) {
				int curSize = Integer.parseInt(lblNotAnswerCount.getText());
				if (condition && getStyleClass().indexOf("filled") == -1) {
					getStyleClass().add("filled");
					lblNotAnswerCount.setText((curSize - 1) + "");
				} else if (!condition && getStyleClass().indexOf("filled") != -1) {
					getStyleClass().remove("filled");
					lblNotAnswerCount.setText((curSize + 1) + "");
				}
				if (Integer.parseInt(lblNotAnswerCount.getText()) == 0) {
					lblNotAnswerCount.setId("complete");
					btnBack.setVisible(false);
				} else {
					lblNotAnswerCount.setId("lblNotAnswerCount");
					btnBack.setVisible(true);
				}
			}

			public boolean isFill() {
				return isFilled;
			}

			public void chooseThis() {
				setLblWord();
				answers[curIndex] = Helper.getCleanString(fieldTran.getText());
				if (answers[curIndex].length() == 0) progressRectangles.get(curIndex).setFilled(false);
				else progressRectangles.get(curIndex).setFilled(true);
				progressRectangles.get(curIndex).isCurr = false;
				progressRectangles.get(curIndex).getStyleClass().remove("current");
				getStyleClass().add("current");
				isCurr = true;
				curIndex = num;
				fieldCurrent.setValue(curIndex + 1);
				if(!checkIfCurVisible())moveScrollTo(curIndex);
				setBeginEnd();
				reload();
			}

			public boolean isCurrent() {
				return isCurr;
			}
		}

		public void chooseThis(int index) {
			progressRectangles.get(index - 1).chooseThis();
		}
	}
}
