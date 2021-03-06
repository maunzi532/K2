package aer.resource2.items.item1;

import aer.locate.*;
import aer.map.*;
import aer.map.maptiles.*;
import aer.path.pather.*;
import aer.path.takeable.*;
import aer.relocatable.*;
import aer.relocatable.mount.*;
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
		if(!res1.dEnd() && !res1.dMTTUsed())
		{
			if(res1.dAirState() == AirState.MOUNT)
			{
				//Dismount
				if(tile.type == MapTileType.FLOOR && res1.dMount().getAirState() == AirState.FLOOR)
					list.add(new Dismount1(costTable, res1.dMount(), AirState.FLOOR));
				list.add(new Dismount1(costTable, res1.dMount(), AirState.FLY));
				//list.add(new Dismount1(costTable, res1.dMount(), AirState.UP));
			}
			else
			{
				//Mount
				for(Relocatable m1 : map.objectsAt(res1.dLocation()))
				{
					if(m1.getMType().canTransport(pather))
					{
						for(int i = 0; i < m1.getMountSlotCount(); i++)
						{
							if(m1.getMountSlotAt(i) == null)
							{
								MountSlotInfo mountSlotInfo = m1.getMountSlotInfo(i);
								list.add(new Mount1(costTable, res1.dDirection(), m1, i));
								if(mountSlotInfo.allowRotating && !res1.dDirection().equals(m1.getDirection()))
									list.add(new Mount1(costTable, m1, i));
							}
						}
					}
				}
			}
		}
		return list;
	}
}