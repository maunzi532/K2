package visual.mesh;

import com.jme3.math.*;
import java.util.*;
import visual.map.*;

public class Wall extends BauMesh1
{
	public Wall(int direction, WallType type, boolean low)
	{
		Vector3f[] edges = new Vector3f[6];
		Vector3f t0 = new Vector3f(0, 0, Scale.X_HEX_RADIUS);
		for(int i = 0; i < 6; i++)
		{
			edges[i] = turn(1, Vector3f.ZERO, t0, i, 6);
		}
		Vector3f sl = new Vector3f(0, Scale.FLOOR_DOWN, 0);
		List<Vector3f> lkl = new ArrayList<>();
		List<Vector3f> lku = new ArrayList<>();
		for(int i = 0; i < 6; i++)
		{
			if(type.ground[i] >= 0)
			{
				quad(false, edges[(i + direction) % 6].add(sl), edges[(type.ground[i] + direction) % 6].add(sl),
						edges[(i + direction) % 6], edges[(type.ground[i] + direction) % 6]);
				lkl.add(edges[(i + direction) % 6].add(sl));
				lku.add(edges[(i + direction) % 6]);
			}
		}
		Vector3f sh = new Vector3f(0, low ? Scale.LOW_WALL : Scale.CELLAR_UP, 0);
		List<Vector3f> hku = new ArrayList<>();
		for(int i = 0; i < 6; i++)
		{
			if(type.high[i] >= 0)
			{
				quad(false, edges[(i + direction) % 6], edges[(type.high[i] + direction) % 6],
						edges[(i + direction) % 6].add(sh), edges[(type.high[i] + direction) % 6].add(sh));
				hku.add(edges[(i + direction) % 6].add(sh));
			}
		}
		flat(true, lkl.toArray(new Vector3f[0]));
		flat(false, lku.toArray(new Vector3f[0]));
		flat(false, hku.toArray(new Vector3f[0]));
		finish();
	}
}