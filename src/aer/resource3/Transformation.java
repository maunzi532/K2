package aer.resource3;

import aer.path.*;
import aer.path.team.*;
import java.util.*;

public class Transformation
{
	public TX_AP_Transform main;
	public Map<TStat, Integer> stats;
	public List<AppliedModifier> modifiers;

	public Transformation(TX_AP_Transform main)
	{
		this.main = main;
		modifiers = new ArrayList<>();
	}

	public List<HexItem> activeItems(ItemGetType type, TargetData targetData)
	{
		return null;
	}

	public int stat(TStat stat)
	{
		int value = stats.get(stat);
		for(AppliedModifier am : modifiers)
		{
			if(am.modifier instanceof StatModifier && ((StatModifier) am.modifier).modified == stat)
			{
				value += ((StatModifier) am.modifier).change;
			}
		}
		for(AppliedModifier am : main.modifiers)
		{
			if(am.modifier instanceof StatModifier && ((StatModifier) am.modifier).modified == stat)
			{
				value += ((StatModifier) am.modifier).change;
			}
		}
		return value;
	}

	public void drawPhase()
	{

	}

	public PathAction endPhase()
	{
		return null;
	}
}