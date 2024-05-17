package model.collectibles;

import java.awt.Point;
import java.util.Random;

import engine.Game;
import model.characters.Hero;
import model.world.CharacterCell;

public class Vaccine implements Collectible {

	public Vaccine() {

	}

	@Override
	public void pickUp(Hero h) {
		h.getVaccineInventory().add(this);

	}

	@Override
	public void use(Hero h) {
		h.getVaccineInventory().remove(this);
		Game.vaccinesUsed++;
		Game.zombies.remove(h.getTarget());
		Random randHero = new Random();
		Hero newHero = Game.availableHeroes.remove(randHero.nextInt(Game.availableHeroes.size()));
		Game.heroes.add(newHero);
		newHero.setLocation(new Point(h.getTarget().getLocation().x, h.getTarget().getLocation().y));
		((CharacterCell) Game.map[h.getTarget().getLocation().x][h.getTarget().getLocation().y]).setCharacter(newHero);

	}

}
