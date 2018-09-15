package aer.resource2.items.item2;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.resource2.interfaces.*;
import aer.resource2.resource.*;
import java.util.*;

public class FloorMovementItem2 implements EndPatherItem
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
		R_Relocatable res1 = (R_Relocatable) pathAction.deducted;
		R_AP_MP res2 = (R_AP_MP) pathAction.deducted;
		Pather pather = pathAction.pather;
		ITiledMap map = pather.map;
		MapTile tile = pather.map.getTile(res1.dLocation());
		if(!res1.dEnd())
		{
			//Turn
			if(res1.dAirState().cheapTurn)
				for(int i = 0; i < 12; i++)
					if(i != res1.dDirection().r)
						list.add(new Turn2(costTable, res1.dDirection(), new HexDirection(i)));
			//Move (Floor)
			if(res1.dAirState() == AirState.FLOOR)
			{
				List<HexLocation> freelyMovingA = null;
				List<HexLocation> freelyMovingN = null;
				if(pathAction.action instanceof IMovementAction)
					freelyMovingA = ((IMovementAction) pathAction.action).freelyMovingN();
				if(freelyMovingA == null)
					freelyMovingN = new ArrayList<>();
				int maxM = costTable.maxMovement(res2.dMovementPoints(), res2.dRequiresExtraMP() == 1);
				for(int ix = -maxM; ix <= maxM; ix++)
				{
					int maxD = Math.min(maxM, maxM - ix);
					for(int id = Math.max(-maxM, -maxM - ix); id <= maxD; id++)
					{
						HexLocation end = new HexLocation(res1.dLocation(), ix, id, 0, 0);
						if(!res1.dLocation().equals(end))
						{
							FloorMovement2 mv = new FloorMovement2(costTable, res2.dRequiresExtraMP() == 1,
									res1.dDirection(), map, res1.dLocation(), end, freelyMovingA, freelyMovingN);
							if(mv.possible())
							{
								list.add(mv);
								if(freelyMovingA == null)
									freelyMovingN.add(end);
							}
						}
					}
				}
			}
			//Aerial
			if(res1.dAirState().isAerial)
			{
				//Land
				if(res1.dAirState().fall < 0 && tile.type == MapTileType.FLOOR)
					list.add(new Landing2(costTable, false));
				//Fall
				else
					list.add(new Fall2(costTable, false, map, res1.dLocation(), res1.dAirState()));
				//Airdash
				if(res1.dAirState().canAirdash && (res1.dAirState().fall >= 0 || tile.type != MapTileType.FLOOR))
				{
					int maxA = costTable.maxAirdash(res2.dMovementPoints());
					for(int i = 1; i < maxA; i++)
						if(res1.dDirection().primary() || i % 2 == 0)
						{
							Airdash2 ad = new Airdash2(costTable, map, res1.dLocation(),
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

	@Override
	public TakeableAction endAction(ActionResource resource, Therathic therathic)
	{
		R_Relocatable res1 = (R_Relocatable) resource;
		R_FallData res3 = (R_FallData) resource;
		Pather pather = therathic.pather();
		ITiledMap map = pather.map;
		MapTile tile = pather.map.getTile(res1.dLocation());
		if(res1.dAirState().isAerial)
		{
			if(res1.dAirState().fall < 0 && tile.type == MapTileType.FLOOR)
				return new Landing2(costTable, true);
			else if(res3.dRequiredFall() > 0)
				return new Fall2(costTable, true, map, res1.dLocation(), res1.dAirState());
		}
		return null;
	}
}