package aer.resource2.items;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.resource2.otheractions.*;
import aer.resource2.resourceTypes.*;
import java.util.*;

public class TargetingItem2 implements HexItem
{
	private CostTable costTable;

	public TargetingItem2(CostTable costTable)
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
			for(HexObject m1 : map.objectsAt(res1.dLocation()))
				list.add(new TargetingAction2(costTable, pather.getLoc(), pather.getDirection(), m1));
		}
		return list;
	}
}