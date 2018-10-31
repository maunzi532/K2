package visual.mesh;

import com.jme3.math.*;

public class HorseMesh extends BauMesh1
{
	public HorseMesh(float low0, float high0, float back0, float front0, float width0,
			float width1, float back1, float front1, float low1, float high1, float fl1,
			float lowX2, float lowY2, float frontX2, float frontY2)
	{
		quad(true, new Vector3f(back0, low0, width0), new Vector3f(back0, high0, -width0));
		quad(true, new Vector3f(back0, low0, width0), new Vector3f(front0, high0, width0));
		quad(false, new Vector3f(back0, low0, -width0), new Vector3f(front0, high0, -width0));
		quad(false, new Vector3f(front0, low0, width0), new Vector3f(front0, high0, -width0));
		quad(false, new Vector3f(back0, low0, width0), new Vector3f(front0, low0, -width0));
		quad(false, new Vector3f(back0, high0, width0), new Vector3f(front0, high0, -width0));

		quad(true, new Vector3f(back1, high0, width1), new Vector3f(back1, high0, -width1),
				new Vector3f(front1, high1, width1), new Vector3f(front1, high1, -width1));
		quad(false, new Vector3f(front0, low1, width1), new Vector3f(front0, low1, -width1),
				new Vector3f(front1, fl1, width1), new Vector3f(front1, fl1, -width1));
		flat(true, new Vector3f(front0, high0, width1), new Vector3f(back1, high0, width1), new Vector3f(front1, high1, width1),
				new Vector3f(front1, fl1, width1), new Vector3f(front0, low1, width1));
		flat(false, new Vector3f(front0, high0, -width1), new Vector3f(back1, high0, -width1), new Vector3f(front1, high1, -width1),
				new Vector3f(front1, fl1, -width1), new Vector3f(front0, low1, -width1));

		quad(false, new Vector3f(frontX2, frontY2, width1), new Vector3f(frontX2, frontY2, -width1),
				new Vector3f(front1, high1, width1), new Vector3f(front1, high1, -width1));
		quad(false, new Vector3f(front1, fl1, width1), new Vector3f(front1, fl1, -width1),
				new Vector3f(lowX2, lowY2, width1), new Vector3f(lowX2, lowY2, -width1));
		quad(true, new Vector3f(frontX2, frontY2, width1), new Vector3f(frontX2, frontY2, -width1),
				new Vector3f(lowX2, lowY2, width1), new Vector3f(lowX2, lowY2, -width1));
		flat(false, new Vector3f(frontX2, frontY2, width1), new Vector3f(front1, high1, width1),
				new Vector3f(front1, fl1, width1), new Vector3f(lowX2, lowY2, width1));
		flat(true, new Vector3f(frontX2, frontY2, -width1), new Vector3f(front1, high1, -width1),
				new Vector3f(front1, fl1, -width1), new Vector3f(lowX2, lowY2, -width1));
		finish();
	}
}