package aer.resource2.items.item1;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.resource2.resource.*;
import java.util.*;

public class MountItem1 implements PatherItem
{
	private CostTable costTable;

	public MountItem1(CostTable costTable)
	{
		this.costTable = costTable;
	}

	@Override
	public List<TakeableAction> takeableActions(PathAction pathAction)
	{
		List<TakeableAction> list = new ArrayList<>();
		R_Relocatable res1 = (R_Relocatable) pathAction.deducted;
		Pather pather = pathAction.pather;
		ITiledMap map = pather.map;
		MapTile tile = pather.map.getTile(res1.dLocation());
		if(!res1.dEnd() && !res1.dMDUsed())
		{
			if(res1.dAirState() == AirState.MOUNT)
			{
				//Dismount
				list.add(new Dismount1(costTable, res1.dMount(), AirState.UP));
				if(tile.type == MapTileType.FLOOR && res1.dMount().getAirState() == AirState.FLOOR)
					list.add(new Dismount1(costTable, res1.dMount(), AirState.FLOOR));
				else
					list.add(new Dismount1(costTable, res1.dMount(), AirState.FLY));
			}
			else
			{
				//Mount
				for(Relocatable m1 : map.objectsAt(res1.dLocation()))
				{
					list.add(new Mount1(costTable, m1));
					if(!res1.dDirection().equals(m1.getDirection()))
						list.add(new Mount1(costTable, res1.dDirection(), m1));
				}
			}
		}
		return list;
	}
}