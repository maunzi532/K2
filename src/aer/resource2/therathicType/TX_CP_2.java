package aer.resource2.therathicType;

import aer.*;
import aer.path.team.*;
import aer.resource2.items.item1.*;
import aer.resource2.items.item2.*;
import aer.resource2.items.item3.*;

public class TX_CP_2 extends TX_CP
{
	public TX_CP_2()
	{
		npc_control = new UselessNPC();
		endItem = new FloorMovementItem2(this);
		items.add(endItem);
		items.add(new MountItem1(this));
		items.add(new TargetingItem3(this));
	}

	@Override
	public MountSlotInfo[] mountSlotInfo()
	{
		return new MountSlotInfo[0];
	}

	@Override
	public boolean playerControlled()
	{
		return true;
	}
}