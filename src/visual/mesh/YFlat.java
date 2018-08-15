package visual.mesh;

import com.jme3.math.*;

public class YFlat extends BauMesh1
{
	public YFlat(float s0, float l, int edgc)
	{
		circle(false, Vector3f.UNIT_Y, new Vector3f(0, l, 0), new Vector3f(0, l, s0), edgc);
		texCircle(0.5f, 0.5f, 0.5f, 0f, edgc);
		finish();
	}
}