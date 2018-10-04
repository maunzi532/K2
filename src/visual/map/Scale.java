package visual.map;

import com.jme3.math.*;

public class Scale
{
	public static final float FLOOR_DOWN = -3f;
	public static final float CELLAR_HEIGHT = 16f;
	public static final float CELLAR_UP = CELLAR_HEIGHT + FLOOR_DOWN;
	public static final float LOW_WALL = 2.5f;
	public static final float X_HEX_RADIUS = 4f;
	public static final float X_HEX_TANGENT = X_HEX_RADIUS * FastMath.sqrt(3f) / 2f;
	public static final float DZ_HEX_DIFF = X_HEX_RADIUS * 1.5f;
	public static final float HEX_EDGE_IN = 0.5f;
}