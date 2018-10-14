package aer.map.mapgen.mapgenpart;

import aer.locate.*;
import aer.map.maptiles.*;

public class HexWallMGP extends MapGenPart
{
	private final HexLocation mid;
	private final int radius;
	private final boolean ground;

	public HexWallMGP(HexLocation mid, int radius, boolean ground)
	{
		this.mid = mid;
		this.radius = radius;
		this.ground = ground;
	}

	@Override
	protected MapTile tile(HexLocation loc)
	{
		if(loc.r == mid.r && loc.h == mid.h)
		{
			if(HexLocation.xdzDifference(mid, loc) == radius)
			{
				HexDirection direction = HexLocation.direction(mid, loc);
				if(direction == null)
					throw new RuntimeException();
				if(direction.primary())
					return new WallTile(map, loc, 5 - (direction.r / 2), ground ? WallType.GROUND_EDGE : WallType.AIR_EDGE);
				else
					return new WallTile(map, loc, 5 - (direction.r / 2), ground ? WallType.GROUND : WallType.GROUND_IN);
			}
			else if(HexLocation.xdzDifference(mid, loc) < radius)
			{
				return new WFloorTile(map, loc, 0, WallType.FLOOR);
			}
		}
		return null;
	}
}