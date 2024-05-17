package model.characters;

import java.awt.Point;
import java.util.Random;

import engine.Game;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughActionsException;
import model.world.CharacterCell;
import model.world.*;

public abstract class Character {
	private String name;
	private Point location;
	private int maxHp;
	private int currentHp;
	private int attackDmg;
	private Character target;

	public Character() {
	}

	public Character(String name, int maxHp, int attackDmg) {
		this.name = name;
		this.maxHp = maxHp;
		this.currentHp = maxHp;
		this.attackDmg = attackDmg;
	}

	public Character getTarget() {
		return target;
	}

	public void setTarget(Character target) {
		this.target = target;
	}

	public String getName() {
		return name;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public int getCurrentHp() {
		return currentHp;
	}

	public void setCurrentHp(int currentHp) {
		if (currentHp <= 0) {
			this.currentHp = 0;
			this.onCharacterDeath();
		} else if (currentHp > maxHp)
			this.currentHp = maxHp;
		else
			this.currentHp = currentHp;
	}

	public int getAttackDmg() {
		return attackDmg;
	}

	public void attack() throws InvalidTargetException, NotEnoughActionsException {
		getTarget().setCurrentHp(getTarget().getCurrentHp() - getAttackDmg());
		getTarget().defend(this);
		if(getTarget().getCurrentHp() == 0)
			setTarget(null);
	}

	public void defend(Character c) {
		int effect = getAttackDmg() / 2;
		c.setCurrentHp(c.getCurrentHp() - effect);
	}

	public void onCharacterDeath() {

		if (this instanceof Hero) {
			((CharacterCell) Game.map[getLocation().x][getLocation().y]).setCharacter(null);
			Game.heroes.remove(this);
		}
		if (this instanceof Zombie) {
			Game.zombies.remove(this);
			Game.zombieSpawn(1);
			((CharacterCell) Game.map[getLocation().x][getLocation().y]).setCharacter(null);
		}
	}

	public static boolean adjacent(Character c1, Character c2) {
		double xDiff = c1.getLocation().getX() - c2.getLocation().getX();
		double yDiff = c1.getLocation().getY() - c2.getLocation().getY();
		double diff1 = c1.getLocation().getX() - c2.getLocation().getX();
		double diff2 = c1.getLocation().getY() - c2.getLocation().getY();
		if ((Math.abs(xDiff) == 1 && c1.getLocation().y == c2.getLocation().y)
				|| (Math.abs(yDiff) == 1 && c1.getLocation().x == c2.getLocation().x)
				|| (Math.abs(diff1) == 1 && Math.abs(diff2) == 1)||(xDiff==0&&yDiff==0))
			return true;
		return false;
	}
}
