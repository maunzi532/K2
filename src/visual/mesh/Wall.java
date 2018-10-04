package visual.mesh;

import com.jme3.math.*;
import visual.map.*;

public class Wall extends BauMesh1
{
	public Wall(int direction, WallType type, boolean low)
	{
		Vector3f[] edges = new Vector3f[7];
		Vector3f t0 = new Vector3f(0, 0, Scale.X_HEX_RADIUS);
		for(int i = 0; i < 7; i++)
		{
			edges[i] = turn(1, Vector3f.ZERO, t0, i, 6);
		}
		Vector3f sl = new Vector3f(0, Scale.FLOOR_DOWN, 0);
		Vector3f[] lkl = new Vector3f[7];
		Vector3f[] lku = new Vector3f[7];
		for(int i = 0; i < 7; i++)
		{
			lku[i] = edges[(i + direction) % 6].mult(1 - (type.ground[(i % 6)] * Scale.HEX_EDGE_IN));
			lkl[i] = lku[i].add(sl);
		}
		float tex0 = 1f + (2f / 3f * Scale.FLOOR_DOWN / Scale.CELLAR_HEIGHT);
		for(int i = 0; i < 6; i++)
		{
			quad(false, lkl[i], lkl[i + 1], lku[i], lku[i + 1]);
			texFlat4(i / 6f, 1f, (i + 1) / 6f, tex0);
		}
		Vector3f sh = new Vector3f(0, low ? Scale.LOW_WALL : Scale.CELLAR_UP, 0);
		Vector3f[] hkl = new Vector3f[7];
		Vector3f[] hku = new Vector3f[7];
		for(int i = 0; i < 7; i++)
		{
			hkl[i] = edges[(i + direction) % 6].mult(1 - (type.high[(i % 6)] * Scale.HEX_EDGE_IN));
			hku[i] = hkl[i].add(sh);
		}
		float tex1 = low ? 1f + (2f / 3f * (Scale.FLOOR_DOWN - Scale.LOW_WALL) / Scale.CELLAR_HEIGHT) : 1f / 3f;
		for(int i = 0; i < 6; i++)
		{
			quad(false, hkl[i], hkl[i + 1], hku[i], hku[i + 1]);
			texFlat4(i / 6f, tex0, (i + 1) / 6f, tex1);
		}
		flatMid(true, sl, lkl);
		flatMid(false, Vector3f.ZERO, lku);
		flatMid(false, sh, hku);
		texCircle(1f / 6f, 1f / 6f, 2f / 6f, 1f / 6f, 6, 1);
		texCircle(3f / 6f, 1f / 6f, 4f / 6f, 1f / 6f, 6, 1);
		texCircle(5f / 6f, 1f / 6f, 6f / 6f, 1f / 6f, 6, 1);
		finish();
	}
}