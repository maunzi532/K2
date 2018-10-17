package aer.map.mapgen.mapgenpart;

import aer.map.maptiles.*;

public enum HWMGPType
{
	GROUND(false, WallType.GROUND_CORNER, WallType.GROUND, WallType.FLOOR),
	FLOOR(false, WallType.AIR_CORNER, WallType.IN, WallType.FLOOR),
	CEILING(true, WallType.AIR_CORNER, WallType.IN, WallType.FLOOR);

	HWMGPType(boolean ceiling, WallType corner, WallType edge, WallType mid)
	{
		this.ceiling = ceiling;
		this.corner = corner;
		this.edge = edge;
		this.mid = mid;
	}

	boolean ceiling;
	WallType corner;
	WallType edge;
	WallType mid;
}