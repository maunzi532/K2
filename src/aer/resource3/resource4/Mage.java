package aer.resource3.resource4;

import aer.path.*;
import aer.resource2.items.*;
import aer.resource3.*;
import java.util.*;

public class Mage extends Transformation
{
	public EndHexItem movementItem;
	public HexItem personalWand;
	public boolean wandDrawn;

	public Mage()
	{
		costTable = CostTable.v1();
		movementItem = new FloorMovementItem2(costTable);
	}

	@Override
	public ArrayList<HexItem> activeItems()
	{
		ArrayList<HexItem> items = super.activeItems();
		items.add(movementItem);
		if(wandDrawn)
			items.add(personalWand);
		return items;
	}

	@Override
	public ArrayList<HexItem> interruptItems(TargetData targetData)
	{
		ArrayList<HexItem> items = super.interruptItems(targetData);
		items.add(movementItem);
		if(wandDrawn)
			items.add(personalWand);
		return items;
	}

	@Override
	public List<EndHexItem> endItems()
	{
		return Collections.singletonList(movementItem);
	}
}