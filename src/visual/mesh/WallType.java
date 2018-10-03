package visual.mesh;

public enum WallType
{
	A1(new int[]{-1, 2, 4, -1, 5, 1}, new int[]{-1, 2, 4, -1, 5, 1}),
	G1(new int[]{-1, 2, 3, 4, 5, 1}, new int[]{-1, 2, 4, -1, 5, 1});

	WallType(int[] ground, int[] high)
	{
		this.ground = ground;
		this.high = high;
	}

	int[] ground;
	int[] high;
}