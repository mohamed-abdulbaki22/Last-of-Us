package views;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;

import model.characters.*;
import engine.Game;
import exceptions.MovementException;
import exceptions.NotEnoughActionsException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.characters.Fighter;
import model.characters.Hero;

public class GUI extends Application {
	static Hero h;
	static Hero myHeroes[];
	static Label sheetH = new Label();

	// boolean[] arr = new boolean[8];

	public static Hero getH() {
		return h;
	}

	public static void setH(Hero h) {
		GUI.h = h;
	}

	Button startGame;
	static Button EndGame;
	Button Quit;
	private static Stage window;
	static Scene s2;
	static Button UP, DOWN, RIGHT, LEFT;
	public static Point clicked;
	static Label name;
	static Label Hp;
	static Label Maxy;
	static Label type;
	static Label points;
	static Label RM;
	static ProgressBar myHealth, mySupplies, myVaccines;
	static Label Healthy, Supplyy, Vacciny;
	static ArrayList<String> avs;

	// public static VBox HeroesDetails() {
	// Label tit = new Label("Remaining Heroes: ");
	// tit.setFont(Font.font("Algerian", 24));
	// avs = new ArrayList<>();
	// for (int i = 0; i < Game.availableHeroes.size(); i++) {
	// String l = Game.heroes.toString() + "\n" + "\n";
	// avs.add(l);
	// }
	// VBox v = new VBox(20);
	// v.getChildren().add(tit);
	// String sheet = "";
	// for (int i = 0; i < avs.size(); i++) {
	// sheet += avs.get(i);
	// // v.getChildren().add(avs.get(i));
	// }
	// sheetH.setText(sheet);
	// v.getChildren().add(sheetH);
	// return v;
	// }

	public static GridPane makeDashBoard() {
		GridPane v = new GridPane();
		name = new Label("Name: ");
		name.setTextFill(Color.WHITE);
		Maxy = new Label("MaxHp: ");
		Maxy.setTextFill(Color.WHITE);
		Maxy.setFont(Font.font("Algerian", 15));
		name.setFont(Font.font("Algerian", 15));
		Hp = new Label("HP: Maximum");
		Hp.setTextFill(Color.WHITE);
		Hp.setFont(Font.font("Algerian", 15));
		type = new Label("Type: Hero");
		type.setTextFill(Color.WHITE);
		type.setFont(Font.font("Algerian", 15));
		points = new Label("Action Points: " + 0);
		points.setTextFill(Color.WHITE);
		points.setFont(Font.font("Algerian", 15));
		RM = new Label("Remaining Heroes");
		RM.setTextFill(Color.WHITE);
		RM.setFont(Font.font("Algerian", 20));
		Healthy = new Label("Health indicator: ");
		Healthy.setTextFill(Color.WHITE);
		Supplyy = new Label("Supply Indicator: ");
		Supplyy.setTextFill(Color.WHITE);
		Vacciny = new Label("Vaccine indicator: ");
		Vacciny.setTextFill(Color.WHITE);
		
		Healthy.setFont(Font.font("Algerian", 15));
		Supplyy.setFont(Font.font("Algerian", 15));
		Vacciny.setFont(Font.font("Algerian", 15));
		v.setVgap(10);
		v.setHgap(10);
		GridPane.setConstraints(name, 0, 0);
		GridPane.setConstraints(Hp, 0, 3);
		GridPane.setConstraints(Maxy, 1, 3);
		GridPane.setConstraints(type, 0, 1);
		myHealth = new ProgressBar();
		mySupplies = new ProgressBar();
		myVaccines = new ProgressBar();

		GridPane.setConstraints(points, 0, 2);
		// GridPane.setConstraints(Healthy, 0, 3);
		GridPane.setConstraints(myHealth, 1, 3);
		GridPane.setConstraints(mySupplies, 1, 4);
		GridPane.setConstraints(Supplyy, 0, 4);
		GridPane.setConstraints(Vacciny, 0, 5);
		GridPane.setConstraints(myVaccines, 1, 5);
		GridPane.setConstraints(RM, 0, 6);
		v.getChildren().addAll(name, Hp, type, Supplyy, Vacciny, points, RM);

		return v;
	}

