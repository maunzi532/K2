package aer.resource2.items.item2;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.resource2.resource.*;
import java.util.*;

public class FallingItem implements EndPatherItem
{
	private final CostTable costTable;
	private final int maxFlyingHeight;

	public FallingItem(CostTable costTable, int maxFlyingHeight)
	{
		this.costTable = costTable;
		this.maxFlyingHeight = maxFlyingHeight;
	}

	@Override
	public List<TakeableAction> takeableActions(PathAction pathAction)
	{
		List<TakeableAction> list = new ArrayList<>();
		ActionResource actionResource = pathAction.deducted;
		R_Relocatable r0 = (R_Relocatable) actionResource;
		if(!r0.dAirState().isAerial)
			return Collections.EMPTY_LIST;
		R_AP_MP r1 = (R_AP_MP) actionResource;
		R_FallData r2 = (R_FallData) actionResource;
		Pather pather = pathAction.pather;
		ITiledMap map = pather.map;
		MapTile currentTile = map.getTile(r0.dLocation());
		MapTile endTile = map.getTile(new HexLocation(r0.dLocation(), 0, 0, r0.dAirState().fall, 0));
		if(r0.dAirState().isAerial)
		{
			if(r0.dAirState().fall < 0 && currentTile.type == MapTileType.FLOOR)
				list.add(new Landing2(costTable, false));
			if(r0.dAirState().fall == 0 || (currentTile.exitingCost(PDirection.h(r0.dAirState().fall), MovementTileType.FALL) >= 0
					&& endTile.enteringCost(PDirection.h(r0.dAirState().fall), MovementTileType.FALL) >= 0))
				list.add(new Fall2(costTable, false, map, r0.dLocation(), r0.dAirState()));
		}
		return list;
	}

	@Override
	public TakeableAction endAction(ActionResource resource, Therathic therathic)
	{
		return null;
	}

	@Override
	public List<TakeableAction> interrupts(TargetData targetData)
	{
		return null;
	}
}