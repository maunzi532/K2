package aer.locate;

public class CubeHex
{
	//x -> right
	//z -> down
	//Rotation: x, z, -x, -z, x
	//Pointy topped

	//   -c
	//+b    +a
	//-a    -b
	//   +c

	public static CubeHex[] directions = new CubeHex[]
			{
					new CubeHex(1, -1, 0),
					new CubeHex(1, -2, 1),
					new CubeHex(0, -1, 1),
					new CubeHex(-1, -1, 2),
					new CubeHex(-1, 0, 1),
					new CubeHex(-2, 1, 1),
					new CubeHex(-1, 1, 0),
					new CubeHex(-1, 2, -1),
					new CubeHex(0, 1, -1),
					new CubeHex(1, 1, -2),
					new CubeHex(1, 0, -1),
					new CubeHex(2, -1, -1)
			};

	public final int a, b, c;

	public CubeHex(int a, int b, int c)
	{
		this.a = a;
		this.b = b;
		this.c = c;
	}

	public static CubeHex fromTAxial(int x, int d)
	{
		return new CubeHex(x - d, -x, d);
	}

	public CubeHex addDirection(int r, int l)
	{
		if((r & 1) == 1)
		{
			l /= 2;
		}
		CubeHex add = directions[r];
		return new CubeHex(a + add.a * l, b + add.b * l, c + add.c * l);
	}

	public int distance(CubeHex hex)
	{
		return (Math.abs(a - hex.a) + Math.abs(b - hex.b) + Math.abs(b - hex.b)) / 2;
	}

	public int direction(CubeHex hex)
	{
		int a0 = Math.abs(hex.a - a);
		int b0 = Math.abs(hex.b - b);
		int c0 = Math.abs(hex.c - c);
		int re;
		if(b0 > c0 && b0 >= a0)
		{
			re = b > 0 ? 7 : 1;
			if(b0 == a0)
				re--;
		}
		else if(c0 > a0 && c0 >= b0)
		{
			re = c > 0 ? 3 : 9;
			if(c0 == b0)
				re--;
		}
		else if(a0 > b0 && a0 >= c0)
		{
			re = a > 0 ? 11 : 5;
			if(a0 == c0)
				re--;
		}
		else
			throw new NumberFormatException();
		return re;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof CubeHex)) return false;
		CubeHex cubeHex = (CubeHex) o;
		return a == cubeHex.a && b == cubeHex.b;
	}

	@Override
	public int hashCode()
	{
		int result = a;
		result = 31 * result + b;
		return result;
	}
}