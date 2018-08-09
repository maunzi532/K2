package aer.resource3.resource4;

import aer.path.*;
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
		costTable = CostTable.v1();
		movementItem = new FloorMovementItem2(costTable);
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
}