	public static void updateDash() { // Progress A7eh
		name.setText("Name: " + getH().getName());
		Hp.setText("HP: " + getH().getCurrentHp() + "/" + getH().getMaxHp());
		points.setText("Action Points: " + getH().getActionsAvailable());
		Supplyy.setText("Supply Indicator: "
				+ getH().getSupplyInventory().size());
		Vacciny.setText("Vaccine indicator: "
				+ getH().getVaccineInventory().size());
		String t = "";
		String rabie = "";
		for (int i = 0; i < Game.heroes.size(); i++) {
			rabie += (i + 1) + " - " + Game.heroes.get(i).toString2();
		}
		RM.setText("Remainig Heroes: " + "\n" + rabie);
		if (getH() instanceof Fighter)
			t = "Fighter";
		else if (getH() instanceof Medic)
			t = "Medic";
		else
			t = "Explorer";
		type.setText("Type: " + t);
		// double nowHealth = (getH().getCurrentHp() / getH().getMaxHp());
		// if (nowHealth <= 0)
		// myHealth.setProgress(0.0);
		// else if (nowHealth <= 0.1 && nowHealth >= 0)
		// myHealth.setProgress(0.1);
		// else if (nowHealth <= 0.2 && nowHealth >= 0.1)
		// myHealth.setProgress(0.2);
		// else if (nowHealth <= 0.3 && nowHealth >= 0.2)
		// myHealth.setProgress(0.3);
		// else if (nowHealth <= 0.4 && nowHealth >= 0.3)
		// myHealth.setProgress(0.4);
		// else if (nowHealth <= 0.5 && nowHealth >= 0.4)
		// myHealth.setProgress(0.5);
		// else if (nowHealth <= 0.6 && nowHealth >= 0.5)
		// myHealth.setProgress(0.6);
		// else if (nowHealth <= 0.7 && nowHealth >= 0.6)
		// myHealth.setProgress(0.7);
		// else if (nowHealth <= 0.8 && nowHealth >= 0.7)
		// myHealth.setProgress(0.8);
		// else if (nowHealth <= 0.9 && nowHealth >= 0.8)
		// myHealth.setProgress(0.9);
		// else
		// myHealth.setProgress(1.0);
		// // System.out.println(nowHealth);
		// double[] num = { 0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9,
		// 1.0 };
		// int indexH = (int) (nowHealth * 10);
		// System.out.println(num[indexH]);
		//
		// myHealth.setProgress(num[indexH]);
		// HeroesDetails();
	}

