package aer;

public class TAxialHex
{
	public final int x, d;

	public TAxialHex(int x, int d)
	{
		this.x = x;
		this.d = d;
	}

	public TAxialHex(TAxialHex copy)
	{
		x = copy.x;
		d = copy.d;
	}

	/*public static int xdzDifference(HexLocation l1, HexLocation l2)
	{
		int dx = l2.x - l1.x;
		int dd = l2.d - l1.d;
		if(dx * dd < 0)
			return Math.max(Math.abs(dx), Math.abs(dd));
		else
			return Math.abs(dx + dd);
	}

	public static HexDirection direction(HexLocation start, HexLocation end)
	{
		int dx = end.x - start.x;
		int dd = end.d - start.d;
		if(dx + dd == 0)
		{
			if(dx > 0)
				return new HexDirection(10);
			else if(dx < 0)
				return new HexDirection(4);
			else
				return null;
		}
		else if(dx == 0)
		{
			if(dd > 0)
				return new HexDirection(2);
			else
				return new HexDirection(8);
		}
		else if(dd == 0)
		{
			if(dx > 0)
				return new HexDirection(0);
			else
				return new HexDirection(6);
		}
		else if(dx * dd > 0)
		{
			if(dx > 0)
				return new HexDirection(1);
			else
				return new HexDirection(7);
		}
		else if(dx > 0)
		{
			if(dx + dd > 0)
				return new HexDirection(11);
			else
				return new HexDirection(9);
		}
		else
		{
			if(dx + dd > 0)
				return new HexDirection(3);
			else
				return new HexDirection(5);
		}
	}

	public static HexLocation addDirection(HexLocation loc, HexDirection direction, int len)
	{
		if(!direction.primary() && len % 2 == 1)
			throw new RuntimeException();
		switch(direction.r)
		{
			case 0:
				return new HexLocation(loc, len, 0, 0);
			case 1:
				return new HexLocation(loc, len / 2, len / 2, 0);
			case 2:
				return new HexLocation(loc, 0, len, 0);
			case 3:
				return new HexLocation(loc, 0, len / 2, len / 2);
			case 4:
				return new HexLocation(loc, 0, 0, len);
			case 5:
				return new HexLocation(loc, -len / 2, 0, len / 2);
			case 6:
				return new HexLocation(loc, -len, 0, 0);
			case 7:
				return new HexLocation(loc, -len / 2, -len / 2, 0);
			case 8:
				return new HexLocation(loc, 0, -len, 0);
			case 9:
				return new HexLocation(loc, 0, -len / 2, -len / 2);
			case 10:
				return new HexLocation(loc, 0, 0, -len);
			case 11:
				return new HexLocation(loc, len / 2, 0, -len / 2);
			default:
				throw new RuntimeException();
		}
	}*/
}