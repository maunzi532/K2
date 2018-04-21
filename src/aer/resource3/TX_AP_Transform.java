package aer.resource3;

import aer.path.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.resource2.*;
import aer.resource2.otheractions.*;
import aer.resource2.therathicTypes.*;
import java.util.*;

public class TX_AP_Transform implements TherathicHex, E_AP_MP
{
	private HexPather pather;
	private List<Transformation> transforms;
	private CostTable costTable;
	private NPC_Control npc_control;
	private int actionPoints;
	private int movePoints;
	private int reqFall;
	//private List<HexItem> items;

	public TX_AP_Transform(CostTable costTable, Transformation transform0)
	{
		this.costTable = costTable;
		transforms = new ArrayList<>();
		transforms.add(transform0);
		npc_control = new UselessNPC();
		/*items = new ArrayList<>();
		items.add(new MDActionItem2(costTable));
		items.add(new FloorMovementItem2(costTable));
		items.add(new TargetingItem2(costTable));*/
	}

	@Override
	public void linkTo(HexPather pather)
	{
		this.pather = pather;
	}

	@Override
	public HexPather pather()
	{
		return pather;
	}

	@Override
	public List<HexItem> activeItems(ItemGetType type, TargetData targetData)
	{
		return null;
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
		reqFall = pather.getAirState().isAerial ? 4 : 0;
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