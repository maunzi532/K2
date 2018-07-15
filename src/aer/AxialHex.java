package aer;

public class AxialHex
{
	public final int x, d;

	public AxialHex(int x, int d)
	{
		this.x = x;
		this.d = d;
	}

	public AxialHex(AxialHex copy)
	{
		x = copy.x;
		d = copy.d;
	}
}