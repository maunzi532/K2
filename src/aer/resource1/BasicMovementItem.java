package aer.resource1;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;
import java.util.*;
import sun.reflect.generics.reflectiveObjects.*;

public class BasicMovementItem implements HexItem
{
	public boolean fly;
	public CostTable costTable;

	public BasicMovementItem(boolean fly, CostTable costTable)
	{
		this.fly = fly;
		this.costTable = costTable;
	}

	@Override
	public List<TakeableAction> takeableActions(PathAction pathAction)
	{
		List<TakeableAction> list = new ArrayList<>();
		APResource resource = (APResource) pathAction.deducted;
		HexPather pather = pathAction.pather;
		IHexMap map = pather.map;
		MapTile tile = pather.map.getTile(resource.location);
		if(!resource.dizzy)
		{
			if(fly || resource.airState.canTurn)
				for(int i = 0; i < 12; i++)
					if(i != resource.direction.r)
						list.add(new TurnAction(costTable, resource.direction, new HexDirection(i)));
		}
		if(resource.airState == AirState.MOUNT)
		{
			if(!resource.hasMounted && !resource.hasDismounted)
			{
				list.add(new DismountAction(costTable, resource.mount, AirState.UP));
				if(tile.type == MapTileType.FLOOR && resource.mount.getAirState() == AirState.FLOOR)
					list.add(new DismountAction(costTable, resource.mount, AirState.FLOOR));
				else
					list.add(new DismountAction(costTable, resource.mount, AirState.FLY));
			}
		}
		else
		{
			if(!resource.hasMounted && !resource.hasDismounted)
			{
				for(HexObject m1 : map.objectsAt(resource.location))
				{
					list.add(new MountAction(costTable, m1));
					if(!resource.dizzy)
						list.add(new MountAction(costTable, resource.direction, m1));
				}
			}
			if(!resource.dizzy && (fly || resource.airState == AirState.FLOOR))
			{
				int maxH = maxH(true);
				for(int ih = -maxH(false); ih <= maxH; ih++)
				{
					int maxM = maxMovement(resource.actionPoints, ih);
					for(int ix = -maxM; ix <= maxM; ix++)
					{
						int maxD = Math.min(maxM, maxM - ix);
						for(int id = Math.max(-maxM, -maxM - ix); id <= maxD; id++)
						{
							HexLocation end = new HexLocation(resource.location, ix, id, ih, 0);
							if(!resource.location.equals(end))
							{
								if(fly)
								{
									throw new NotImplementedException();
								}
								else
								{
									MovementAction2D mv = new MovementAction2D(costTable, resource.direction, map, resource.location, end);
									if(mv.possible())
										list.add(mv);
								}
							}
						}
					}
				}
			}
			if(resource.airState.isAerial && resource.airState.fall < 0 && tile.type == MapTileType.FLOOR)
				list.add(new LandingAction(costTable));
			if(!fly && resource.airState.isAerial)
			{
				if(tile.type != MapTileType.FLOOR)
					list.add(new FallAction(costTable, map, resource.location, resource.airState));
				if(resource.airState.canAirdash && (resource.airState.fall >= 0 || tile.type != MapTileType.FLOOR))
				{
					int maxA = maxAirdash();
					for(int i = 1; i < maxA; i++)
						if(resource.direction.primary() || i % 2 == 0)
						{
							AirdashAction ad = new AirdashAction(costTable, map, resource.location,
									HexLocation.addDirection(resource.location, resource.direction, i),
									resource.direction, resource.airState);
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

	public int maxH(boolean up)
	{
		return 0;
	}

	public int maxMovement(int movePoints, int h)
	{
		if(fly)
			return 0;
		else
			return (movePoints - costTable.initMoveM) / costTable.moveCostM;
	}
}