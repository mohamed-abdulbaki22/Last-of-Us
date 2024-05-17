package model.characters;

import java.awt.Point;

import model.world.*;

import java.util.ArrayList;
import java.util.Random;

import views.Alerts;
import views.GUI;
import views.Trap;
import engine.Game;
import exceptions.InvalidTargetException;
import exceptions.MovementException;
import exceptions.NoAvailableResourcesException;
import exceptions.NotEnoughActionsException;
import model.collectibles.Supply;
import model.collectibles.Vaccine;
import model.world.CharacterCell;
import model.world.CollectibleCell;
import model.world.TrapCell;

public abstract class Hero extends Character {

	private int actionsAvailable;
	private int maxActions;
	private ArrayList<Vaccine> vaccineInventory;
	private ArrayList<Supply> supplyInventory;
	private boolean specialAction;

	public Hero(String name, int maxHp, int attackDmg, int maxActions) {
		super(name, maxHp, attackDmg);
		this.maxActions = maxActions;
		this.actionsAvailable = maxActions;
		this.vaccineInventory = new ArrayList<Vaccine>();
		this.supplyInventory = new ArrayList<Supply>();
		this.specialAction = false;

	}

	public boolean isSpecialAction() {
		return specialAction;
	}

	public void setSpecialAction(boolean specialAction) {
		this.specialAction = specialAction;
	}

	public int getActionsAvailable() {
		return actionsAvailable;
	}

	public void setActionsAvailable(int actionsAvailable) {
		this.actionsAvailable = actionsAvailable;
	}

	public int getMaxActions() {
		return maxActions;
	}

	public ArrayList<Vaccine> getVaccineInventory() {
		return vaccineInventory;
	}

	public ArrayList<Supply> getSupplyInventory() {
		return supplyInventory;
	}

	public String toString() {
		String type;
		if (this instanceof Fighter)
			type = "Fighter";
		else if (this instanceof Medic)
			type = "Medic";
		else
			type = "Explorer";
		String s = "Name: " + getName() + "\n" + "Type: " + type + "\n"
				+ "MaxActions: " + getMaxActions() + "\n" + "MaxHp: "
				+ getMaxHp() + "\n" + "AttckDmg: " + getAttackDmg();
		return s;
	}

	public String toString2() {
		String type;
		if (this instanceof Fighter)
			type = "Fighter";
		else if (this instanceof Medic)
			type = "Medic";
		else
			type = "Explorer";
		String s = "Name: " + getName() + "\n" + "Type: " + type + "\n"
				+ "MaxActions: " + getMaxActions() + "\n" + "CurrentHp: "
				+ getCurrentHp() + "\n" + "AttckDmg: " + getAttackDmg()+"\n";
		return s;
	}

	public void attack() throws InvalidTargetException,
			NotEnoughActionsException {
		if (getTarget() instanceof Hero)
			throw new InvalidTargetException("Nashin Ya Sayed");
		else if (getTarget() == null)
			throw new InvalidTargetException("There's No Target to Attack");
		else if (!adjacent(this, getTarget()))
			throw new InvalidTargetException("Only Adjacent zombies could be attacked");
		else if (getActionsAvailable() == 0)
			throw new NotEnoughActionsException("Your action points are 0");
		else {
			super.attack();
			setActionsAvailable(getActionsAvailable() - 1);
		}
	}

	// public void onCharacterDeath() { // whenever a character's HP reached 0
	// Game.heroes.remove(this);
	// ((CharacterCell)
	// Game.map[this.getLocation().y][this.getLocation().x]).setCharacter(null);
	// }

	// public void defend(Character c) {
	// int newHealth = c.getCurrentHp() - (getAttackDmg() / 2);
	// c.setCurrentHp(newHealth);
	// }

	public void move(Direction d) throws MovementException,
			NotEnoughActionsException {// incomplete
		if (this.getCurrentHp() == 0) {
			this.onCharacterDeath();
			return;
		}
		if (getActionsAvailable() == 0)
			throw new NotEnoughActionsException("Your Action Points are 0");
		int xh = getLocation().x;
		int yh = getLocation().y;
		switch (d) {
		case UP:
			if (xh == 14)
				throw new MovementException("raye7 feeeeeeeeeen");
			xh++;
			break;
		case DOWN:
			if (xh == 0)
				throw new MovementException("raye7 feeeeeeeeeen");
			xh--;
			break;
		case LEFT:
			if (yh == 0)
				throw new MovementException("raye7 feeeeeeeeeen");
			yh--;
			break;
		case RIGHT:
			if (yh == 14)
				throw new MovementException("raye7 feeeeeeeeeen");
			yh++;
			break;
		}
		CharacterCell oldC = (CharacterCell) Game.map[getLocation().x][getLocation().y];
		Cell newC = Game.map[xh][yh];
		if (newC instanceof CharacterCell) { // 1
			if (((CharacterCell) newC).getCharacter() != null)
				throw new MovementException("Can't move a cell having a Character");
			else {
				oldC.setCharacter(null);
				((CharacterCell) newC).setCharacter(this);
				setLocation(new Point(xh, yh));
				Game.newVisibility(xh, yh);
				setActionsAvailable(getActionsAvailable() - 1);
			}
		} else if (newC instanceof CollectibleCell) {// 2
			((CollectibleCell) newC).getCollectible().pickUp(this);
			oldC.setCharacter(null);
			Game.map[xh][yh] = new CharacterCell(this);
			setLocation(new Point(xh, yh));
			Game.newVisibility(xh, yh);
			setActionsAvailable(getActionsAvailable() - 1);
		} else if (newC instanceof TrapCell) {// 3
			int ay = ((TrapCell) newC).getTrapDamage();
			oldC.setCharacter(null);
			this.setCurrentHp(getCurrentHp() - ay);
			Trap.display();
//			GUI.makeDashBoard();
			if (getCurrentHp() > 0) {
				Game.map[xh][yh] = new CharacterCell(this);
				setLocation(new Point(xh, yh));
				Game.newVisibility(xh, yh);
				setActionsAvailable(getActionsAvailable() - 1);
			} else
				return;
		}
	}

	public void useSpecial() throws NoAvailableResourcesException,
			InvalidTargetException {
		if (getSupplyInventory().isEmpty())
			throw new NoAvailableResourcesException("No Supplies");
		getSupplyInventory().get(getSupplyInventory().size() - 1).use(this);
		setSpecialAction(true);
	}

	public void cure() throws InvalidTargetException,
			NoAvailableResourcesException, NotEnoughActionsException { // modification
		if (getActionsAvailable() == 0)
			throw new NotEnoughActionsException("Your Action points are 0");
		if (getVaccineInventory().isEmpty())
			throw new NoAvailableResourcesException("Your Vacine Inventory is Empty");
		if (getTarget() instanceof Hero || getTarget() == null) // c
			throw new InvalidTargetException("na4in ya sayed");
		if (adjacent(getTarget(), this)) {
			setActionsAvailable(getActionsAvailable() - 1);
			getVaccineInventory().get(getVaccineInventory().size() - 1).use(
					this);

			// this.setTarget(null); //c
			// Game.checkWin();
			// Game.checkGameOver();
		} else {
			throw new InvalidTargetException("You can only cure adjacent zombies");
		}
	}
}
