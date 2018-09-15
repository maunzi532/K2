package aer.resource2.therathicType;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.resource2.items.item1.*;
import aer.resource2.items.item2.*;
import aer.resource2.items.item3.*;
import java.util.*;

public class TX_AP_2 extends TX_AP
{
	private CostTable costTable;
	private List<PatherItem> items;

	public TX_AP_2(CostTable costTable)
	{
		this.costTable = costTable;
		npc_control = new UselessNPC();
		items = new ArrayList<>();
		items.add(new MountItem1(costTable));
		items.add(new FloorMovementItem2(costTable));
		items.add(new TargetingItem3(costTable));
	}

	@Override
	public MountSlotInfo[] mountSlotInfo()
	{
		return new MountSlotInfo[0];
	}

	@Override
	public List<PatherItem> activeItems()
	{
		return items;
	}

	@Override
	public List<PatherItem> interruptItems(TargetData targetData)
	{
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<EndPatherItem> endItems()
	{
		return Collections.EMPTY_LIST;
	}

	@Override
	public TakeableAction startAction()
	{
		return new InitAction(costTable, usedFirstPath);
	}

	@Override
	public boolean drawPhase()
	{
		super.drawPhase();
		actionPoints = costTable.startingAP();
		movePoints = costTable.startingM();
		reqFall = costTable.requiredFall();
		return true;
	}

	@Override
	public boolean playerControlled()
	{
		return true;
	}
}