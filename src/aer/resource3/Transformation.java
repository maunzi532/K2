package aer.resource3;

import aer.*;
import aer.path.*;
import java.io.*;
import java.util.*;

public abstract class Transformation implements Serializable
{
	public CostTable costTable;
	public TX_AP_Transform main;
	public Map<TStat, Integer> stats;
	public List<AppliedModifier> modifiers;

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

	public MountSlotInfo[] mountSlotInfo()
	{
		return new MountSlotInfo[0];
	}

	public boolean transformKeepMounted()
	{
		return false;
	}

	public int stat(TStat stat)
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