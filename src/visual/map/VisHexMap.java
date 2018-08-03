package visual.map;

import aer.*;
import aer.commands.*;
import com.jme3.math.*;
import com.jme3.renderer.queue.*;
import com.jme3.scene.*;
import visual.*;
import visual.mesh.*;
import visual.pather.*;

public class VisHexMap extends VisualR<IHexMap>
{
	public int rLayer;
	private Node lightLocations;
	private Node lightDirections;
	private Node lightObjects;

	public VisHexMap(IHexMap linked, int rLayer)
	{
		super(linked);
		this.rLayer = rLayer;
	}

	@Override
	public void setSpatial(Spatial spatial)
	{
		super.setSpatial(spatial);
		lightLocations = new Node("lightLocations");
		node.attachChild(lightLocations);
		lightDirections = new Node("lightDirections");
		node.attachChild(lightDirections);
		lightObjects = new Node("lightObjects");
		node.attachChild(lightObjects);
		int[] bounds = linked.getBounds();
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
								Geometry geom = new Geometry(loc.toString(), MeshLager.floorMesh);
								geom.setMaterial(MeshLager.floorMat);
								node1.attachChild(geom);
								break;
							}
							case BLOCKED:
							{
								Geometry geom = new Geometry(loc.toString(), MeshLager.blockedMesh);
								geom.setMaterial(MeshLager.blockedMat);
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

	public void lightThese(PathTraverse pathTraverse)
	{
		lightLocations.detachAllChildren();
		lightDirections.detachAllChildren();
		lightObjects.detachAllChildren();
		for(HexLocation loc : pathTraverse.locations().keySet())
		{
			Node node1 = new Node();
			Geometry geom1 = new Geometry(loc.toString(), MeshLager.possibleActionMeshLoc);
			geom1.setMaterial(MeshLager.possibleActionMatLoc);
			geom1.setQueueBucket(RenderQueue.Bucket.Transparent);
			node1.attachChild(geom1);
			node1.setLocalTranslation(conv(loc));
			lightLocations.attachChild(node1);
		}
		for(HexDirection dir : pathTraverse.directions().keySet())
		{
			/*Node node1 = new Node();
			Geometry geom1 = new Geometry(loc.toString(), Lager1.possibleActionMeshLoc);
			geom1.setMaterial(Lager1.possibleActionMatLoc);
			geom1.setQueueBucket(RenderQueue.Bucket.Transparent);
			node1.attachChild(geom1);
			node1.setLocalTranslation(conv(loc));
			lightDirections.attachChild(node1);*/
		}
		for(HexObject obj : pathTraverse.objects().keySet())
		{
			Node node1 = new Node();
			Geometry geom1 = new Geometry(obj.name(), MeshLager.possibleActionMeshObj);
			geom1.setMaterial(MeshLager.possibleActionMatObj);
			geom1.setQueueBucket(RenderQueue.Bucket.Transparent);
			node1.attachChild(geom1);
			node1.setLocalTranslation(conv(obj.getLoc()));
			lightObjects.attachChild(node1);
		}
		if(pathTraverse.others().size() > 0)
		{
			Node node1 = new Node();
			Geometry geom1 = new Geometry(pathTraverse.pather.name(), MeshLager.possibleActionMeshObj);
			geom1.setMaterial(MeshLager.possibleActionMatOth);
			geom1.setQueueBucket(RenderQueue.Bucket.Transparent);
			node1.attachChild(geom1);
			node1.setLocalTranslation(conv(pathTraverse.pather.getLoc()));
			lightObjects.attachChild(node1);
		}
	}

	public void endLighting()
	{
		lightLocations.detachAllChildren();
		lightDirections.detachAllChildren();
		lightObjects.detachAllChildren();
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