package visual;

import com.jme3.asset.*;
import com.jme3.material.*;
import com.jme3.math.*;
import com.jme3.scene.*;
import visual.map.*;
import visual.pather.*;

public class Lager1
{
	public static Mesh floorMesh;
	public static Material floorMat;
	public static Mesh blockedMesh;
	public static Material blockedMat;
	public static Mesh possibleActionMesh;
	public static Material possibleActionMat;
	public static Mesh objectMesh;
	public static Material objectMat;

	public static void init(AssetManager assetManager)
	{
		floorMesh = new YHexoMesh1(Scale.X_HEX_RADIUS, Scale.X_HEX_RADIUS * 0.9f, Scale.FLOOR_DOWN, 0f, 6);
		floorMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		floorMat.setBoolean("UseMaterialColors",true);
		floorMat.setColor("Diffuse", ColorRGBA.Green);
		floorMat.setColor("Ambient", ColorRGBA.Green);
		blockedMesh = new YHexoMesh(Scale.X_HEX_RADIUS, Scale.FLOOR_DOWN, Scale.CELLAR_UP, 6);
		blockedMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		blockedMat.setBoolean("UseMaterialColors",true);
		blockedMat.setColor("Diffuse", ColorRGBA.Brown);
		blockedMat.setColor("Ambient", ColorRGBA.Brown);
		possibleActionMesh = new YHexoMeshT(Scale.X_HEX_RADIUS * 0.9f, Scale.X_HEX_RADIUS * 0.8f,
				Scale.X_HEX_RADIUS * 0.75f, 0f, -Scale.FLOOR_DOWN * 0.6f, 6);
		possibleActionMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		possibleActionMat.setBoolean("UseMaterialColors",true);
		possibleActionMat.setColor("Diffuse", new ColorRGBA(1, 0, 1, 0.4f));
		possibleActionMat.setColor("Ambient", new ColorRGBA(1, 0, 1, 0.4f));
		possibleActionMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		objectMesh = new YHexoMesh(Scale.X_HEX_RADIUS * 0.5f, 0f, Scale.CELLAR_HEIGHT / 4f, 3);
		objectMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		objectMat.setBoolean("UseMaterialColors",true);
		objectMat.setColor("Diffuse", ColorRGBA.Red);
		objectMat.setColor("Ambient", ColorRGBA.Red);
	}
}