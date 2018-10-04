package visual.map;

import aer.commands.*;
import aer.locate.*;
import aer.map.*;
import aer.path.pather.*;
import aer.relocatable.*;
import aer.resource2.resource.*;
import com.jme3.math.*;
import com.jme3.renderer.queue.*;
import com.jme3.scene.*;
import java.util.*;
import visual.*;
import visual.mesh.*;
import visual.pather.*;

public class VisTiledMap extends AbstractVis<ITiledMap>
{
	public int rLayer;
	private Node lightLocations;
	private Node lightDirections;
	private Node lightObjects;
	private Node locationTargeters;
	private Node currentLocationTarget;
	private Node[] rLayers;
	private MapLayer[][] layers;
	private CameraHeightState cameraHeight;

	public VisTiledMap(ITiledMap linked, int rLayer, CameraHeightState cameraHeight)
	{
		super(linked);
		this.rLayer = rLayer;
		this.cameraHeight = cameraHeight;
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
		createLayers();
		createLocationTargeters();
		createCurrentLocationTargeter();
	}

	public void createLayers()
	{
		int[] bounds = linked.getBounds();
		rLayers = new Node[bounds[7] - bounds[3]];
		layers = new MapLayer[bounds[7] - bounds[3]][bounds[6] - bounds[2]];
		for(int ir = bounds[3]; ir < bounds[7]; ir++)
		{
			Node nodeR = new Node(String.valueOf(ir));
			rLayers[ir - bounds[3]] = nodeR;
			node.attachChild(nodeR);
			for(int ih = bounds[2]; ih < bounds[6]; ih++)
			{
				MapLayer layer = new MapLayer(linked, ir, ih, bounds[0], bounds[4], bounds[1], bounds[5]);
				layer.create();
				layer.update(cameraHeight.heightLevel);
				layers[ir - bounds[3]][ih - bounds[2]] = layer;
				nodeR.attachChild(layer);
			}
		}
	}

	public void createLocationTargeters()
	{
		locationTargeters = new Node("locationTargeters");
		node.attachChild(locationTargeters);
		int[] bounds = linked.getBounds();
		for(int ix = bounds[0]; ix < bounds[4]; ix++)
			for(int id = bounds[1]; id < bounds[5]; id++)
			{
				HexLocation loc = new HexLocation(ix, id, 0, rLayer);
				Node node1 = new Node();
				Geometry geom = new Geometry(loc.toString(), MeshLager.locationMesh);
				geom.setCullHint(Spatial.CullHint.Always);
				node1.attachChild(geom);
				node1.setUserData("X", ix);
				node1.setUserData("D", id);
				node1.setUserData("R", rLayer);
				node1.setUserData("Tile", true);
				node1.setLocalTranslation(conv(new HexLocation(ix, id, 0, rLayer)));
				locationTargeters.attachChild(node1);
			}
		locationTargeters.setUserData("H", 0);
	}

	public void createCurrentLocationTargeter()
	{
		currentLocationTarget = new Node("CurrentLocationTarget");
		node.attachChild(currentLocationTarget);
		Geometry geom1 = new Geometry("CurrentLocationTarget", MeshLager.locationMesh);
		geom1.setMaterial(MeshLager.locationMatDisabled);
		geom1.setQueueBucket(RenderQueue.Bucket.Transparent);
		currentLocationTarget.attachChild(geom1);
		currentLocationTarget.setCullHint(Spatial.CullHint.Always);
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

	public void lightThese(ReactionTraverse reactionTraverse)
	{
		lightLocations.detachAllChildren();
		lightDirections.detachAllChildren();
		lightObjects.detachAllChildren();
		for(Pather obj : reactionTraverse.reallyCanInterrupt)
		{
			Node node1 = new Node();
			Geometry geom1 = new Geometry(obj.name(), MeshLager.possibleActionMeshObj);
			if(obj.equals(reactionTraverse.interrupter))
				geom1.setMaterial(MeshLager.activeObjMat);
			else
				geom1.setMaterial(MeshLager.possibleActionMatObj);
			geom1.setQueueBucket(RenderQueue.Bucket.Transparent);
			node1.attachChild(geom1);
			node1.setLocalTranslation(conv(obj.getLoc()));
			lightObjects.attachChild(node1);
		}
		{
			Node node1 = new Node();
			Geometry geom1 = new Geometry("Target", MeshLager.possibleActionMeshObj);
			geom1.setMaterial(MeshLager.activeOtherMat);
			geom1.setQueueBucket(RenderQueue.Bucket.Transparent);
			node1.attachChild(geom1);
			node1.setLocalTranslation(conv(reactionTraverse.targetData.target.getLoc()));
			lightObjects.attachChild(node1);
		}
		{
			Node node1 = new Node();
			Geometry geom1 = new Geometry("Caster", MeshLager.possibleActionMeshObj);
			geom1.setMaterial(MeshLager.activeOtherMat);
			geom1.setQueueBucket(RenderQueue.Bucket.Transparent);
			node1.attachChild(geom1);
			node1.setLocalTranslation(conv(reactionTraverse.targetData.caster.getLoc()));
			lightObjects.attachChild(node1);
		}
	}

	public void endLighting()
	{
		lightLocations.detachAllChildren();
		lightDirections.detachAllChildren();
		lightObjects.detachAllChildren();
	}

	private void updateForCameraHeight()
	{
		if(cameraHeight.heightLevel != (int) locationTargeters.getUserData("H"))
		{
			locationTargeters.setUserData("H", cameraHeight.heightLevel);
			locationTargeters.setLocalTranslation(conv(new HexLocation(0, 0, cameraHeight.heightLevel, 0)));
			int[] bounds = linked.getBounds();
			Arrays.stream(layers[rLayer - bounds[3]]).forEach(e -> e.update(cameraHeight.heightLevel));
		}
	}

	public void markCurrentTargetLocation(HexLocation location, boolean enabled)
	{
		if(location == null)
			currentLocationTarget.setCullHint(Spatial.CullHint.Always);
		else
		{
			currentLocationTarget.setMaterial(enabled ? MeshLager.locationMat : MeshLager.locationMatDisabled);
			currentLocationTarget.setLocalTranslation(conv(location));
			currentLocationTarget.setCullHint(Spatial.CullHint.Inherit);
		}
	}

	@Override
	public void execute(ICommand command)
	{

	}

	@Override
	protected void controlUpdate(float tpf)
	{
		super.controlUpdate(tpf);
		updateForCameraHeight();
	}

	public static Vector3f conv(HexLocation loc)
	{
		return new Vector3f(loc.x * Scale.X_HEX_TANGENT * 2 + loc.d * Scale.X_HEX_TANGENT,
				loc.h * Scale.CELLAR_HEIGHT, loc.d * Scale.DZ_HEX_DIFF);
	}

	public static Quaternion conv(HexDirection direction)
	{
		return new Quaternion().fromAngleAxis(FastMath.TWO_PI * direction.r / -12f, Vector3f.UNIT_Y);
	}
}