package visual.pather;

import com.jme3.math.*;
import visual.map.*;

public class YHexoMeshT extends BauMesh1
{
	public YHexoMeshT(float s0, float s1, float s2, float l, float h, int edgc)
	{
		//oben
		/*circle(false, Vector3f.UNIT_Y, new Vector3f(0, h, 0), new Vector3f(0, h, s), edgc);
		texCircle(0.25f, 0.25f, 0.25f, 0f, edgc);
		circle(true, Vector3f.UNIT_Y, new Vector3f(0, h, 0), new Vector3f(0, h, s), edgc);
		texCircle(0.25f, 0.25f, 0.25f, 0f, edgc);*/
		//unten
		/*circle(false, Vector3f.UNIT_Y.negate(), new Vector3f(0, l, 0), new Vector3f(0, l, s), edgc);
		texCircle(0.75f, 0.25f, 0.75f, 0f, edgc);
		circle(true, Vector3f.UNIT_Y.negate(), new Vector3f(0, l, 0), new Vector3f(0, l, s), edgc);
		texCircle(0.75f, 0.25f, 0.75f, 0f, edgc);*/
		//seiten
		cylinder(true, new Vector3f(0, h, 0), new Vector3f(0, h, s1),
				new Vector3f(0, l, 0), new Vector3f(0, l, s0), edgc);
		texCylinder(0f, 0.5f, 1f / edgc, 1f, edgc);
		cylinder(false, new Vector3f(0, h, 0), new Vector3f(0, h, s1),
				new Vector3f(0, l, 0), new Vector3f(0, l, s2), edgc);
		texCylinder(0f, 0.5f, 1f / edgc, 1f, edgc);
		//ende
		finish();
	}
}