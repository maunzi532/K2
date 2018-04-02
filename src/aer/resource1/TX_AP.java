package aer.resource1;

import aer.path.*;
import aer.path.team.*;
import java.util.*;

public class TX_AP implements TherathicHex
{
	private CostTable costTable;
	private HexPather pather;

	@Override
	public void linkTo(HexPather pather)
	{
		this.pather = pather;
	}

	@Override
	public List<HexItem> activeItems(ItemGetType type, HexPather toDef)
	{
		return Collections.singletonList(new BasicMovementItem(false, costTable));
	}

	@Override
	public APAction startAction(ItemGetType type, HexPather toDef)
	{
		return new InitAction(costTable);
	}

	@Override
	public ActionResource actionResource()
	{
		return new APResource(pather.getLoc(), pather.getDirection(), pather.getAirState(),
				pather.getMount(), 4, 100, 100);
	}

	@Override
	public void drawPhase()
	{

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

	public TX_AP(CostTable costTable)
	{
		this.costTable = costTable;
	}
}