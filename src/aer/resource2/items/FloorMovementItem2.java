package aer.resource2.items;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.resource2.interfaces.*;
import aer.resource2.movement.*;
import aer.resource2.resourceTypes.*;
import java.util.*;

public class FloorMovementItem2 implements HexItem
{
	private CostTable costTable;

	public FloorMovementItem2(CostTable costTable)
	{
		this.costTable = costTable;
	}

	@Override
	public List<TakeableAction> takeableActions(PathAction pathAction)
	{
		List<TakeableAction> list = new ArrayList<>();
		RBasicData res1 = (RBasicData) pathAction.deducted;
		RActionPoints res2 = (RActionPoints) pathAction.deducted;
		HexPather pather = pathAction.pather;
		IHexMap map = pather.map;
		MapTile tile = pather.map.getTile(res1.dLocation());
		if(!res1.dEnd())
		{
			//Turn
			if(res1.dAirState().canTurn)
				for(int i = 0; i < 12; i++)
					if(i != res1.dDirection().r)
						list.add(new TurnAction2(costTable, res1.dDirection(), new HexDirection(i)));
			//Move (Floor)
			if(res1.dAirState() == AirState.FLOOR)
			{
				List<HexLocation> freelyMovingA = null;
				List<HexLocation> freelyMovingN = null;
				if(pathAction.action instanceof IMovementAction)
					freelyMovingA = ((IMovementAction) pathAction.action).freelyMovingN();
				if(freelyMovingA == null)
					freelyMovingN = new ArrayList<>();
				int maxM = maxMovement(res2.dMovementPoints());
				for(int ix = -maxM; ix <= maxM; ix++)
				{
					int maxD = Math.min(maxM, maxM - ix);
					for(int id = Math.max(-maxM, -maxM - ix); id <= maxD; id++)
					{
						HexLocation end = new HexLocation(res1.dLocation(), ix, id, 0, 0);
						if(!res1.dLocation().equals(end))
						{
							if(freelyMovingA == null)
								freelyMovingN.add(end);
							FloorMovementAction2 mv = new FloorMovementAction2(costTable, res1.dDirection(), map,
									res1.dLocation(), end, freelyMovingA, freelyMovingN);
							if(mv.possible())
								list.add(mv);
						}
					}
				}
			}
			//Land
			if(res1.dAirState().isAerial)
			{
				//Land
				if(res1.dAirState().fall < 0 && tile.type == MapTileType.FLOOR)
					list.add(new LandingAction2(costTable, false));
				//Fall
				else
					list.add(new FallAction2(costTable, false, map, res1.dLocation(), res1.dAirState()));
				//Airdash
				if(res1.dAirState().canAirdash && (res1.dAirState().fall >= 0 || tile.type != MapTileType.FLOOR))
				{
					int maxA = maxAirdash();
					for(int i = 1; i < maxA; i++)
						if(res1.dDirection().primary() || i % 2 == 0)
						{
							AirdashAction2 ad = new AirdashAction2(costTable, map, res1.dLocation(),
									HexLocation.addDirection(res1.dLocation(), res1.dDirection(), i),
									res1.dDirection(), res1.dAirState());
							if(ad.possible())
								list.add(ad);
						}
				}
			}
		}
		return list;
	}

	public int maxAirdash()
	{
		return 2;
	}

	public int maxMovement(int movePoints)
	{
		return (movePoints - costTable.initMoveM) / costTable.moveCostM;
	}
}