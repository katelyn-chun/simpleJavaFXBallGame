// ********** BONUS: ***********
 //  Ball gets smaller each time it gets hit
 //  Ball changes color each time it gets hit
 //  2 Balls at the same time
// *****************************

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.event.EventHandler;

import java.util.Random;

public class BallGame extends Application {
    private Pane graphicsPane;
    private BorderPane root;
    private Circle ball;
    private Circle ball2;
    private int ballRadius = 28;
    private Button pauseButton;
    private Button resetButton;
    private Text hitsText;
    private Text missesText;
    private HBox top;
    private int hits = 0;
    private int misses = 0;
    private int xVelocity = 1;
    private BallAnimation animation;
    private ButtonHandler buttonHandler;
    private Text stop;
    private int pauseCount = 0;
    private boolean isPaused = false;
    private boolean isEnded = false;
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {

        root = new BorderPane();

        graphicsPane = new Pane();
        graphicsPane.setPrefWidth(400);
        graphicsPane.setPrefHeight(400);

        setBackground();
        setText();
        setBall();
        setBall2();
        setButtons();

        buttonHandler = new ButtonHandler();

        resetButton.setOnAction(buttonHandler);
        pauseButton.setOnAction(buttonHandler);
        ball.setOnMouseClicked(new ClickHandler());
        ball2.setOnMouseClicked(new ClickHandler());

        animation = new BallAnimation();
        animation.start();

        root.setCenter(graphicsPane);
        Scene scene = new Scene(root);
        primaryStage.setTitle("Ball Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Handles the reset and pause buttons.
     */
    private class ButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            if (e.getSource() == resetButton) {
                resetGame();
            }
            if (e.getSource() == pauseButton) {
                animation.stop();
                if (pauseCount > 0) {
                    animation.start();
                    pauseCount = 0;
                    isPaused = false;
                }
                else {
                    pauseCount++;
                    isPaused = true;
                }
            }
        }
    }

    /**
     * Handles the clicking of a Circle.
     *  Circles decrease in size
     *  Circles' speed increases
     *  Circles change colour
     */
    private class ClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent e) {
            if ((e.getSource() == ball || e.getSource() == ball2) && !(isPaused) && !(isEnded)) {
                hits++;
                setText();
                xVelocity++;
                ball.setCenterX(0);
                ball.setRadius(ballRadius--);
                ball.setFill(randomColor());

                ball2.setCenterX(0);
                ball2.setRadius(ballRadius--);
                ball2.setFill(randomColor());
            }
        }
    }

    /**
     * Resets game when reset button is pressed.
     */
    public void resetGame() {
        graphicsPane.getChildren().remove(ball);
        graphicsPane.getChildren().remove(ball2);
        graphicsPane.getChildren().remove(stop);
        ballRadius = 28;
        setBall();
        setBall2();
        ball.setOnMouseClicked(new ClickHandler());
        ball2.setOnMouseClicked(new ClickHandler());

        hits = 0;
        misses = 0;
        setText();

        xVelocity = 1;
        isEnded = false;
        isPaused = false;
        animation.start();
    }

    /**
     * Animates a circle moving across the screen.
     *  When the number of misses reaches 5, animation stops and displayed message
     */
    private class BallAnimation extends AnimationTimer {
        @Override
        public void handle(long now) {
            isEnded = false;
            double x = ball.getCenterX();

            if (x + xVelocity >= 395 && x + xVelocity <= 400) {
                misses++;
                setText();
                ball.setCenterX(0);
                ball2.setCenterX(0);
            }
            else if (misses == 5) {
                animation.stop();
                isEnded = true;
                getStopMessage();
            }
            else {
                x += xVelocity;
                ball.setCenterX(x);
                ball2.setCenterX(x);
            }
        }
    }

    /**
     * Colour randomizer.
     * @return a random colour
     */
    private Color randomColor() {
        Random random = new Random();
        return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    /**
     * Sets the black background.
     */
    public void setBackground() {
        Rectangle background = new Rectangle(400, 400);
        background.setFill(Color.BLACK);
        root.getChildren().add(background);
    }

    /**
     * Sets the text for number of hits and misses.
     */
    public void setText() {
        hitsText = new Text("Hits: " + hits);
        hitsText.setFill(Color.WHITE);
        missesText = new Text("Misses: " + misses);
        missesText.setFill(Color.WHITE);

        top = new HBox(10, hitsText, missesText);
        top.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        top.setAlignment(Pos.TOP_LEFT);
        graphicsPane.getChildren().add(top);
    }

    /**
     * Adds the first ball to the pane.
     */
    public void setBall() {
        ball = new Circle(100, 200, ballRadius, Color.WHITE);
        graphicsPane.getChildren().add(ball);
    }

    /**
     * Adds the second ball to the pane.
     */
    public void setBall2() {
        ball2 = new Circle(100, 100, ballRadius, Color.WHITE);
        graphicsPane.getChildren().add(ball2);
    }

    /**
     * Adds the pause and reset button.
     */
    public void setButtons() {
        pauseButton = new Button("Pause");
        resetButton = new Button("Reset");

        HBox bottom = new HBox(10, pauseButton, resetButton);
        bottom.setAlignment(Pos.BOTTOM_RIGHT);
        root.setBottom(bottom);
    }

    /**
     * Sets the "Game Over" message for when number of misses reaches 5.
     */
    public void getStopMessage() {
        stop = new Text(120, 200, "Game Over");
        stop.setFill(Color.WHITE);
        stop.setFont(Font.font(40));
        graphicsPane.getChildren().add(stop);
    }
}