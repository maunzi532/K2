package aer.resource2.therathicType;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.resource2.items.item1.*;
import aer.resource2.items.item2.*;
import aer.resource2.items.item3.*;
import aer.resource2.resource.*;
import java.util.*;

public class TX_AP_2 implements Therathic, E_AP_MP
{
	private CostTable costTable;
	private Pather pather;
	private NPC_Control npc_control;
	private int actionPoints;
	private int movePoints;
	private int reqFall;
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
	public void linkTo(Pather pather)
	{
		this.pather = pather;
	}

	@Override
	public Pather pather()
	{
		return pather;
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
		return new InitAction(costTable);
	}

	@Override
	public void setUsedFirstPath(){}

	@Override
	public void setUsedFirstMovement(){}

	@Override
	public ActionResource actionResource()
	{
		return new Resource_AP_MP(actionPoints, movePoints, pather.getDirection(), pather.getAirState(), reqFall, pather.getLoc(), pather.getMountedTo(), pather.getMountedToSlot());
	}

	@Override
	public boolean drawPhase()
	{
		actionPoints = costTable.startingAP();
		movePoints = costTable.startingM();
		reqFall = costTable.requiredFall();
		return true;
	}

	@Override
	public PathAction endPhase()
	{
		return endPath();
	}

	@Override
	public int teamSide()
	{
		return 0;
	}

	@Override
	public boolean playerControlled()
	{
		return true;
	}

	@Override
	public NPC_Control npcControl()
	{
		return npc_control;
	}

	@Override
	public boolean useAP(int amount, Use use)
	{
		if(use == Use.DRAIN)
		{
			actionPoints = Math.max(actionPoints - amount, 0);
			return true;
		}
		boolean ok = actionPoints >= amount;
		if(ok && use == Use.REAL)
			actionPoints -= amount;
		return ok;
	}

	@Override
	public boolean useMP(int amount, Use use)
	{
		if(use == Use.DRAIN)
		{
			movePoints = Math.max(movePoints - amount, 0);
			return true;
		}
		boolean ok = movePoints >= amount;
		if(ok && use == Use.REAL)
			movePoints -= amount;
		return ok;
	}
}