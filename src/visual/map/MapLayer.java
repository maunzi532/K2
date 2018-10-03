package visual.map;

import aer.*;
import com.jme3.scene.*;
import visual.mesh.*;

public class MapLayer extends Node
{
	private ITiledMap map;
	private int r;
	private int h;
	private int startX, endX;
	private int startD, endD;
	private Node currentLayer;
	private Node lowerLayer;

	public MapLayer(ITiledMap map, int r, int h, int startX, int endX, int startD, int endD)
	{
		super(r + " " + h);
		this.map = map;
		this.r = r;
		this.h = h;
		this.startX = startX;
		this.endX = endX;
		this.startD = startD;
		this.endD = endD;
	}

	public void create()
	{
		currentLayer = new Node("CurrentLayer");
		attachChild(currentLayer);
		lowerLayer = new Node("LowerLayer");
		attachChild(lowerLayer);
		for(int ix = startX; ix < endX; ix++)
			for(int id = startD; id < endD; id++)
			{
				HexLocation loc = new HexLocation(ix, id, h, r);
				MapTile mapTile = map.getTile(loc);
				Node nodeC = new Node();
				Node nodeL = new Node();
				switch(mapTile.type)
				{
					case FLOOR:
					{
						Geometry geomC = new Geometry(loc.toString(), MeshLager.floorMesh);
						geomC.setMaterial(MeshLager.floorMat);
						nodeC.attachChild(geomC);
						Geometry geomL = new Geometry(loc.toString(), MeshLager.floorMesh);
						geomL.setMaterial(MeshLager.floorMat);
						nodeL.attachChild(geomL);
						break;
					}
					case BLOCKED:
					{
						/*Geometry geomC = new Geometry(loc.toString(), MeshLager.blockedMeshV);
						geomC.setMaterial(MeshLager.blockedMat);
						nodeC.attachChild(geomC);
						Geometry geomL = new Geometry(loc.toString(), MeshLager.blockedMesh);
						geomL.setMaterial(MeshLager.blockedMat);
						nodeL.attachChild(geomL);*/
						nodeC.attachChild(MeshLager.wallGeom(loc.toString(), Math.abs(loc.x % 6), WallType.G1, true));
						nodeL.attachChild(MeshLager.wallGeom(loc.toString(), Math.abs(loc.x % 6), WallType.G1, false));
						break;
					}
				}
				if(mapTile.type == MapTileType.FLOOR || mapTile.type == MapTileType.BLOCKED)
				{
					nodeC.setUserData("X", ix);
					nodeC.setUserData("D", id);
					nodeC.setUserData("H", h);
					nodeC.setUserData("R", r);
					nodeC.setUserData("Tile", true);
					nodeC.setLocalTranslation(VisTiledMap.conv(new HexLocation(ix, id, h, r)));
					currentLayer.attachChild(nodeC);
				}
				if(mapTile.type == MapTileType.FLOOR || mapTile.type == MapTileType.BLOCKED)
				{
					nodeL.setUserData("X", ix);
					nodeL.setUserData("D", id);
					nodeL.setUserData("H", h);
					nodeL.setUserData("R", r);
					nodeL.setUserData("Tile", true);
					nodeL.setLocalTranslation(VisTiledMap.conv(new HexLocation(ix, id, h, r)));
					lowerLayer.attachChild(nodeL);
				}
			}
	}

	public void update(int viewH)
	{
		if(viewH == h)
			attachChild(currentLayer);
		else
			detachChild(currentLayer);
		if(viewH > h)
			attachChild(lowerLayer);
		else
			detachChild(lowerLayer);
	}
}