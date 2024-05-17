package model.characters;

import java.awt.Point;
import java.util.ArrayList;

import engine.Game;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughActionsException;
import model.world.CharacterCell;
import model.world.*;

public class Zombie extends Character {
	static int ZOMBIES_COUNT = 1;

	public Zombie() {
		super("Zombie " + ZOMBIES_COUNT, 40, 10);
		ZOMBIES_COUNT++;
	}

	public void attack() throws InvalidTargetException,
			NotEnoughActionsException {
		// Hero hero = findAdjacentHero();
		// setTarget(hero);
		searchAdjacentCells();

		if (getTarget() != null) {
			super.attack();
		}
	}

	public void searchAdjacentCells() {
		ArrayList<Point> cells = new ArrayList<>();
		cells.add(new Point(getLocation().x + 1, getLocation().y));// 1
		cells.add(new Point(getLocation().x - 1, getLocation().y));// 2
		cells.add(new Point(getLocation().x, getLocation().y + 1));// 3
		cells.add(new Point(getLocation().x, getLocation().y - 1));// 4
		cells.add(new Point(getLocation().x + 1, getLocation().y + 1));// 5
		cells.add(new Point(getLocation().x + 1, getLocation().y - 1));// 6
		cells.add(new Point(getLocation().x - 1, getLocation().y + 1));// 7
		cells.add(new Point(getLocation().x - 1, getLocation().y - 1));// 8
		for (int i = 0; i < cells.size(); i++) {
			int xCell = cells.get(i).x;
			int yCell = cells.get(i).y;
			if (xCell < 15 && xCell >= 0 && yCell < 15 && yCell >= 0) {
				Cell adjCell = Game.map[cells.get(i).x][cells.get(i).y];
				if (adjCell instanceof CharacterCell
						&& ((CharacterCell) adjCell).getCharacter() instanceof Hero) {
					setTarget(((CharacterCell) adjCell).getCharacter());
					return;
				}
			}
		}
	}
}
// public void onCharacterDeath() { // whenever a character's HP reached 0
// Game.zombies.remove(this);
// ((CharacterCell)
// Game.map[this.getLocation().y][this.getLocation().x]).setCharacter(null);
// }

//
// public void defend(Character c) {
// int newHealth = c.getCurrentHp() - (getAttackDmg() / 2);
// c.setCurrentHp(newHealth);
//
// }
