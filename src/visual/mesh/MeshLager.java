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
	public static Mesh locationMesh;
	public static Material locationMat;
	public static Material locationMatDisabled;
	public static Mesh possibleActionMeshLoc;
	public static Material possibleActionMatLoc;
	public static Material activeLocMat;
	public static Mesh possibleActionMeshObj;
	public static Material possibleActionMatObj;
	public static Material activeObjMat;
	public static Material activeOtherMat;
	public static Mesh directionArrowMesh;
	public static Material directionArrowMat;
	public static Material activeArrowMat;
	public static Mesh objectMesh;
	public static Material objectMat;
	public static Material pathLocMat;

	public static void init(AssetManager assetManager)
	{
		floor(assetManager);
		blocked(assetManager);
		location(assetManager);
		possibleActionLoc(assetManager);
		possibleActionObj(assetManager);
		directionArrow(assetManager);
		object(assetManager);
	}

	private static Material material(AssetManager assetManager, ColorRGBA color)
	{
		Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		material.setBoolean("UseMaterialColors",true);
		material.setColor("Diffuse", color);
		material.setColor("Ambient", color);
		if(color.a < 1f)
			material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		return material;
	}

	private static void floor(AssetManager assetManager)
	{
		floorMesh = new YSolidPrism(Scale.X_HEX_RADIUS, Scale.X_HEX_RADIUS * 0.9f, Scale.FLOOR_DOWN, 0f, 6);
		floorMat = material(assetManager, new ColorRGBA(0f, 1f, 0f, 1f));
	}

	private static void blocked(AssetManager assetManager)
	{
		blockedMesh = new YSolidPrism(Scale.X_HEX_RADIUS, Scale.FLOOR_DOWN, Scale.CELLAR_UP, 6);
		blockedMat = material(assetManager, new ColorRGBA(65f / 255f, 40f / 255f, 25f / 255f, 1f));
	}

	private static void location(AssetManager assetManager)
	{
		locationMesh = new YSolidPrism(Scale.X_HEX_RADIUS, Scale.CELLAR_HEIGHT * -0.2f, Scale.FLOOR_DOWN * -0.2f, 6);
		locationMat = material(assetManager, new ColorRGBA(0.8f, 0.8f, 1f, 0.3f));
		locationMatDisabled = material(assetManager, new ColorRGBA(1f, 0.8f, 0.8f, 0.2f));
	}

	private static void possibleActionLoc(AssetManager assetManager)
	{
		possibleActionMeshLoc = new YHollowDoublePrism(Scale.X_HEX_RADIUS * 0.9f, Scale.X_HEX_RADIUS * 0.8f,
				Scale.X_HEX_RADIUS * 0.75f, 0f, -Scale.FLOOR_DOWN * 0.6f, 0f, false, false, 6);
		possibleActionMatLoc = material(assetManager, new ColorRGBA(1f, 0f, 1f, 0.4f));
		activeLocMat = material(assetManager, new ColorRGBA(1f, 0.4f, 0f, 0.8f));
	}

	private static void possibleActionObj(AssetManager assetManager)
	{
		possibleActionMeshObj = new YHollowDoublePrism(
				Scale.X_HEX_RADIUS * 0.9f, Scale.X_HEX_RADIUS * 0.6f, Scale.X_HEX_RADIUS * 0.9f,
				Scale.FLOOR_DOWN * -0.4f, Scale.FLOOR_DOWN * -1f, Scale.FLOOR_DOWN * -1.6f, false, true, 12);
		possibleActionMatObj = material(assetManager, new ColorRGBA(1f, 0f, 0.4f, 0.7f));
		activeObjMat = material(assetManager, new ColorRGBA(1f, 0.4f, 0.4f, 0.8f));
		activeOtherMat = material(assetManager, new ColorRGBA(1f, 0.4f, 0f, 0.7f));
	}

	private static void directionArrow(AssetManager assetManager)
	{
		directionArrowMesh = new YArrow(Scale.FLOOR_DOWN * -0.5f, Scale.FLOOR_DOWN * -0.7f,
				Scale.X_HEX_RADIUS * 1.1f, Scale.X_HEX_RADIUS * 1.5f, Scale.X_HEX_RADIUS * 1.7f,
				Scale.X_HEX_RADIUS * 0.05f, Scale.X_HEX_RADIUS * 0.15f);
		directionArrowMat = material(assetManager, new ColorRGBA(65f / 255f, 40f / 255f, 25f / 255f, 1f));
		activeArrowMat = material(assetManager, new ColorRGBA(1f, 1f, 0f, 1f));
	}

	private static void object(AssetManager assetManager)
	{
		objectMesh = new YSolidPrismX(Scale.X_HEX_RADIUS * 0.5f, 0f, Scale.CELLAR_HEIGHT / 4f, 3);
		objectMat = material(assetManager, new ColorRGBA(1f, 0f, 0f, 1f));
		pathLocMat = material(assetManager, new ColorRGBA(1f, 0.4f, 0f, 0.7f));
	}
}