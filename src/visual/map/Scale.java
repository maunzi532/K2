package visual.map;

import com.jme3.math.*;

public class Scale
{
	public static final float FLOOR_DOWN = -3f;
	public static final float CELLAR_HEIGHT = 16f;
	public static final float CELLAR_UP = CELLAR_HEIGHT + FLOOR_DOWN;
	public static final float X_HEX_RADIUS = 4f;
	public static final float X_HEX_HALF = FastMath.sqrt(X_HEX_RADIUS * X_HEX_RADIUS * 3f / 4f);
	public static final float DZ_HEX_DIFF = X_HEX_RADIUS * 1.5f;
}