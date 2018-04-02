package aer.resource2;

import aer.path.*;
import aer.path.takeable.*;
import aer.path.team.*;
import java.util.*;

public class TX_AP_2 implements TherathicHex
{
	private CostTable costTable;
	private HexPather pather;

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
}