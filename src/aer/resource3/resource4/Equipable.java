package aer.resource3.resource4;

import aer.*;
import aer.path.*;
import aer.path.team.*;
import aer.resource2.items.item2.*;
import aer.resource3.*;
import java.util.*;

public class Equipable extends Transformation
{
	public EndPatherItem movementItem;
	public StatItem lhItem;
	public StatItem rhItem;
	public StatItem dhItem;
	public StatItem armorItem;
	public StatItem shoeItem;
	public PatherItem classItem;
	public PatherItem personalItem;

	public Equipable()
	{
		costTable = CostTable.v1();
		movementItem = new FloorMovementItem2(costTable);
	}

	protected void addItems(ArrayList<PatherItem> items)
	{
		items.add(movementItem);
		if(dhItem != null)
			items.add(dhItem);
		else
		{
			if(lhItem != null)
				items.add(lhItem);
			if(rhItem != null)
				items.add(rhItem);
		}
		if(armorItem != null)
			items.add(armorItem);
		if(shoeItem != null)
			items.add(shoeItem);
		if(classItem != null)
			items.add(classItem);
		if(personalItem != null)
			items.add(personalItem);
	}

	@Override
	public ArrayList<PatherItem> activeItems()
	{
		ArrayList<PatherItem> items = super.activeItems();
		addItems(items);
		return items;
	}

	@Override
	public ArrayList<PatherItem> interruptItems(TargetData targetData)
	{
		ArrayList<PatherItem> items = super.interruptItems(targetData);
		addItems(items);
		return items;
	}

	@Override
	public List<EndPatherItem> endItems()
	{
		return Collections.singletonList(movementItem);
	}

	@Override
	public MountSlotInfo[] mountSlotInfo()
	{
		return new MountSlotInfo[]{MountSlotInfo.NORMAL};
	}

	public List<StatItem> statItems()
	{
		List<StatItem> items = new ArrayList<>();
		if(dhItem != null)
			items.add(dhItem);
		else
		{
			if(lhItem != null)
				items.add(lhItem);
			if(rhItem != null)
				items.add(rhItem);
		}
		if(armorItem != null)
			items.add(armorItem);
		if(shoeItem != null)
			items.add(shoeItem);
		if(classItem instanceof StatItem)
			items.add((StatItem) classItem);
		if(personalItem instanceof StatItem)
			items.add((StatItem) personalItem);
		return items;
	}

	@Override
	public boolean canBeAttacked()
	{
		return true;
	}

	@Override
	public List<Reaction> reactions(Therathic attackedBy, StatItem item, AttackType attackType, int distance)
	{
		List<Reaction> reactions = new ArrayList<>();
		reactions.add(new Reaction("Nothing", 0, 0, true));
		for(StatItem statItem1 : statItems())
		{
			for(AttackType attackType1 : statItem1.attackTypes())
			{
				reactions.add(new Reaction("Retaliate with item", 1, attackType1.retaliateCost(), true, statItem1, attackType1));
			}
		}
		reactions.add(new Reaction("Dodge", 2, 10, true));
		for(StatItem statItem1 : statItems())
		{
			if(statItem1.canBlock())
			{
				reactions.add(new Reaction("Block with item", 3, statItem1.blockCost(), true, statItem1));
			}
		}
		return reactions;
	}

	@Override
	public void takeAttack(Therathic attackedBy, StatItem item, AttackType attackType, int distance, boolean retaliated)
	{
		System.out.println("W");
	}

	@Override
	public void dodgeAttack(Therathic attackedBy, StatItem item, AttackType attackType, int distance)
	{
		System.out.println("D");
	}

	@Override
	public void blockAttack(Therathic attackedBy, StatItem item, AttackType attackType, int distance,
			StatItem blockWith)
	{
		System.out.println("B");
	}
}