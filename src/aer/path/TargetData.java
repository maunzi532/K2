package aer.path;

import aer.*;
import aer.path.takeable.*;
import java.util.*;

public class TargetData
{
	public final Pather caster;
	public final TakeableAction spell;
	public final Pather target;

	public TargetData(Pather caster, TakeableAction spell, Pather target)
	{
		this.caster = caster;
		this.spell = spell;
		this.target = target;
	}

	public List<Reaction> reactionOptions()
	{
		return spell.targetOptions(caster, target);
	}

	public List<Pather> canInterrupt(ITiledMap map)
	{
		List<Pather> interrupt = new ArrayList<>();
		for(Integer tn : spell.interruptTeamNumbers(caster, target))
			map.team(tn).forEach(e -> interrupt.add((Pather) e));
		return interrupt;
	}

	public boolean exec(Reaction reaction)
	{
		return spell.executeOn(caster, target, reaction);
	}
}