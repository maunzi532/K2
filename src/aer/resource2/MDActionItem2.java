package aer.resource2;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.resource2.resourceTypes.*;
import java.util.*;

public class MDActionItem2 implements HexItem
{
	private CostTable costTable;

	public MDActionItem2(CostTable costTable)
	{
		this.costTable = costTable;
	}

	@Override
	public List<TakeableAction> takeableActions(PathAction pathAction)
	{
		List<TakeableAction> list = new ArrayList<>();
		RBasicData res1 = (RBasicData) pathAction.deducted;
		HexPather pather = pathAction.pather;
		IHexMap map = pather.map;
		MapTile tile = pather.map.getTile(res1.dLocation());
		if(!res1.dEnd() && !res1.dMDUsed())
		{
			if(res1.dAirState() == AirState.MOUNT)
			{
				//Dismount
				list.add(new DismountAction2(costTable, res1.dMount(), AirState.UP));
				if(tile.type == MapTileType.FLOOR && res1.dMount().getAirState() == AirState.FLOOR)
					list.add(new DismountAction2(costTable, res1.dMount(), AirState.FLOOR));
				else
					list.add(new DismountAction2(costTable, res1.dMount(), AirState.FLY));
			}
			else
			{
				//Mount
				for(HexObject m1 : map.objectsAt(res1.dLocation()))
				{
					list.add(new MountAction2(costTable, m1));
					if(!res1.dDirection().equals(m1.getDirection()))
						list.add(new MountAction2(costTable, res1.dDirection(), m1));
				}
			}
		}
		return list;
	}
}