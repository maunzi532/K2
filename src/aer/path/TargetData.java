package aer.path;

import aer.*;
import aer.path.takeable.*;
import java.util.*;

public class TargetData
{
	public final HexPather caster;
	public final TakeableAction spell;
	public final HexPather target;

	public TargetData(HexPather caster, TakeableAction spell, HexPather target)
	{
		this.caster = caster;
		this.spell = spell;
		this.target = target;
	}

	public List<Reaction> reactionOptions()
	{
		return spell.targetOptions(caster, target);
	}

	public List<HexPather> canInterrupt(IHexMap map)
	{
		List<HexPather> interrupt = new ArrayList<>();
		for(Integer tn : spell.interruptTeamNumbers(caster, target))
			map.team(tn).forEach(e -> interrupt.add((HexPather) e));
		return interrupt;
	}

	public boolean exec(Reaction reaction)
	{
		return spell.executeOn(caster, target, reaction);
	}
}