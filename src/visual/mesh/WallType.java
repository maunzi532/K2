package visual.mesh;

public enum WallType
{
	A1(new int[]{0, 0, 1, 0, 0, 1}, new int[]{0, 0, 1, 0, 0, 1}),
	G1(new int[]{0, 0, 1, 0, 0, 0}, new int[]{0, 0, 1, 0, 0, 1}),
	A2(new int[]{0, 0, 1, 1, 0, 0}, new int[]{0, 0, 1, 1, 0, 0}),
	G2(new int[]{0, 0, 1, 1, 0, 0}, new int[]{0, 0, 1, 1, 0, 0});

	WallType(int[] ground, int[] high)
	{
		this.ground = ground;
		this.high = high;
	}

	int[] ground;
	int[] high;
}