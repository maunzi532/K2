package aer;

public class PDirection
{
	public int h;
	public HexDirection direction;

	public PDirection(HexDirection direction)
	{
		this.direction = direction;
	}

	public static PDirection h(int h)
	{
		PDirection pDirection = new PDirection(null);
		pDirection.h = h;
		return pDirection;
	}
}