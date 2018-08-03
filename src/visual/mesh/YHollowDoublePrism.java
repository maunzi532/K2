package visual.mesh;

import com.jme3.math.*;

public class YHollowDoublePrism extends BauMesh1
{
	public YHollowDoublePrism(float s0, float s1, float s2, float y0, float y1, float y2, boolean inv0, boolean inv1, int edgc)
	{
		cylinder(inv0, new Vector3f(0, y0, 0), new Vector3f(0, y0, s0),
				new Vector3f(0, y1, 0), new Vector3f(0, y1, s1), edgc);
		texCylinder(0f, 0f, 1f / edgc, 0.5f, edgc);
		cylinder(inv1, new Vector3f(0, y1, 0), new Vector3f(0, y1, s1),
				new Vector3f(0, y2, 0), new Vector3f(0, y2, s2), edgc);
		texCylinder(0f, 0.5f, 1f / edgc, 1f, edgc);
		finish();
	}
}