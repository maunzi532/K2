package aer.resource3.resource4;

import aer.path.*;
import aer.resource2.items.*;
import aer.resource3.*;
import java.util.*;

public class Mage extends Transformation
{
	public EndHexItem movementItem;
	public HexItem personalWand;

	public Mage()
	{
		costTable = CostTable.v1();
		movementItem = new FloorMovementItem2(costTable);
	}

	@Override
	public List<HexItem> activeItems()
	{
		ArrayList<HexItem> items = new ArrayList<>();
		items.add(movementItem);
		items.addAll(super.activeItems());
		return items;
	}

	@Override
	public List<HexItem> interruptItems(TargetData targetData)
	{
		ArrayList<HexItem> items = new ArrayList<>();
		items.add(movementItem);
		items.addAll(super.interruptItems(targetData));
		return items;
	}

	@Override
	public List<EndHexItem> endItems()
	{
		return Collections.singletonList(movementItem);
	}
}