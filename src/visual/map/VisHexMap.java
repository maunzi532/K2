package visual.map;

import aer.*;
import aer.commands.*;
import com.jme3.material.*;
import com.jme3.math.*;
import com.jme3.renderer.queue.*;
import com.jme3.scene.*;
import java.util.*;
import visual.*;
import visual.pather.*;

public class VisHexMap extends VisualR<IHexMap>
{
	public int rLayer;

	public VisHexMap(IHexMap linked, int rLayer)
	{
		super(linked);
		this.rLayer = rLayer;
	}

	@Override
	public void setSpatial(Spatial spatial)
	{
		super.setSpatial(spatial);
		int[] bounds = linked.getBounds();
		YHexoMesh hexoMeshFloor = new YHexoMesh(Scale.X_HEX_RADIUS * 0.9f, Scale.FLOOR_DOWN, 0f, 6);
		Material matFloor = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		matFloor.setBoolean("UseMaterialColors",true);
		matFloor.setColor("Diffuse", ColorRGBA.Green);
		matFloor.setColor("Ambient", ColorRGBA.Green);
		YHexoMesh hexoMeshBlock = new YHexoMesh(Scale.X_HEX_RADIUS, Scale.FLOOR_DOWN, Scale.CELLAR_UP, 6);
		Material matBlock = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		matBlock.setBoolean("UseMaterialColors",true);
		matBlock.setColor("Diffuse", ColorRGBA.Brown);
		matBlock.setColor("Ambient", ColorRGBA.Brown);
		if(rLayer >= bounds[3] && rLayer < bounds[7])
			for(int ix = bounds[0]; ix < bounds[4]; ix++)
				for(int id = bounds[1]; id < bounds[5]; id++)
					for(int ih = bounds[2]; ih < bounds[6]; ih++)
					{
						HexLocation loc = new HexLocation(ix, id, ih, rLayer);
						MapTile mapTile = linked.getTile(loc);
						Node node1 = new Node();
						switch(mapTile.type)
						{
							case FLOOR:
							{
								Geometry geom = new Geometry(loc.toString(), hexoMeshFloor);
								geom.setMaterial(matFloor);
								node1.attachChild(geom);
								/*Geometry geom1 = new Geometry(loc.toString(), hexoMeshFloor1);
								geom1.setMaterial(matFloor1);
								geom1.setQueueBucket(RenderQueue.Bucket.Translucent);
								node1.attachChild(geom1);*/
								break;
							}
							case BLOCKED:
							{
								Geometry geom = new Geometry(loc.toString(), hexoMeshBlock);
								geom.setMaterial(matBlock);
								node1.attachChild(geom);
								break;
							}
						}
						if(node1.getQuantity() > 0)
						{
							node1.setUserData("X", ix);
							node1.setUserData("D", id);
							node1.setUserData("H", ih);
							node1.setUserData("R", rLayer);
							node1.setUserData("Target", true);
							node1.setLocalTranslation(conv(new HexLocation(ix, id, ih, rLayer)));
							node.attachChild(node1);
						}
					}
	}

	//broken
	public void lightThese(Collection<HexLocation> locations)
	{
		YHexoMeshT hexoMeshFloor1 = new YHexoMeshT(Scale.X_HEX_RADIUS * 0.9f, Scale.X_HEX_RADIUS * 0.8f,
				Scale.X_HEX_RADIUS * 0.75f, 0f, -Scale.FLOOR_DOWN * 0.6f, 6);
		Material matFloor1 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		matFloor1.setBoolean("UseMaterialColors",true);
		matFloor1.setColor("Diffuse", new ColorRGBA(1, 0, 1, 0.4f));
		matFloor1.setColor("Ambient", new ColorRGBA(1, 0, 1, 0.4f));
		matFloor1.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		if(node.getChild("LightThese") != null)
			((Node) node.getChild("LightThese")).detachAllChildren();
		else
			node.attachChild(new Node("LightThese"));
		Node lightThese = (Node) node.getChild("LightThese");
		for(HexLocation loc : locations)
		{
			//MapTile mapTile = linked.getTile(loc);
			Node node1 = new Node();
			Geometry geom1 = new Geometry(loc.toString(), hexoMeshFloor1);
			geom1.setMaterial(matFloor1);
			geom1.setQueueBucket(RenderQueue.Bucket.Transparent);
			node1.attachChild(geom1);
			node1.setLocalTranslation(conv(loc));
			lightThese.attachChild(node1);
		}
	}

	@Override
	public void execute(VisualCommand command)
	{

	}

	public static Vector3f conv(HexLocation loc)
	{
		return new Vector3f(loc.x * Scale.X_HEX_HALF * 2 + loc.d * Scale.X_HEX_HALF,
				loc.h * Scale.CELLAR_HEIGHT, loc.d * Scale.DZ_HEX_DIFF);
	}

	public static Quaternion conv(HexDirection direction)
	{
		return new Quaternion().fromAngleAxis(FastMath.TWO_PI * direction.r / -12f, Vector3f.UNIT_Y);
	}
}