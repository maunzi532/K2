package visual.mesh;

import com.jme3.math.*;

public class YArrow extends BauMesh1
{
	public YArrow(float y0, float y1, float x0, float x1, float x2, float z0, float z1)
	{
		quad(true, new Vector3f(x0, y0, z0), new Vector3f(x0, y1, -z0));
		quad(true, new Vector3f(x0, y0, z0), new Vector3f(x1, y1, z0));
		quad(false, new Vector3f(x0, y0, -z0), new Vector3f(x1, y1, -z0));
		quad(false, new Vector3f(x1, y0, z0), new Vector3f(x1, y1, z1));
		quad(true, new Vector3f(x1, y0, -z0), new Vector3f(x1, y1, -z1));
		quad(false, new Vector3f(x1, y0, z1), new Vector3f(x2, y0, 0), new Vector3f(x1, y1, z1), new Vector3f(x2, y1, 0));
		quad(true, new Vector3f(x1, y0, -z1), new Vector3f(x2, y0, 0), new Vector3f(x1, y1, -z1), new Vector3f(x2, y1, 0));
		flat(false, new Vector3f(x2, y0, 0), new Vector3f(x1, y0, z1), new Vector3f(x1, y0, z0), new Vector3f(x0, y0, z0),
				new Vector3f(x0, y0, -z0), new Vector3f(x1, y0, -z0), new Vector3f(x1, y0, -z1));
		flat(true, new Vector3f(x2, y1, 0), new Vector3f(x1, y1, z1), new Vector3f(x1, y1, z0), new Vector3f(x0, y1, z0),
				new Vector3f(x0, y1, -z0), new Vector3f(x1, y1, -z0), new Vector3f(x1, y1, -z1));
		finish();
	}
}