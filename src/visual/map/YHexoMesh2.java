package visual.map;

import com.jme3.math.*;

public class YHexoMesh2 extends BauMesh1
{
	public YHexoMesh2(float s0, float s1, float s2, float l, float m, float h, int edgc)
	{
		cylinder(false, new Vector3f(0, h, 0), new Vector3f(0, h, s2),
				new Vector3f(0, m, 0), new Vector3f(0, m, s1), edgc);
		texCylinder(0f, 0f, 1f / edgc, 0.5f, edgc);
		cylinder(false, new Vector3f(0, m, 0), new Vector3f(0, m, s1),
				new Vector3f(0, l, 0), new Vector3f(0, l, s0), edgc);
		texCylinder(0f, 0.5f, 1f / edgc, 1f, edgc);
		finish();
	}
}