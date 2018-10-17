package aer.map.maptiles;

public enum WallType
{
	AIR(new int[]{0, 0, 1, 0, 0, 1}, new int[]{0, 0, 1, 0, 0, 1}),
	IN(new int[]{0, 0, 1, 0, 0, 0}, new int[]{0, 0, 1, 0, 0, 1}),
	GROUND(new int[]{0, 0, 0, 0, 0, 0}, new int[]{0, 0, 1, 0, 0, 1}),
	AIR_CORNER(new int[]{0, 0, 1, 1, 0, 0}, new int[]{0, 0, 1, 1, 0, 0}),
	GROUND_CORNER(new int[]{0, 0, 0, 0, 0, 0}, new int[]{0, 0, 1, 1, 0, 0}),
	FLOOR(new int[]{0, 0, 0, 0, 0, 0}, null);

	WallType(int[] ground, int[] high)
	{
		this.ground = ground;
		this.high = high;
	}

	public int[] ground;
	public int[] high;
}