	public static Scene chooseHero() {
		Scene s = null;
		try {
			s = new Scene(Control.makeAvailableHeroesList(), 1000, 1000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	public static Scene makeScenePlay() {
		try {
			Game.loadHeroes("Heros.csv");
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		EndGame.setWrapText(true);
		Button EndTurn = new Button("End Turn");
		// EndTurn.setPrefSize(100, 100);
		EndTurn.setWrapText(true);
		// cure button
		Button cure = new Button("cure");
		cure.setWrapText(true);
		Button useSpecial = new Button("Use Special Action");
		useSpecial.setWrapText(true);
		Button attack = new Button("Attack");
		attack.setWrapText(true);
		BorderPane layout2 = new BorderPane();
		layout2.setLeft(makeDashBoard());
		GridPane grid = Game.makeGrid();
		EndTurn.setOnAction(e -> {
			Control.endTurnGui();
			// System.out.println(Game.heroes);
			GUI.updateDash();
			Game.SetHero.setVisible(false);
			Game.SetTrarget.setVisible(false);
		});
		cure.setOnAction(e -> {
			Control.cureGui();
			GUI.updateDash();
			Game.SetHero.setVisible(false);
			Game.SetTrarget.setVisible(false);
		});
		useSpecial.setOnAction(e -> {
			Control.useSpecialGui();
			GUI.updateDash();
			Game.SetHero.setVisible(false);
			Game.SetTrarget.setVisible(false);
		});
		attack.setOnAction(e -> {
			Control.attackGUI();
			GUI.updateDash();
			Game.SetHero.setVisible(false);
			Game.SetTrarget.setVisible(false);
		});
		layout2.setCenter(grid);
		GridPane hbox = new GridPane();
		hbox.setAlignment(Pos.TOP_CENTER); // Set alignment to top center
		DOWN = new Button("↓");
		DOWN.setOnAction(e -> {
			Game.SetHero.setVisible(false);
			Game.SetTrarget.setVisible(false);
			try {
				// System.out.println(Game.heroes);
				h.move(Direction.DOWN);
				Control.mapUpdate();
			} catch (MovementException e1) {
				Alerts.display("WARNING", e1.getMessage());
			} catch (NotEnoughActionsException e2) {
				Alerts.display("WARNING", e2.getMessage());
			}
			GUI.updateDash();
		});
		UP = new Button("↑");
		UP.setOnAction(e -> {
			// GUI.updateDash();
			Game.SetHero.setVisible(false);
			Game.SetTrarget.setVisible(false);
			try {
				h.move(Direction.UP);
				Control.mapUpdate();
			} catch (MovementException e1) {
				Alerts.display("WARNING", e1.getMessage());
			} catch (NotEnoughActionsException e2) {
				Alerts.display("WARNING", e2.getMessage());
			}
			GUI.updateDash();
		});
		RIGHT = new Button("→");
		RIGHT.setOnAction(e -> {
			// GUI.updateDash();
			Game.SetHero.setVisible(false);
			Game.SetTrarget.setVisible(false);
			try {
				h.move(Direction.RIGHT);
				Control.mapUpdate();
			} catch (MovementException e1) {
				Alerts.display("WARNING", e1.getMessage());
			} catch (NotEnoughActionsException e2) {
				Alerts.display("WARNING", e2.getMessage());
			}
			GUI.updateDash();
		});
		LEFT = new Button("←");
		LEFT.setOnAction(e -> {
			// GUI.updateDash();
			Game.SetHero.setVisible(false);
			Game.SetTrarget.setVisible(false);
			try {
				h.move(Direction.LEFT);
				Control.mapUpdate();
			} catch (MovementException e1) {
				Alerts.display("WARNING", e1.getMessage());
			} catch (NotEnoughActionsException e2) {
				Alerts.display("WARNING", e2.getMessage());
			}
			GUI.updateDash();
		});
		// ProgressBar b = new ProgressBar(getH().getCurrentHp());
		GridPane.setConstraints(EndGame, 0, 0);
		GridPane.setConstraints(cure, 1, 0);
		GridPane.setConstraints(useSpecial, 2, 0);
		GridPane.setConstraints(attack, 3, 0);
		GridPane.setConstraints(EndTurn, 4, 0);
		GridPane.setConstraints(Game.SetHero, 3, 1); // c
		GridPane.setConstraints(Game.SetTrarget, 2, 1); // c
		GridPane.setConstraints(UP, 6, 0);
		GridPane.setConstraints(DOWN, 6, 1);
		GridPane.setConstraints(LEFT, 5, 1);
		GridPane.setConstraints(RIGHT, 7, 1);
		hbox.setHgap(20);
		hbox.setVgap(20);
		hbox.setPadding(new Insets(0, 0, 30, 0));
		hbox.getChildren().addAll(EndGame, cure, useSpecial, attack, EndTurn,
				Game.SetTrarget, Game.SetHero, UP, DOWN, RIGHT, LEFT);
		layout2.setBottom(hbox);
//		 VBox v = new VBox(20);
//		 Label m = new Label("");
//		 v.getChildren().addAll(m);
//		 layout2.setRight(v);
		Image backgroundImage = new Image("msm.jpg");

		// Create a BackgroundSize object to fit the scene
		BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO,
				BackgroundSize.AUTO, true, true, true, false);

		// Create a BackgroundImage object with the image, repeat option, and
		// sizing
		BackgroundImage backgroundImg = new BackgroundImage(backgroundImage,
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.REPEAT,
				BackgroundPosition.DEFAULT, backgroundSize);

		// Create a Background object with the background image
		Background background = new Background(backgroundImg);
		// System.out.println(Game.availableHeroes);
		layout2.setBackground(background);
		Scene s = new Scene(layout2, 1000, 1000);
		return s;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		window.setOnCloseRequest(e -> {
			e.consume();
			closeProgram();
		});
		// setting background
		// Create the background image object
		Image backgroundImage = new Image("Sunny.jpg");
		Image image = new Image("Blue.jpg");
		BackgroundImage backgroundImg = new BackgroundImage(backgroundImage,
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		BackgroundImage backgroundImg1 = new BackgroundImage(image,
				BackgroundRepeat.REPEAT, BackgroundRepeat.SPACE,
				BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
		// scene1
		// Create the background
		Background background = new Background(backgroundImg);
		Background myStart = new Background(backgroundImg1);
		// setting icon
		Image logoImage = new Image("Blue.jpg");
		// Replace with the actual path to your logo image
		primaryStage.getIcons().add(logoImage);
		Label l1 = new Label("Wow");
		startGame = new Button("Start the Game");
		startGame.setPrefHeight(50);
		startGame.setPrefWidth(200);
		// startGame.setBackground(myStart);
		EndGame = new Button("Quit");
		EndGame.setOnAction(e -> {
			closeProgram();
		});
		Quit = new Button("Quit"); // //////////c
		Quit.setPrefSize(200, 50);
		Quit.setOnAction(e -> { // //////////c
			closeProgram(); // //////////c
		});
		// Button b2 = new Button("restore scene 1");
		VBox layout1 = new VBox(15);
		layout1.setAlignment(Pos.CENTER);
		layout1.setBackground(background);
		// Label start = new Label("Are You ready to play?");
		layout1.getChildren().addAll(startGame, Quit); // ////c

		Scene s1 = new Scene(layout1, 1000, 1000);
		// scene2
		s2 = makeScenePlay();
		startGame.setOnAction(e -> primaryStage.setScene(chooseHero()));
		// b2.setOnAction(e -> primaryStage.setScene(s1));
		// Control.setVisibility(0,0);
		// primaryStage
		// Control.SetGuiVisibility(0,0) ;
		Control.mapUpdate();
		primaryStage.setTitle("The Last of Us");
		primaryStage.setScene(s1);
		primaryStage.show();
	}

	public static Stage getWindow() {
		return window;
	}

	public static void main(String[] args) throws IOException {
		launch(args);
		// Game.loadHeroes("Heros.csv");
	}

	private void closeProgram() {
		Boolean res = Confirm.display("Closing", "Are you sure?");
		if (res)
			window.close();
	}

	// @Override
	// public void handle(ActionEvent event) {
	// if(event.getSource() == startGame){
	// Fighter h = new Fighter("Bob",23,23,23);
	// Game.startGame(h);
	// }
	//
	// }
}
