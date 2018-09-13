package aer.resource3.resource4;

import aer.*;
import aer.path.*;
import aer.path.team.*;
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
			boolean dodge, StatItem blockWith)
	{

	}

}