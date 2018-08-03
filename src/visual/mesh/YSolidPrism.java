package visual.mesh;

import com.jme3.math.*;

public class YSolidPrism extends BauMesh1
{
	public YSolidPrism(float s0, float l, float h, int edgc)
	{
		this(s0, s0, l, h, edgc);
	}

	public YSolidPrism(float s0, float s1, float l, float h, int edgc)
	{
		//oben
		circle(false, Vector3f.UNIT_Y, new Vector3f(0, h, 0), new Vector3f(0, h, s1), edgc);
		texCircle(0.25f, 0.25f, 0.25f, 0f, edgc);
		//unten
		circle(false, Vector3f.UNIT_Y.negate(), new Vector3f(0, l, 0), new Vector3f(0, l, s0), edgc);
		texCircle(0.75f, 0.25f, 0.75f, 0f, edgc);
		//seiten
		cylinder(true, new Vector3f(0, h, 0), new Vector3f(0, h, s1),
				new Vector3f(0, l, 0), new Vector3f(0, l, s0), edgc);
		texCylinder(0f, 0.5f, 1f / edgc, 1f, edgc);
		//ende
		finish();
	}
}