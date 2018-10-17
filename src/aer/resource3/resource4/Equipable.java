package aer.resource3.resource4;

import aer.path.pather.*;
import aer.path.schedule.*;
import aer.path.team.*;
import aer.relocatable.*;
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
	public List<StatItem> inventory;
	public int health;
	public boolean active;
	public int lives;

	public Equipable(StatItem... items)
	{
		movementItem = new FloorMovementItem2(this);
		health = maxHealth();
		active = true;
		lives = maxLives();
		if(items.length >= 5)
		{
			lhItem = items[0];
			rhItem = items[1];
			dhItem = items[2];
			armorItem = items[3];
			shoeItem = items[4];
		}
		if(items.length > 5)
			inventory = new ArrayList<>(Arrays.asList(items).subList(5, items.length));
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
	public void drawPhase()
	{
		if(!active && lives > 0)
		{
			increaseHealth(regenPerTurn());
			if(health >= maxHealth())
			{
				active = true;
			}
		}
	}

	@Override
	public boolean canBeAttacked()
	{
		return active;
	}

	@Override
	public List<Reaction> reactions(Therathic attackedBy, StatItem item, AttackType attackType, int distance)
	{
		List<Reaction> reactions = new ArrayList<>();
		reactions.add(new Reaction("Nothing", 0, 0, true, 0));
		for(StatItem statItem1 : statItems())
		{
			for(AttackType attackType1 : statItem1.attackTypes())
			{
				reactions.add(new Reaction("Retaliate with item", 1, attackType1.retaliateCost(),
						attackType1.retaliateCost() <= main.getAP(), 2, statItem1, attackType1));
			}
		}
		reactions.add(new Reaction("Dodge", 2, 10, 10 <= main.getAP(), 1));
		return reactions;
	}

	@Override
	public void takeAttack(Therathic attackedBy, StatItem item, AttackType attackType,
			int distance, boolean retaliated, boolean dodge)
	{
		if(attackedBy instanceof CBA)
		{
			CBA cba = (CBA) attackedBy;
			AttackCalc attackCalc = new AttackCalc(cba, item, attackType, this, distance, retaliated, dodge);
			System.out.println(attackCalc.toString());
			int dmg = attackCalc.dmg();
			if(dmg >= 0)
				reduceHealth(dmg);
		}
		else
			throw new RuntimeException("Attacked by non-CBA: " + attackedBy.pather().name());
	}

	@Override
	public int statAttack(int num)
	{
		return 2;
	}

	@Override
	public int statArmor(int num)
	{
		return 2;
	}

	@Override
	public int statResist(int num)
	{
		return 2;
	}

	@Override
	public int statAvoid()
	{
		return 2;
	}

	@Override
	public int statFocus()
	{
		return 2;
	}

	public int maxHealth()
	{
		return 100;
	}

	public int regenPerTurn()
	{
		return 30;
	}

	public int maxLives()
	{
		return 2;
	}

	@Override
	public int startingAP()
	{
		return active ? super.startingAP() : 20;
	}

	@Override
	public int startingM()
	{
		return active ? super.startingM() : 0;
	}

	public boolean reduceHealth(int amount)
	{
		if(amount < 0)
			amount = 0;
		health = health - amount;
		System.out.println(amount + " dmg -> " + health + " health remaining");
		if(health <= 0 && active)
		{
			lives = Math.max(lives - 1, 0);
			active = false;
			return true;
		}
		return health <= 0;
	}

	public boolean increaseHealth(int amount)
	{
		if(amount <= 0)
			return health >= maxHealth();
		health = health + amount;
		System.out.println("healed by " + amount + " -> " + health + " health");
		if(health >= maxHealth())
		{
			health = maxHealth();
			return true;
		}
		return false;
	}
}