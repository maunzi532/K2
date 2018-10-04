package aer.locate.pathseeker;

import aer.locate.*;
import aer.map.*;
import aer.map.maptiles.*;
import java.util.*;

public class PathSeekerH
{
	public final List<PDirection> directions;
	public final int len;

	public PathSeekerH(ITiledMap map, HexLocation start, HexLocation end, MovementTileType mType)
	{
		if(start.r != end.r)
		{
			directions = null;
			len = 0;
			return;
		}
		int hDiff = end.h - start.h;
		HexDirection direction = HexLocation.direction(start, end);
		if(direction == null)
		{
			directions = new ArrayList<>();
			if(hDiff >= 0)
			{
				len = hDiff;
				for(int i = 0; i < hDiff; i++)
					directions.add(PDirection.h(1));
			}
			else
			{
				len = -hDiff;
				for(int i = 0; i < -hDiff; i++)
					directions.add(PDirection.h(-1));
			}
			return;
		}
		HexDirection x0;
		HexDirection x1;
		if(direction.r % 2 > 0)
		{
			x0 = HexDirection.minus(direction, 1);
			x1 = HexDirection.plus(direction, 1);
		}
		else
		{
			x0 = direction;
			x1 = HexDirection.plus(direction, 2);
		}
		int l0 = 1;
		HexLocation a0 = start;
		while(true)
		{
			HexLocation b0 = HexLocation.addDirection(a0, x0, 1);
			if(HexLocation.xdzDifference(end, b0) >= HexLocation.xdzDifference(end, a0))
				break;
			l0++;
			a0 = b0;
		}
		int l1 = 1;
		HexLocation a1 = start;
		while(true)
		{
			HexLocation b1 = HexLocation.addDirection(a1, x1, 1);
			if(HexLocation.xdzDifference(end, b1) >= HexLocation.xdzDifference(end, a1))
				break;
			l1++;
			a1 = b1;
		}
		int lh = Math.abs(hDiff) + 1;
		int sh = hDiff >= 0 ? 1 : -1;
		int[][][] arr = new int[l0][l1][lh];
		int ld = l0 + l1 + lh - 2;
		for(int d = 0; d < ld; d++)
		{
			for(int a = 0; a < l0; a++)
			{
				for(int b = 0; b < l1; b++)
				{
					int ch = d - a - b;
					if(d < 0 || d >= lh)
						continue;
					if(a > 0 || b > 0 || ch > 0)
					{
						arr[a][b][ch] = -1;
						if(a > 0)
						{
							int prev = arr[a - 1][b][ch];
							if(prev >= 0)
							{
								int re = movementCost(map, start, x0, a, x1, b, sh, ch, mType, 0);
								if(re >= 0)
								{
									arr[a][b][ch] = re + prev;
								}
							}
						}
						if(b > 0)
						{
							int prev = arr[a][b - 1][ch];
							if(prev >= 0)
							{
								int re = movementCost(map, start, x0, a, x1, b, sh, ch, mType, 1);
								if(re >= 0 && (arr[a][b][ch] < 0 || re + prev < arr[a][b][ch]))
								{
									arr[a][b][ch] = re + prev;
								}
							}
						}
						if(ch > 0)
						{
							int prev = arr[a][b][ch - 1];
							if(prev >= 0)
							{
								int re = movementCost(map, start, x0, a, x1, b, sh, ch, mType, 2);
								if(re >= 0 && (arr[a][b][ch] < 0 || re + prev < arr[a][b][ch]))
								{
									arr[a][b][ch] = re + prev;
								}
							}
						}
					}
				}
			}
		}
		len = arr[l0 - 1][l1 - 1][lh - 1];
		if(len < 0)
		{
			directions = null;
			return;
		}
		directions = new ArrayList<>();
		int a = l0 - 1;
		int b = l1 - 1;
		int ch = lh - 1;
		boolean f = false;
		while(a > 0 || b > 0)
		{
			int r0 = a > 0 ? arr[a - 1][b][ch] : -1;
			int r1 = b > 0 ? arr[a][b - 1][ch] : -1;
			int r2 = b > 0 ? arr[a][b][ch - 1] : -1;
			if(r0 < 0 && r1 < 0 && r2 < 0)
				throw new RuntimeException();
			int m = Math.max(Math.max(r0, r1), r2) + 1;
			if(r0 < 0)
				r0 = m + 1;
			if(r1 < 0)
				r1 = m + 1;
			if(r2 < 0)
				r2 = m + 1;
			if(r2 <= r0 && r2 <= r1)
			{
				directions.add(PDirection.h(sh));
				ch--;
			}
			else if(r0 < r1 || (f && r0 <= r1))
			{
				directions.add(new PDirection(x0));
				a--;
			}
			else
			{
				directions.add(new PDirection(x1));
				b--;
			}
			f = !f;
		}
		Collections.reverse(directions);
	}

	private int movementCost(ITiledMap map, HexLocation start, HexDirection x0, int a, HexDirection x1, int b, int sh, int ch, MovementTileType mType, int n)
	{
		PDirection pDirection;
		if(n == 0)
			pDirection = new PDirection(x0);
		else if(n == 1)
			pDirection = new PDirection(x1);
		else
			pDirection = PDirection.h(sh);
		MapTile exiting = map.getTile(new HexLocation(HexLocation
				.addDirection(HexLocation.addDirection(start, x0, n == 0 ? a - 1 : a), x1, n == 1 ? b - 1 : b), 0, 0,
				sh * (n == 2 ? ch - 1 : ch), 0));
		int exitingCost = exiting.exitingCost(pDirection,  mType);
		MapTile entering = map.getTile(new HexLocation(HexLocation.addDirection(HexLocation.addDirection(start, x0, a), x1, b), 0, 0, sh * ch, 0));
		int enteringCost = entering.enteringCost(pDirection,  mType);
		if(exitingCost < 0 || enteringCost < 0)
			return -1;
		return exitingCost + enteringCost;
	}
}