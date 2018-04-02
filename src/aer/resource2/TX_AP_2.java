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
	private int actionPoints;
	private int movePoints;
	private int reqFall;

	public TX_AP_2(CostTable costTable)
	{
		this.costTable = costTable;
	}

	@Override
	public void linkTo(HexPather pather)
	{
		this.pather = pather;
	}

	@Override
	public List<HexItem> activeItems(ItemGetType type, HexPather toDef)
	{
		List<HexItem> items = new ArrayList<>();
		items.add(new MDActionItem2(costTable));
		items.add(new FloorMovementItem2(costTable));
		return items;
	}

	@Override
	public TakeableAction startAction(ItemGetType type, HexPather toDef)
	{
		return new InitAction2(costTable);
	}

	@Override
	public ActionResource actionResource()
	{
		return new BasicAPResource2(100, 100, pather.getDirection(), pather.getAirState(),
				4, pather.getLoc(), pather.getMount());
	}

	@Override
	public void drawPhase()
	{
		actionPoints = 100;
		movePoints = 100;
		reqFall = 4;
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
		return null;
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