package visual.mesh;

import com.jme3.asset.*;
import com.jme3.material.*;
import com.jme3.math.*;
import com.jme3.scene.*;
import java.util.*;
import java.util.function.*;
import visual.map.*;

public class MeshLager
{
	public static AssetManager am;
	public static Map<String, Mesh> meshes;
	public static Map<String, Material> materials;

	public static Mesh floorMesh;
	public static Material floorMat;
	public static Mesh blockedMesh;
	public static Mesh blockedMeshV;
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
	public static Material[] objectMat;
	public static Material pathLocMat;

	public static void init(AssetManager assetManager)
	{
		am = assetManager;
		meshes = new HashMap<>();
		materials = new HashMap<>();
		floor();
		blocked();
		location();
		possibleActionLoc();
		possibleActionObj();
		directionArrow();
		object();
	}

	private static Material material(ColorRGBA color)
	{
		Material material = new Material(am, "Common/MatDefs/Light/Lighting.j3md");
		material.setBoolean("UseMaterialColors",true);
		material.setColor("Diffuse", color);
		material.setColor("Ambient", color);
		if(color.a < 1f)
			material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		return material;
	}

	private static Material material(String name, boolean alpha)
	{
		Material material = new Material(am, "Common/MatDefs/Light/Lighting.j3md");
		material.setTexture("DiffuseMap", am.loadTexture(new TextureKey(name, false)));
		if(alpha)
			material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		return material;
	}

	private static void floor()
	{
		floorMesh = new YSolidPrism(Scale.X_HEX_RADIUS, Scale.X_HEX_RADIUS * 0.9f, Scale.FLOOR_DOWN, 0f, 6);
		/*floorMat = material(assetManager, new ColorRGBA(0f, 1f, 0f, 1f));
		floorMat.setBoolean("UseMaterialColors",false);
		floorMat.setTexture("DiffuseMap", assetManager.loadTexture(new TextureKey("Textures/Floor.png", false)));*/
		floorMat = material("Textures/Floor.png", false);
	}

	private static void blocked()
	{
		/*blockedMesh = new YSolidPrism(Scale.X_HEX_RADIUS, Scale.FLOOR_DOWN, Scale.CELLAR_UP, 6);
		blockedMeshV = new YSolidPrism(Scale.X_HEX_RADIUS, Scale.FLOOR_DOWN, Scale.LOW_WALL, 6);*/
		blockedMesh = new Wall(1, WallType.G1, false);
		blockedMeshV = new Wall(1, WallType.G1, true);
		blockedMat = material(new ColorRGBA(65f / 255f, 40f / 255f, 25f / 255f, 1f));
		//((BauMesh1) blockedMesh).outputTextureMap(256, 256, 16);
	}

	private static void location()
	{
		locationMesh = new YSolidPrism(Scale.X_HEX_RADIUS, Scale.CELLAR_HEIGHT * -0.2f, Scale.FLOOR_DOWN * -0.2f, 6);
		locationMat = material(new ColorRGBA(0.8f, 0.8f, 1f, 0.3f));
		locationMatDisabled = material(new ColorRGBA(1f, 0.8f, 0.8f, 0.2f));
	}

	private static void possibleActionLoc()
	{
		possibleActionMeshLoc = new YHollowDoublePrism(Scale.X_HEX_RADIUS * 0.9f, Scale.X_HEX_RADIUS * 0.8f,
				Scale.X_HEX_RADIUS * 0.75f, 0f, -Scale.FLOOR_DOWN * 0.6f, 0f, false, false, 6);
		possibleActionMatLoc = material(new ColorRGBA(1f, 0f, 1f, 0.4f));
		activeLocMat = material(new ColorRGBA(1f, 0.4f, 0f, 0.8f));
	}

	private static void possibleActionObj()
	{
		possibleActionMeshObj = new YHollowDoublePrism(
				Scale.X_HEX_RADIUS * 0.9f, Scale.X_HEX_RADIUS * 0.6f, Scale.X_HEX_RADIUS * 0.9f,
				Scale.FLOOR_DOWN * -0.4f, Scale.FLOOR_DOWN * -1f, Scale.FLOOR_DOWN * -1.6f, false, true, 12);
		possibleActionMatObj = material(new ColorRGBA(1f, 0f, 0.4f, 0.7f));
		activeObjMat = material(new ColorRGBA(1f, 0.4f, 0.4f, 0.8f));
		activeOtherMat = material(new ColorRGBA(1f, 0.4f, 0f, 0.7f));
	}

	private static void directionArrow()
	{
		directionArrowMesh = new YArrow(Scale.FLOOR_DOWN * -0.5f, Scale.FLOOR_DOWN * -0.7f,
				Scale.X_HEX_RADIUS * 1.1f, Scale.X_HEX_RADIUS * 1.5f, Scale.X_HEX_RADIUS * 1.7f,
				Scale.X_HEX_RADIUS * 0.05f, Scale.X_HEX_RADIUS * 0.15f);
		directionArrowMat = material(new ColorRGBA(65f / 255f, 40f / 255f, 25f / 255f, 1f));
		activeArrowMat = material(new ColorRGBA(1f, 1f, 0f, 1f));
	}

	private static void object()
	{
		objectMesh = new YSolidPrismX(Scale.X_HEX_RADIUS * 0.5f, 0f, Scale.CELLAR_HEIGHT / 4f, 3);
		objectMat = new Material[3];
		objectMat[0] = material(new ColorRGBA(0.6f, 0.6f, 0.6f, 1f));
		objectMat[1] = material(new ColorRGBA(0.8f, 0f, 0f, 1f));
		objectMat[2] = material(new ColorRGBA(0f, 0.3f, 0.6f, 1f));
		pathLocMat = material(new ColorRGBA(1f, 0.4f, 0f, 0.7f));
	}

	public static Geometry wallGeom(String n, int direction, WallType wallType, boolean low)
	{
		Mesh mesh = readyMesh("Wall_" + direction + "_" + wallType + "_" + (low ? "L" : "H"),
				() -> new Wall(direction, wallType, low));
		Material material = readyMaterial("Wall",
				() -> material(new ColorRGBA(65f / 255f, 40f / 255f, 25f / 255f, 1f)));
		return geometry(n, mesh, material);
	}

	public static Mesh readyMesh(String codec, Supplier<Mesh> supplier)
	{
		if(meshes.containsKey(codec))
			return meshes.get(codec);
		else
		{
			Mesh mesh = supplier.get();
			meshes.put(codec, mesh);
			return mesh;
		}
	}

	public static Material readyMaterial(String codec, Supplier<Material> supplier)
	{
		if(materials.containsKey(codec))
			return materials.get(codec);
		else
		{
			Material material = supplier.get();
			materials.put(codec, material);
			return material;
		}
	}

	public static Geometry geometry(String n, Mesh mesh, Material material)
	{
		Geometry geometry = new Geometry(n, mesh);
		geometry.setMaterial(material);
		return geometry;
	}
}