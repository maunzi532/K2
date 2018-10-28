package aer.resource3;

import aer.path.pather.*;
import aer.path.schedule.*;
import aer.relocatable.mount.*;
import aer.resource3.resource4.*;
import java.io.*;
import java.util.*;

public abstract class Transformation implements Serializable, CBA, CostTable
{
	public TX_AP_Transform main;
	public Map<TStat, Integer> stats;
	public List<AppliedModifier> modifiers;
	public MType mType;

	public Transformation()
	{
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

	public ArrayList<PatherItem> activeItems()
	{
		ArrayList<PatherItem> modifierItems = new ArrayList<>();
		for(ItemModifier itemModifier : classModifiers(ItemModifier.class))
		{
			PatherItem item = itemModifier.item();
			if(item != null)
				modifierItems.add(item);
		}
		return modifierItems;
	}

	public ArrayList<PatherItem> interruptItems(TargetData targetData)
	{
		ArrayList<PatherItem> modifierItems = new ArrayList<>();
		for(ItemModifier itemModifier : classModifiers(ItemModifier.class))
		{
			PatherItem item = itemModifier.item();
			if(item != null)
				modifierItems.add(item);
		}
		return modifierItems;
	}

	public abstract List<EndPatherItem> endItems();

	public MType mType()
	{
		return mType;
	}

	public boolean transformKeepMounted()
	{
		return false;
	}

	public int modifiedStat(TStat stat)
	{
		int value = stats.get(stat);
		value += classModifiers(StatModifier.class).stream().filter(e -> e.modified == stat).mapToInt(e -> e.change).sum();
		return value;
	}

	public void drawPhase(){}

	public PathAction endPhase()
	{
		return null;
	}
}