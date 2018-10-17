package aer.map.mapgen.mapgenpart;

import aer.locate.*;
import aer.map.maptiles.*;

public class HexWallMGP extends MapGenPart
{
	private final HexLocation mid;
	private final int radius;
	private final HWMGPType type;

	public HexWallMGP(HexLocation mid, int radius, HWMGPType type)
	{
		this.mid = mid;
		this.radius = radius;
		this.type = type;
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
				if(type.ceiling)
				{
					if(direction.primary())
						return new WFloorTile(map, loc, 5 - (direction.r / 2), type.corner);
					else
						return new WFloorTile(map, loc, 5 - (direction.r / 2), type.edge);
				}
				else
				{
					if(direction.primary())
						return new WallTile(map, loc, 5 - (direction.r / 2), type.corner);
					else
						return new WallTile(map, loc, 5 - (direction.r / 2), type.edge);
				}
			}
			else if(HexLocation.xdzDifference(mid, loc) < radius)
			{
				return new WFloorTile(map, loc, 0, type.mid);
			}
		}
		return null;
	}
}