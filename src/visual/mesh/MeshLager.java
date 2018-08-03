package visual.mesh;

import com.jme3.asset.*;
import com.jme3.material.*;
import com.jme3.math.*;
import com.jme3.scene.*;
import visual.map.*;

public class MeshLager
{
	public static Mesh floorMesh;
	public static Material floorMat;
	public static Mesh blockedMesh;
	public static Material blockedMat;
	public static Mesh possibleActionMeshLoc;
	public static Mesh possibleActionMeshObj;
	public static Material possibleActionMatLoc;
	public static Material possibleActionMatObj;
	public static Material possibleActionMatOth;
	public static Mesh objectMesh;
	public static Material objectMat;

	public static void init(AssetManager assetManager)
	{
		floor(assetManager);
		blocked(assetManager);
		possibleActionLoc(assetManager);
		possibleActionObj(assetManager);
		object(assetManager);
	}

	private static Material material(AssetManager assetManager, ColorRGBA color)
	{
		Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		material.setBoolean("UseMaterialColors",true);
		material.setColor("Diffuse", color);
		material.setColor("Ambient", color);
		if(color.a < 1)
			material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		return material;
	}

	private static void floor(AssetManager assetManager)
	{
		floorMesh = new YSolidPrism(Scale.X_HEX_RADIUS, Scale.X_HEX_RADIUS * 0.9f, Scale.FLOOR_DOWN, 0f, 6);
		floorMat = material(assetManager, ColorRGBA.Green);
	}

	private static void blocked(AssetManager assetManager)
	{
		blockedMesh = new YSolidPrism(Scale.X_HEX_RADIUS, Scale.FLOOR_DOWN, Scale.CELLAR_UP, 6);
		blockedMat = material(assetManager, ColorRGBA.Brown);
	}

	private static void possibleActionLoc(AssetManager assetManager)
	{
		possibleActionMeshLoc = new YHollowDoublePrism(Scale.X_HEX_RADIUS * 0.9f, Scale.X_HEX_RADIUS * 0.8f,
				Scale.X_HEX_RADIUS * 0.75f, 0f, -Scale.FLOOR_DOWN * 0.6f, 0f, false, false, 6);
		possibleActionMatLoc = material(assetManager, new ColorRGBA(1, 0, 1, 0.4f));
	}

	private static void possibleActionObj(AssetManager assetManager)
	{
		possibleActionMeshObj = new YHollowDoublePrism(
				Scale.X_HEX_RADIUS * 0.9f, Scale.X_HEX_RADIUS * 0.6f, Scale.X_HEX_RADIUS * 0.9f,
				Scale.FLOOR_DOWN * -0.4f, Scale.FLOOR_DOWN * -1f, Scale.FLOOR_DOWN * -1.6f, false, true, 12);
		possibleActionMatObj = material(assetManager, new ColorRGBA(1f, 0f, 0.4f, 0.7f));
		possibleActionMatOth = material(assetManager, new ColorRGBA(1f, 0.4f, 0f, 0.7f));
	}

	private static void object(AssetManager assetManager)
	{
		objectMesh = new YSolidPrism(Scale.X_HEX_RADIUS * 0.5f, 0f, Scale.CELLAR_HEIGHT / 4f, 3);
		objectMat = material(assetManager, ColorRGBA.Red);
	}
}