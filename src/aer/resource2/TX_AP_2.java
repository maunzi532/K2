package aer.resource2;

import aer.path.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.resource2.therathicTypes.*;
import java.util.*;

public class TX_AP_2 implements TherathicHex, EActionPoints
{
	private CostTable costTable;
	private HexPather pather;
	private NPC_Control npc_control;
	private int actionPoints;
	private int movePoints;
	private int reqFall;

	public TX_AP_2(CostTable costTable)
	{
		this.costTable = costTable;
		npc_control = new UselessNPC();
	}

	@Override
	public void linkTo(HexPather pather)
	{
		this.pather = pather;
	}

	@Override
	public List<HexItem> activeItems(ItemGetType type, TargetData targetData)
	{
		List<HexItem> items = new ArrayList<>();
		items.add(new MDActionItem2(costTable));
		items.add(new FloorMovementItem2(costTable));
		return items;
	}

	@Override
	public TakeableAction startAction(ItemGetType type)
	{
		return new InitAction2(costTable);
	}

	@Override
	public ActionResource actionResource()
	{
		return new BasicAPResource2(actionPoints, movePoints, pather.getDirection(), pather.getAirState(), reqFall, pather.getLoc(), pather.getMount());
	}

	@Override
	public boolean drawPhase()
	{
		actionPoints = 100;
		movePoints = 100;
		reqFall = 4;
		return true;
	}

	@Override
	public PathAction endPhase()
	{
		return null;
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
	public boolean useAP(int amount, boolean real)
	{
		boolean ok = actionPoints >= amount;
		if(ok && real)
			actionPoints -= amount;
		return ok;
	}

	@Override
	public boolean useMP(int amount, boolean real)
	{
		boolean ok = movePoints >= amount;
		if(ok && real)
			movePoints -= amount;
		return ok;
	}

	@Override
	public void drainAP(int amount)
	{
		actionPoints = Math.max(actionPoints - amount, 0);
	}

	@Override
	public void drainMP(int amount)
	{
		movePoints = Math.max(movePoints - amount, 0);
	}
}