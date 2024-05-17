package engine;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

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

public class Game {

	public static Cell[][] map = new Cell[15][15];
	public static ArrayList<Hero> availableHeroes = new ArrayList<Hero>();
	public static ArrayList<Hero> heroes = new ArrayList<Hero>();
	public static ArrayList<Zombie> zombies = new ArrayList<Zombie>();
	public static int vaccinesUsed = 0;

	public static void loadHeroes(String filePath) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			Hero hero = null;
			switch (content[1]) {
			case "FIGH":
				hero = new Fighter(content[0], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]));
				break;
			case "MED":
				hero = new Medic(content[0], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]));
				break;
			case "EXP":
				hero = new Explorer(content[0], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]));
				break;
			}
			availableHeroes.add(hero);
			line = br.readLine();

		}
		br.close();

	}

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
	}

	public static boolean checkWin() {
		for (int i = 0; i < heroes.size(); i++)
			if (heroes.get(i).getVaccineInventory().size() != 0)
				return false;
		for (int i = 0; i < 15; i++)
			for (int j = 0; j < 15; j++)
				if (map[i][j] instanceof CollectibleCell
						&& ((CollectibleCell) map[i][j]).getCollectible() instanceof Vaccine)
					return false;
		if (heroes.size() >= 5)
			return true;
		return false;
	}

	public static boolean checkGameOver() {
		if (checkWin() || heroes.isEmpty() || (availableHeroes.isEmpty() && heroes.size() < 5))
			return true;
		for (int i = 0; i < heroes.size(); i++)
			if (heroes.get(i).getVaccineInventory().size() != 0)
				return false;
		for (int i = 0; i < 15; i++)
			for (int j = 0; j < 15; j++)
				if (map[i][j] instanceof CollectibleCell
						&& ((CollectibleCell) map[i][j]).getCollectible() instanceof Vaccine)
					return false;

		return true;

	}

	public static void endTurn() throws InvalidTargetException, NotEnoughActionsException { // incomplete
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
//	public static void main (String[]args) throws MovementException, NotEnoughActionsException, NoAvailableResourcesException, InvalidTargetException {
//		Fighter f = new Fighter("Ali",23,23,23);
//		startGame(f);
//		f.move(Direction.UP);
//		System.out.println(f.getLocation());
//		try {
//			f.useSpecial();
//		}catch(NoAvailableResourcesException e) {
//			System.out.println(e.getMessage());
//		}
//		try {
//			f.attack();
//		}catch(InvalidTargetException e) {
//			System.out.println(e.getMessage());
//		}
//		Zombie z1 = new Zombie();
//		map[0][0] = new CharacterCell(z1);
//		f.setTarget(z1);
//		z1.setLocation(new Point(0,0));
//		f.attack();
//		System.out.println(f.getCurrentHp() + " " + z1.getCurrentHp());
//		z1.attack();
//		System.out.println(f.getCurrentHp() + " " + z1.getCurrentHp());
//	}
}
