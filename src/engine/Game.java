package engine;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import exceptions.InvalidTargetException;
import exceptions.MovementException;
import exceptions.NoAvailableResourcesException;
import exceptions.NotEnoughActionsException;
import model.characters.Direction;
import model.characters.Explorer;
import model.characters.Fighter;
import model.characters.Hero;
import model.characters.Medic;
import model.characters.Zombie;
import model.collectibles.Supply;
import model.collectibles.Vaccine;
import model.world.Cell;
import model.world.CharacterCell;
import model.world.CollectibleCell;
import model.world.TrapCell;
import views.*;

public class Game {

	public static model.characters.Character temp;
	public static Button SetHero = new Button("SetHero");
	public static Button SetTrarget = new Button("SetTarget");
	public static Cell[][] map = new Cell[15][15];
	public static Button[][] MapIndices = new Button[15][15];
	public static ArrayList<Hero> availableHeroes = new ArrayList<Hero>();
	public static ArrayList<Hero> heroes = new ArrayList<Hero>();
	public static ArrayList<Zombie> zombies = new ArrayList<Zombie>();
	public static int vaccinesUsed = 0;
	private static final int GRID_SIZE = 15;
	private static final int CELL_SIZE = 40;

	public static void loadHeroes(String filePath) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			Hero hero = null;
			switch (content[1]) {
			case "FIGH":
				hero = new Fighter(content[0], Integer.parseInt(content[2]),
						Integer.parseInt(content[4]),
						Integer.parseInt(content[3]));
				break;
			case "MED":
				hero = new Medic(content[0], Integer.parseInt(content[2]),
						Integer.parseInt(content[4]),
						Integer.parseInt(content[3]));
				break;
			case "EXP":
				hero = new Explorer(content[0], Integer.parseInt(content[2]),
						Integer.parseInt(content[4]),
						Integer.parseInt(content[3]));
				break;
			}
			availableHeroes.add(hero);
			// System.out.println(availableHeroes.get(0));
			line = br.readLine();

		}
		br.close();

	}

	public static GridPane makeGrid() {
		GridPane gameMap = new GridPane();
		gameMap.setAlignment(Pos.CENTER);
		gameMap.setVgap(5);
		gameMap.setHgap(5);
		gameMap.setPadding(new Insets(0, 0, 0, 0));
		// Load the icon image
		Image iconImage = new Image("file:path/to/your/icon.png");
		// Create an ImageView with the icon image
		ImageView iconImageView = new ImageView(iconImage);
		// Set the icon as the graphic of the button
		for (int x = 0; x < GRID_SIZE; x++) {
			for (int y = 0; y < GRID_SIZE; y++) {
				MapIndices[x][y] = new Button();
				int X = x;
				int Y = y;

				SetHero.setVisible(false);
				SetTrarget.setVisible(false);
				MapIndices[x][y].setPrefSize(CELL_SIZE, CELL_SIZE);
				MapIndices[x][y]
						.setOnAction(e -> {
							if (map[X][Y] instanceof CharacterCell
									&& ((CharacterCell) map[X][Y])
											.getCharacter() instanceof Hero) {
								SetHero.setVisible(true);
							}
							if (map[X][Y] instanceof CharacterCell) {
								temp = ((CharacterCell) map[X][Y])
										.getCharacter();
								SetTrarget.setVisible(true);
							}
							GUI.updateDash();
//							GUI.HeroesDetails();
						});
				SetHero.setOnAction(e -> {
					GUI.setH((Hero) temp);
					GUI.updateDash();
					SetHero.setVisible(false);
					SetTrarget.setVisible(false);
				});
				SetTrarget.setOnAction(e -> {
					GUI.getH().setTarget(temp);
					SetHero.setVisible(false);
					SetTrarget.setVisible(false);
				});

				// System.out.println(map[X][Y]);
				// if (map[X][Y] instanceof CharacterCell) {
				// if (((CharacterCell) map[X][Y]).getCharacter() instanceof
				// Hero) {
				// GUI.setH((Hero) ((CharacterCell) map[X][Y])
				// .getCharacter());
				// // if(((CharacterCell) map[X][Y]).getCharacter() instanceof
				// Medic)
				// // GUI.getH().setTarget(((CharacterCell)
				// map[X][Y]).getCharacter());
				// }
				// else
				// GUI.getH().setTarget(((CharacterCell)
				// map[X][Y]).getCharacter());
				// System.out.println(GUI.getH().getTarget());
				// }
				// });
				GridPane.setConstraints(MapIndices[x][y], y, 14 - x);
				gameMap.getChildren().add(MapIndices[x][y]);
			}
		}
		return gameMap;
	}

	// public static Point buttonIndex(int r, int c) {
	// Point point = new Point(r, c);
	// return point;
	// }

	public static void newVisibility(int i, int j) {
		map[i][j].setVisible(true);
		if (i + 1 <= 14)
			map[i + 1][j].setVisible(true);
		if (i - 1 >= 0)
			map[i - 1][j].setVisible(true);
		if (j + 1 <= 14)
			map[i][j + 1].setVisible(true);
		if (j - 1 >= 0)
			map[i][j - 1].setVisible(true);
		if (j + 1 <= 14 && i + 1 <= 14)
			map[i + 1][j + 1].setVisible(true);
		if (i - 1 >= 0 && j - 1 >= 0)
			map[i - 1][j - 1].setVisible(true);
		if (i - 1 >= 0 && j + 1 <= 14)
			map[i - 1][j + 1].setVisible(true);
		if (i + 1 <= 14 && j - 1 >= 0)
			map[i + 1][j - 1].setVisible(true);
	}

	public static void zombieSpawn(int x) {
		int counter = 0;
		do { // TODO:Zombies
			Random rand = new Random();
			int indexX = rand.nextInt(15);
			int indexY = rand.nextInt(15);
			if (map[indexX][indexY] instanceof CharacterCell
					&& ((CharacterCell) (map[indexX][indexY])).getCharacter() == null) {
				Zombie newZombie = new Zombie();
				zombies.add(newZombie);
				((CharacterCell) map[indexX][indexY]).setCharacter(newZombie);
				newZombie.setLocation(new Point(indexX, indexY));
				counter++;
			}
		} while (counter != x);
	}

	public static void startGame(Hero h) {

		for (int i = 0; i < 15; i++)
			for (int j = 0; j < 15; j++) {
				map[i][j] = new CharacterCell(null);
			}
		availableHeroes.remove(h);
		heroes.add(h);
		// Medic m = new Medic("",59,5,5);
		// Medic m1 = new Medic("",59,5,5);
		// Medic m2 = new Medic("",59,5,5);
		// Medic m3 = new Medic("",59,5,5);
		// m.setLocation(new Point(6,6));
		// m1.setLocation(new Point(12,12));
		// m2.setLocation(new Point(4,8));
		// m3.setLocation(new Point(14,10));
		// heroes.add(m);
		// heroes.add(m1);
		// heroes.add(m2);
		// heroes.add(m3);
		// ((CharacterCell)map[6][6]).setCharacter(m);
		((CharacterCell) map[0][0]).setCharacter(h);
		h.setLocation(new Point(0, 0));
		int counter = 0;
		do { // Traps
			Random rand = new Random();
			int indexX = rand.nextInt(15);
			int indexY = rand.nextInt(15);
			if ((map[indexX][indexY] instanceof CharacterCell)
					&& (((CharacterCell) map[indexX][indexY]).getCharacter()) == null) {
				map[indexX][indexY] = new TrapCell();
				counter++;
			}
		} while (counter != 5);
		counter = 0;
		do { // TODO:Vaccine
			Random rand = new Random();
			int indexX = rand.nextInt(15);
			int indexY = rand.nextInt(15);
			if ((map[indexX][indexY] instanceof CharacterCell)
					&& (((CharacterCell) map[indexX][indexY]).getCharacter()) == null) {
				map[indexX][indexY] = new CollectibleCell(new Vaccine());
				counter++;
			}
		} while (counter != 5);
		counter = 0;
		do { // Supply
			Random rand = new Random();
			int indexX = rand.nextInt(15);
			int indexY = rand.nextInt(15);
			if ((map[indexX][indexY] instanceof CharacterCell)
					&& (((CharacterCell) map[indexX][indexY]).getCharacter()) == null) {
				map[indexX][indexY] = new CollectibleCell(new Supply());
				counter++;
			}
		} while (counter != 5);
		zombieSpawn(10);
		newVisibility(0, 0);
		System.out.println(h.getName());
		// for (int i = 0; i < map.length; i++) {
		// for (int j = 0; j < map.length; j++) {
		// if (map[i][j] instanceof CharacterCell
		// && ((CharacterCell) (map[i][j])).getCharacter() instanceof Hero) {
		// System.out.print("h ");
		// } else if (map[i][j] instanceof CharacterCell
		// && ((CharacterCell) (map[i][j])).getCharacter() instanceof Zombie) {
		// System.out.print("z ");
		// } else if (map[i][j] instanceof CollectibleCell
		// && ((CollectibleCell) (map[i][j])).getCollectible() instanceof
		// Vaccine) {
		// System.out.print("v ");
		// } else if (map[i][j] instanceof CollectibleCell
		// && ((CollectibleCell) (map[i][j])).getCollectible() instanceof
		// Supply) {
		// System.out.print("s ");
		// } else if (map[i][j] instanceof TrapCell) {
		// System.out.print("t ");
		// } else
		// System.out.print("e ");
		// }
		// System.out.println();
		// }
	}

	public static boolean checkWin() {
		int remainingVaccines = 0;
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (map[i][j] instanceof CollectibleCell
						&& ((CollectibleCell) map[i][j]).getCollectible() instanceof Vaccine)
					remainingVaccines++;
			}
		}
		for (Hero hero : heroes) {
			remainingVaccines += hero.getVaccineInventory().size();
		}
		return heroes.size() >= 5 && remainingVaccines == 0;
	}

	public static boolean checkGameOver() {
		if (heroes.size() > 0) {
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[i].length; j++) {
					if (map[i][j] instanceof CollectibleCell
							&& ((CollectibleCell) map[i][j]).getCollectible() instanceof Vaccine)
						return false;
				}
			}
			for (Hero hero : heroes) {
				if (hero.getVaccineInventory().size() > 0)
					return false;
			}
		}
		return true;
	}

	public static void endTurn() throws InvalidTargetException,
			NotEnoughActionsException { // incomplete
		for (int i = 0; i < zombies.size(); i++) {
			Zombie currZombie = zombies.get(i);
			currZombie.attack();
			currZombie.setTarget(null); // c
		}
		for (int i = 0; i < heroes.size(); i++) {
			Hero h = heroes.get(i);
			h.setActionsAvailable(h.getMaxActions());
			h.setTarget(null); // c
			h.setSpecialAction(false);
		}
		for (int i = 0; i < 15; i++)
			for (int j = 0; j < 15; j++)
				map[i][j].setVisible(false);
		for (int i = 0; i < heroes.size(); i++) {
			Hero currHero = heroes.get(i);
			newVisibility(currHero.getLocation().x, currHero.getLocation().y);
		}

		zombieSpawn(1);
	}
	// public static void main (String[]args) throws MovementException,
	// NotEnoughActionsException, NoAvailableResourcesException,
	// InvalidTargetException {
	// Fighter f = new Fighter("Ali",23,23,23);
	// startGame(f);
	// f.move(Direction.UP);
	// System.out.println(f.getLocation());
	// try {
	// f.useSpecial();
	// }catch(NoAvailableResourcesException e) {
	// System.out.println(e.getMessage());
	// }
	// try {
	// f.attack();
	// }catch(InvalidTargetException e) {
	// System.out.println(e.getMessage());
	// }
	// Zombie z1 = new Zombie();
	// map[0][0] = new CharacterCell(z1);
	// f.setTarget(z1);
	// z1.setLocation(new Point(0,0));
	// f.attack();
	// System.out.println(f.getCurrentHp() + " " + z1.getCurrentHp());
	// z1.attack();
	// System.out.println(f.getCurrentHp() + " " + z1.getCurrentHp());
	// }
}
