package aer.resource3;

import aer.path.*;
import java.util.*;

public abstract class Transformation
{
	public TX_AP_Transform main;
	public Map<TStat, Integer> stats;
	public List<AppliedModifier> modifiers;

	public Transformation(TX_AP_Transform main)
	{
		this.main = main;
		stats = new HashMap<>();
		modifiers = new ArrayList<>();
	}

	public <T extends Modifier> List<T> classModifiers(Class<T> class0)
	{
		ArrayList<T> classModifiers = new ArrayList<>();
		for(AppliedModifier am : main.modifiers)
			if(am.modifier.getClass().equals(class0))
				classModifiers.add((T) am.modifier);
		for(AppliedModifier am : modifiers)
			if(am.modifier.getClass().equals(class0))
				classModifiers.add((T) am.modifier);
		return classModifiers;
	}

	public List<HexItem> activeItems()
	{
		List<HexItem> modifierItems = new ArrayList<>();
		for(ItemModifier itemModifier : classModifiers(ItemModifier.class))
		{
			HexItem item = itemModifier.item();
			if(item != null)
				modifierItems.add(item);
		}
		return modifierItems;
	}

	public List<HexItem> interruptItems(TargetData targetData)
	{
		List<HexItem> modifierItems = new ArrayList<>();
		for(ItemModifier itemModifier : classModifiers(ItemModifier.class))
		{
			HexItem item = itemModifier.item();
			if(item != null)
				modifierItems.add(item);
		}
		return modifierItems;
	}

	public abstract List<EndHexItem> endItems();

	public int stat(TStat stat)
	{
		int value = stats.get(stat);
		value += classModifiers(StatModifier.class).stream().filter(e -> e.modified == stat).mapToInt(e -> e.change).sum();
		/*for(AppliedModifier am : modifiers)
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
		}*/
		return value;
	}

	public void drawPhase(){}

	public PathAction endPhase()
	{
		return null;
	}
}