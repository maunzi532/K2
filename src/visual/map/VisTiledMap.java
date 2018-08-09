package visual.map;

import aer.*;
import aer.commands.*;
import aer.resource2.resource.*;
import com.jme3.math.*;
import com.jme3.renderer.queue.*;
import com.jme3.scene.*;
import visual.*;
import visual.mesh.*;
import visual.pather.*;

public class VisTiledMap extends AbstractVis<ITiledMap>
{
	public int rLayer;
	private Node lightLocations;
	private Node lightDirections;
	private Node lightObjects;

	public VisTiledMap(ITiledMap linked, int rLayer)
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
		Resource_AP_MP currentState = (Resource_AP_MP) pathTraverse.currentAction.deducted;
		for(HexLocation loc : pathTraverse.locations().keySet())
		{
			Node node1 = new Node();
			Geometry geom1 = new Geometry(loc.toString(), MeshLager.possibleActionMeshLoc);
			if(loc.equals(pathTraverse.loc))
				geom1.setMaterial(MeshLager.activeLocMat);
			else
				geom1.setMaterial(MeshLager.possibleActionMatLoc);
			geom1.setQueueBucket(RenderQueue.Bucket.Transparent);
			node1.attachChild(geom1);
			node1.setLocalTranslation(conv(loc));
			lightLocations.attachChild(node1);
		}
		for(HexDirection dir : pathTraverse.directions().keySet())
		{
			Node node1 = new Node();
			Geometry geom1 = new Geometry(dir.toString(), MeshLager.directionArrowMesh);
			if(pathTraverse.turn != null && HexDirection.plus(currentState.dDirection(), pathTraverse.turn).equals(dir))
				geom1.setMaterial(MeshLager.activeArrowMat);
			else
				geom1.setMaterial(MeshLager.directionArrowMat);
			geom1.setQueueBucket(RenderQueue.Bucket.Transparent);
			geom1.setLocalRotation(conv(dir));
			node1.attachChild(geom1);
			node1.setLocalTranslation(conv(((Resource_AP_MP) pathTraverse.currentAction.deducted).dLocation()));
			lightDirections.attachChild(node1);
		}
		for(Relocatable obj : pathTraverse.objects().keySet())
		{
			Node node1 = new Node();
			Geometry geom1 = new Geometry(obj.name(), MeshLager.possibleActionMeshObj);
			if(obj.equals(pathTraverse.object))
				geom1.setMaterial(MeshLager.activeObjMat);
			else
				geom1.setMaterial(MeshLager.possibleActionMatObj);
			geom1.setQueueBucket(RenderQueue.Bucket.Transparent);
			node1.attachChild(geom1);
			node1.setLocalTranslation(conv(obj.getLoc()));
			lightObjects.attachChild(node1);
		}
		if(!currentState.dLocation().equals(pathTraverse.pather.getLoc())
				|| !currentState.dDirection().equals(pathTraverse.pather.getDirection()))
		{
			Node node1 = new Node();
			Geometry geom1 = new Geometry("Copy", MeshLager.objectMesh);
			geom1.setMaterial(MeshLager.pathLocMat);
			geom1.setQueueBucket(RenderQueue.Bucket.Transparent);
			geom1.setLocalRotation(conv(currentState.dDirection()));
			node1.attachChild(geom1);
			node1.setLocalTranslation(conv(currentState.dLocation()));
			lightObjects.attachChild(node1);
		}
		if(pathTraverse.others().size() > 0)
		{
			Node node1 = new Node();
			Geometry geom1 = new Geometry("Other", MeshLager.possibleActionMeshObj);
			geom1.setMaterial(MeshLager.activeOtherMat);
			geom1.setQueueBucket(RenderQueue.Bucket.Transparent);
			node1.attachChild(geom1);
			node1.setLocalTranslation(conv(currentState.dLocation()));
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
	public void execute(ICommand command)
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