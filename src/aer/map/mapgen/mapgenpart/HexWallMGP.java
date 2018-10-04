package aer.map.mapgen.mapgenpart;

import aer.locate.*;
import aer.map.maptiles.*;
import visual.mesh.*;

public class HexWallMGP extends MapGenPart
{
	private final HexLocation mid;
	private final int radius;

	public HexWallMGP(HexLocation mid, int radius)
	{
		this.mid = mid;
		this.radius = radius;
	}

	@Override
	protected MapTile tile(HexLocation loc)
	{
		if(loc.r == mid.r && loc.h == mid.h && HexLocation.xdzDifference(mid, loc) == radius)
		{
			HexDirection direction = HexLocation.direction(mid, loc);
			if(direction == null)
				throw new RuntimeException();
			if(direction.primary())
				return new WallTile(map, loc, 5 - (direction.r / 2), WallType.G2);
			else
				return new WallTile(map, loc, 5 - (direction.r / 2), WallType.G1);
		}
		return null;
	}
}