package aer.locate.pathseeker;

import aer.locate.*;
import aer.map.*;
import aer.map.maptiles.*;
import java.util.*;
import java.util.function.*;

public class PathSeeker
{
	public final List<HexDirection> directions;
	public final int len;

	public PathSeeker(ITiledMap map, HexLocation start, HexLocation end, Function<MapTile, Integer> function)
	{
		if(start.r != end.r || start.h != end.h)
		{
			directions = null;
			len = 0;
			return;
		}
		HexDirection direction = HexLocation.direction(start, end);
		if(direction == null)
		{
			directions = Collections.EMPTY_LIST;
			len = 0;
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
		int[][] arr = new int[l0][l1];
		int ld = l0 + l1 - 1;
		for(int d = 0; d < ld; d++)
		{
			for(int a = 0; a < l0; a++)
			{
				int b = d - a;
				if(b < 0 || b >= l1)
					continue;
				if(a > 0 || b > 0)
				{
					arr[a][b] = -1;
					if(a > 0)
					{
						int prev = arr[a - 1][b];
						if(prev >= 0)
						{
							int re = function.apply(map.getTile(HexLocation.addDirection(HexLocation.addDirection(start, x0, a), x1, b)));
							if(re >= 0)
								arr[a][b] = re + prev;
						}
					}
					if(b > 0)
					{
						int prev = arr[a][b - 1];
						if(prev >= 0)
						{
							int re = function.apply(map.getTile(HexLocation.addDirection(HexLocation.addDirection(start, x0, a), x1, b)));
							if(re >= 0 && (arr[a][b] < 0 || re + prev < arr[a][b]))
							{
								arr[a][b] = re + prev;
							}
						}
					}
				}
			}
		}
		len = arr[l0 - 1][l1 - 1];
		if(len < 0)
		{
			directions = null;
			return;
		}
		directions = new ArrayList<>();
		int a = l0 - 1;
		int b = l1 - 1;
		boolean f = false;
		while(a > 0 || b > 0)
		{
			int r0 = a > 0 ? arr[a - 1][b] : -1;
			int r1 = b > 0 ? arr[a][b - 1] : -1;
			if(r0 < 0 && r1 < 0)
				throw new RuntimeException();
			if(r0 < 0)
				r0 = r1 + 1;
			else if(r1 < 0)
				r1 = r0 + 1;
			if(r0 < r1 || (f && r0 <= r1))
			{
				directions.add(x0);
				a--;
			}
			else
			{
				directions.add(x1);
				b--;
			}
			f = !f;
		}
		Collections.reverse(directions);
	}
}