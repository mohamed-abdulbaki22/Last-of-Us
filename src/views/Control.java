package views;

import java.awt.Point;

import model.world.*;

import java.io.IOException;
import java.util.ArrayList;

import model.characters.Explorer;
import model.characters.Hero;
import model.characters.Medic;
import model.characters.Zombie;
import model.collectibles.Collectible;
import model.collectibles.Supply;
import model.collectibles.Vaccine;
import model.world.CharacterCell;
import model.world.CollectibleCell;
import model.world.TrapCell;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import engine.Game;
import exceptions.InvalidTargetException;
import exceptions.NoAvailableResourcesException;
import exceptions.NotEnoughActionsException;
import views.GUI;

public class Control {
	static ArrayList<Button> HeroButtons = new ArrayList<>();
	public static GridPane c = new GridPane();

	public static GridPane makeAvailableHeroesList() throws IOException {

		c.setVgap(10);
		c.setHgap(10);
		c.setAlignment(Pos.CENTER);
		// Game.loadHeroes("Heros.csv");
		 Image backgroundImage = new Image("gameBackground.jpg");

	        // Create a BackgroundImage with the loaded image
	        BackgroundImage backgroundImg = new BackgroundImage(backgroundImage,
	                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
	                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

	        // Create a Background with the BackgroundImage
	        Background background = new Background(backgroundImg);

	        // Set the background to the layout
	        c.setBackground(background);
		int j = 0;
		for (int i = 0; i < Game.availableHeroes.size(); i++) {
			String s = Game.availableHeroes.get(i).toString();
			Button h = new Button(s);
			h.setMinSize(200, 200);
			Hero hh = Game.availableHeroes.get(i);
			h.setOnMouseClicked(e -> {
				Game.startGame(hh);
				GUI.h = hh;
				mapUpdate();
				GUI.getWindow().setScene(GUI.s2);
			});
			HeroButtons.add(h);
		}
		int h = 0;
		for (int i = 0; i < HeroButtons.size(); i++) {
			// String s = Game.availableHeroes.get(i).toString();
			c.add(HeroButtons.get(i), j, h);
			j++;
			if (j == 4) {
				j = 0;
				h++;
			}
		}

		return c;
	}

	public static ArrayList adjacentCells(int i, int j) {
		ArrayList<Point> cells = new ArrayList<>();
		// ArrayList<Rectangle> adj = new ArrayList<>();
		cells.add(new Point(i, j));
		if (i + 1 <= 14)
			cells.add(new Point(i + 1, j));
		if (i - 1 >= 0)
			cells.add(new Point(i - 1, j));
		if (j + 1 <= 14)
			cells.add(new Point(i, j + 1));
		if (j - 1 >= 0)
			cells.add(new Point(i, j - 1));
		if (j + 1 <= 14 && i + 1 <= 14)
			cells.add(new Point(i + 1, j + 1));
		if (i - 1 >= 0 && j - 1 >= 0)
			cells.add(new Point(i - 1, j - 1));
		if (i - 1 >= 0 && j + 1 <= 14)
			cells.add(new Point(i - 1, j + 1));
		if (i + 1 <= 14 && j - 1 >= 0)
			cells.add(new Point(i + 1, j - 1));
		// for (int k = 0; k < cells.size(); k++) {
		// int xCell = cells.get(k).x;
		// int yCell = cells.get(k).y;
		// if (xCell < 15 && xCell >= 0 && yCell < 15 && yCell >= 0) {
		// adj.add(Game.MapIndices[xCell][yCell]);
		// }
		// }
		return cells;
	}

public static void mapUpdate() {
		
	
		
		for (int i = 0; i < Game.heroes.size(); i++) {
			Hero h = Game.heroes.get(i);
			ArrayList l = adjacentCells(h.getLocation().x, h.getLocation().y);
			System.out.println(h.getLocation().x + " " + h.getLocation().y);
			for (int j = 0; j < l.size(); j++) {
				int x = (int) ((Point) l.get(j)).getX();
				int y = (int) ((Point) l.get(j)).getY();
				//Game.MapIndices[x][y].setTextFill(Color.BLUEVIOLET);
				if (Game.map[x][y] instanceof CharacterCell) {
					if (((CharacterCell) Game.map[x][y]).getCharacter() instanceof Hero) {
						Game.MapIndices[x][y].setGraphic(null);
						Image hero = new Image("Joel.jpg");
						ImageView heroV = new ImageView(hero);
						heroV.setPreserveRatio(true);
						heroV.setFitHeight(20);
						heroV.setFitWidth(20);
						Game.MapIndices[x][y].setGraphic(heroV);
						//Game.MapIndices[x][y].setText("h");
					}

					else if (((CharacterCell) Game.map[x][y]).getCharacter() instanceof Zombie) {
						Game.MapIndices[x][y].setGraphic(null);
						Image zombie = new Image("zombie-png-6215.png.jpg");
						ImageView zombieV = new ImageView(zombie);
						zombieV.setPreserveRatio(true);
						zombieV.setFitHeight(20);
						zombieV.setFitWidth(20);
						Game.MapIndices[x][y].setGraphic(zombieV);
						//Game.MapIndices[x][y].setText("z");
					} else {
						Game.MapIndices[x][y].setGraphic(null);
						Image grass = new Image("gras.png.jpg");
						ImageView grassV = new ImageView(grass);
						grassV.setPreserveRatio(true);
						grassV.setFitHeight(20);
						grassV.setFitWidth(20);
						Game.MapIndices[x][y].setGraphic(grassV);
						//Game.MapIndices[x][y].setText("e");
					}
				} else if (Game.map[x][y] instanceof CollectibleCell) {
					if (((CollectibleCell) Game.map[x][y]).getCollectible() instanceof Supply) {
						Game.MapIndices[x][y].setGraphic(null);
						Image chest = new Image("Chest.jpg");
						ImageView chestV = new ImageView(chest);
						chestV.setPreserveRatio(true);
						chestV.setFitHeight(20);
						chestV.setFitWidth(20);
						Game.MapIndices[x][y].setGraphic(chestV);
						//Game.MapIndices[x][y].setText("S");
					} else {
						Game.MapIndices[x][y].setGraphic(null);
						Image vaccine = new Image("vaccine.png.jpg");
						ImageView vaccineV = new ImageView(vaccine);
						vaccineV.setPreserveRatio(true);
						vaccineV.setFitHeight(20);
						vaccineV.setFitWidth(20);
						Game.MapIndices[x][y].setGraphic(vaccineV);
						//Game.MapIndices[x][y].setText("V");
					}
				} else {
					Game.MapIndices[x][y].setGraphic(null);
					Image grass = new Image("gras.png.jpg");
					ImageView grassV = new ImageView(grass);
					grassV.setPreserveRatio(true);
					grassV.setFitHeight(20);
					grassV.setFitWidth(20);
					Game.MapIndices[x][y].setGraphic(grassV);
					//Game.MapIndices[x][y].setText("T");
				}
			}
		}
	}

	public static void endTurnGui() {
		try {
			Game.endTurn();
			for (int i = 0; i < Game.map.length; i++) {
				for (int j = 0; j < Game.map[i].length; j++) {
					Game.MapIndices[i][j].setGraphic(null);
				}
			}
			mapUpdate();
			if (Game.checkWin())
				WinGame.display();
			else if (Game.checkGameOver())
				Defeat.display();

			/*
			 * int c = 0; for (int i = 0; i < Game.map.length; i++) { for (int j
			 * = 0; j < Game.map.length; j++) { if (Game.map[i][j] instanceof
			 * CharacterCell && ((CharacterCell)
			 * (Game.map[i][j])).getCharacter() instanceof Hero) {
			 * System.out.print("h "); } else if (Game.map[i][j] instanceof
			 * CharacterCell && ((CharacterCell)
			 * (Game.map[i][j])).getCharacter() instanceof Zombie) {
			 * System.out.print("z "); c++; } else if (Game.map[i][j] instanceof
			 * CollectibleCell && ((CollectibleCell)
			 * (Game.map[i][j])).getCollectible() instanceof Vaccine) {
			 * System.out.print("v "); } else if (Game.map[i][j] instanceof
			 * CollectibleCell && ((CollectibleCell)
			 * (Game.map[i][j])).getCollectible() instanceof Supply) {
			 * System.out.print("s "); } else if (Game.map[i][j] instanceof
			 * TrapCell) { System.out.print("t "); } else
			 * System.out.print("e "); } System.out.println(); }
			 * System.out.println(c);
			 */

		} catch (NotEnoughActionsException e) {
			Alerts.display("NotEnoughActionsException", e.getMessage());
		} catch (InvalidTargetException e2) {
			Alerts.display("InvalidTargetException", e2.getMessage());
		}

	}

	// public void setTargetGui(){
	// GUI.h.setTarget(target);
	// }
	public static void cureGui() {
		try {
			GUI.getH().cure();
			mapUpdate();
			System.out.println(Game.heroes);
			if (Game.checkWin())
				WinGame.display();
			else if (Game.checkGameOver())
				Defeat.display();
		} catch (InvalidTargetException e) {
			Alerts.display("InvalidTargetException", e.getMessage());
		} catch (NoAvailableResourcesException e2) {
			Alerts.display("NoAvailableResourcesException", e2.getMessage());
		} catch (NotEnoughActionsException e3) {
			Alerts.display("NotEnoughActionsException", e3.getMessage());
		}
	}

	public static void winGame() {
		WinGame.display();
	}

	public static void useSpecialGui() {
		try {
			GUI.getH().useSpecial();
			if (GUI.getH() instanceof Explorer) {
				for (int i = 0; i < Game.map.length; i++) {
					for (int j = 0; j < Game.map[i].length; j++) {
						if (Game.map[i][j] instanceof CharacterCell
								&& ((CharacterCell) Game.map[i][j])
										.getCharacter() instanceof Hero) {
							Game.MapIndices[i][j].setGraphic(null);
							Image hero = new Image("Joel.jpg");
							ImageView heroV = new ImageView(hero);
							heroV.setPreserveRatio(true);
							heroV.setFitHeight(20);
							heroV.setFitWidth(20);
							Game.MapIndices[i][j].setGraphic(heroV);
							//Game.MapIndices[i][j].setText("h");
						} else if (Game.map[i][j] instanceof CharacterCell
								&& ((CharacterCell) (Game.map[i][j]))
										.getCharacter() instanceof Zombie) {
							Game.MapIndices[i][j].setGraphic(null);
							Image zombie = new Image("zombie-png-6215.png.jpg");
							ImageView zombieV = new ImageView(zombie);
							zombieV.setPreserveRatio(true);
							zombieV.setFitHeight(20);
							zombieV.setFitWidth(20);
							Game.MapIndices[i][j].setGraphic(zombieV);
							//Game.MapIndices[i][j].setText("z");
						} else if (Game.map[i][j] instanceof CollectibleCell
								&& ((CollectibleCell) (Game.map[i][j]))
										.getCollectible() instanceof Vaccine) {
							Game.MapIndices[i][j].setGraphic(null);
							Image vaccine = new Image("vaccine.png.jpg");
							ImageView vaccineV = new ImageView(vaccine);
							vaccineV.setPreserveRatio(true);
							vaccineV.setFitHeight(20);
							vaccineV.setFitWidth(20);
							Game.MapIndices[i][j].setGraphic(vaccineV);
							//Game.MapIndices[i][j].setText("v");
						} else if (Game.map[i][j] instanceof CollectibleCell
								&& ((CollectibleCell) (Game.map[i][j]))
										.getCollectible() instanceof Supply) {
							Game.MapIndices[i][j].setGraphic(null);
							Image chest = new Image("Chest.jpg");
							ImageView chestV = new ImageView(chest);
							chestV.setPreserveRatio(true);
							chestV.setFitHeight(20);
							chestV.setFitWidth(20);
							Game.MapIndices[i][j].setGraphic(chestV);
							//Game.MapIndices[i][j].setText("s");
						} else{
							Game.MapIndices[i][j].setGraphic(null);
							Image grass = new Image("gras.png.jpg");
							ImageView grassV = new ImageView(grass);
							grassV.setPreserveRatio(true);
							grassV.setFitHeight(20);
							grassV.setFitWidth(20);
							Game.MapIndices[i][j].setGraphic(grassV);
							//Game.MapIndices[i][j].setText("e");
						}
					}
				}
				if (Game.checkWin())
					WinGame.display();
				else if (Game.checkGameOver())
					Defeat.display();
			}
		} catch (NoAvailableResourcesException e) {
			Alerts.display("NoAvailableResourcesException", e.getMessage());
		} catch (InvalidTargetException e2) {
			Alerts.display("InvalidTargetException", e2.getMessage());
		}
	}

	public static void attackGUI() {
		try {
			GUI.getH().attack();
			mapUpdate();
			if (Game.checkWin())
				WinGame.display();
			else if (Game.checkGameOver())
				Defeat.display();
		} catch (InvalidTargetException e) {
			Alerts.display("InvalidTargetException", e.getMessage());
		} catch (NotEnoughActionsException e2) {
			Alerts.display("NotEnoughActionsException", e2.getMessage());
		}
	}

}
