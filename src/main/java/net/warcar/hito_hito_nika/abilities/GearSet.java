package net.warcar.hito_hito_nika.abilities;

import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;

import java.util.ArrayList;
import java.util.Optional;

public class GearSet extends ArrayList<Ability> {
	public <A extends Ability> boolean containsAbility(AbilityCore<A> in) {
		return this.stream().anyMatch(itr -> itr.getCore() == in);
	}

	public <A extends Ability> Optional<Ability> getAbility(AbilityCore<A> in) {
		return this.stream().filter(itr -> itr.getCore() == in).findFirst();
	}
}
