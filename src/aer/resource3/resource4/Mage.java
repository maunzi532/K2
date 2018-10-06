package aer.resource3.resource4;

import aer.path.pather.*;
import aer.path.schedule.*;
import aer.path.team.*;
import aer.relocatable.*;
import aer.resource2.items.item2.*;
import aer.resource3.*;
import java.util.*;

public class Mage extends Transformation
{
	public EndPatherItem movementItem;
	public PatherItem personalWand;
	public boolean wandDrawn;

	public Mage()
	{
		movementItem = new FloorMovementItem2(this);
	}

	@Override
	public ArrayList<PatherItem> activeItems()
	{
		ArrayList<PatherItem> items = super.activeItems();
		items.add(movementItem);
		if(wandDrawn)
			items.add(personalWand);
		return items;
	}

	@Override
	public ArrayList<PatherItem> interruptItems(TargetData targetData)
	{
		ArrayList<PatherItem> items = super.interruptItems(targetData);
		items.add(movementItem);
		if(wandDrawn)
			items.add(personalWand);
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

	@Override
	public boolean canBeAttacked()
	{
		return false;
	}

	@Override
	public List<Reaction> reactions(Therathic attackedBy, StatItem item, AttackType attackType, int distance)
	{
		return null;
	}

	@Override
	public void takeAttack(Therathic attackedBy, StatItem item, AttackType attackType, int distance, boolean retaliated,
			boolean dodge)
	{

	}

	@Override
	public int statAttack(int num)
	{
		return 1;
	}

	@Override
	public int statArmor(int num)
	{
		return 2;
	}

	@Override
	public int statResist(int num)
	{
		return 1;
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

}