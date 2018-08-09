package aer.resource2.items.item3;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.resource2.resource.*;
import java.util.*;

public class TargetingItem3 implements PatherItem
{
	private CostTable costTable;

	public TargetingItem3(CostTable costTable)
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
			for(Relocatable m1 : map.objectsAt(res1.dLocation()))
				list.add(new BaseTargeting3(costTable, pather.getLoc(), pather.getDirection(), m1));
		}
		return list;
	}
}