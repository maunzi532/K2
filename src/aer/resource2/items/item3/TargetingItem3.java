package aer.resource2.items.item3;

import aer.locate.*;
import aer.map.*;
import aer.map.maptiles.*;
import aer.path.pather.*;
import aer.path.takeable.*;
import aer.relocatable.*;
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
			for(HexLocation l0 : res1.dLocation().rangeLoc(0, 2))
				for(Relocatable m1 : map.objectsAt(l0))
					list.add(new BaseTargeting3(costTable, pather.getLoc(), pather.getDirection(), m1, false));
		}
		return list;
	}
}