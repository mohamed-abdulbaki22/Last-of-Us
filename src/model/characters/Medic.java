package model.characters;

import exceptions.InvalidTargetException;
import exceptions.NoAvailableResourcesException;
import exceptions.NotEnoughActionsException;

public class Medic extends Hero {
	// Heal amount attribute - quiz idea

	public Medic(String name, int maxHp, int attackDmg, int maxActions) {
		super(name, maxHp, attackDmg, maxActions);

	}

	public void useSpecial() throws NoAvailableResourcesException, InvalidTargetException {
		if (getTarget() instanceof Hero) {
			if (adjacent(this, getTarget())) {
				System.out.println("curr hp before"+(this.getTarget().getCurrentHp()));
				System.out.println("max hp"+(this.getTarget().getMaxHp()));
				super.useSpecial();
				getTarget().setCurrentHp(getTarget().getMaxHp());
				System.out.println("curr hp after"+(this.getTarget().getCurrentHp()));
				System.out.println("max hp"+(this.getTarget().getMaxHp()));
			} else
				throw new InvalidTargetException();
		} else
			throw new InvalidTargetException();
	}

}
