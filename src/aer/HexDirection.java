package aer;

import java.io.*;

public class HexDirection implements Serializable
{
	public final int r;

	public HexDirection(int r)
	{
		this.r = r;
	}

	public HexDirection(HexDirection copy)
	{
		r = copy.r;
	}

	public boolean primary()
	{
		return r % 2 == 0;
	}

	public static HexDirection plus(HexDirection r1, HexDirection r2)
	{
		return new HexDirection((r1.r + r2.r) % 12);
	}

	public static HexDirection minus(HexDirection r1, HexDirection r2)
	{
		return new HexDirection((r1.r + 12 - r2.r) % 12);
	}

	public static HexDirection plus(HexDirection r1, int r2)
	{
		return new HexDirection((r1.r + r2) % 12);
	}

	public static HexDirection minus(HexDirection r1, int r2)
	{
		return new HexDirection((r1.r + 12 - r2) % 12);
	}

	public static int turnCost(HexDirection from, HexDirection to)
	{
		return Math.min((to.r + 12 - from.r) % 12, (from.r + 12 - to.r) % 12);
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof HexDirection)) return false;

		HexDirection that = (HexDirection) o;

		return r == that.r;
	}

	@Override
	public int hashCode()
	{
		return r;
	}

	@Override
	public String toString()
	{
		return "r=" + r;
	}
}