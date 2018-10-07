package aer.resource3.resource4;

import aer.path.pather.*;
import aer.path.schedule.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.resource2.resource.*;
import java.util.*;

public class ValueNPC implements NPC_Control
{
	@Override
	public PathAction path(Pather xec)
	{
		ValueValue current = (ValueValue) aiValue(xec, xec.getTherathic().actionResource());
		return xec.getPossiblePaths().stream().filter(e -> e.aiValue.compareTo(current) > 0)
				.max(Comparator.comparing(e -> e.aiValue)).orElse(null);
	}

	@Override
	public TakeableAction interrupt(Pather xec, TargetData targetData)
	{
		ValueValue current = (ValueValue) aiValue(xec, xec.getTherathic().actionResource());
		return xec.getPossiblePaths().stream().filter(e -> e.aiValue.compareTo(current) > 0)
				.max(Comparator.comparing(e -> e.aiValue)).map(e -> e.action).orElse(null);
	}

	@Override
	public Reaction reaction(TargetData targetData)
	{
		return targetData.reactionOptions().stream().filter(e -> e.available)
				.max(Comparator.comparingInt(e -> e.aiValue)).orElse(targetData.reactionOptions().get(0));
	}

	@Override
	public AIValue aiValue(Pather pather, ActionResource resource)
	{
		return new ValueValue(pather, (Resource_AP_MP) resource);
	}
}