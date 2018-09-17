package aer;

import aer.commands.*;
import aer.mapgen.*;
import aer.path.*;
import aer.save.*;
import java.util.*;
import java.util.stream.*;

public class HexTiledMap extends CommandLink implements ITiledMap
{
	private String name;

	private int startX, startD, startH, startR;
	private int endX, endD, endH, endR;

	private final MapTile[][][][] tiles;
	private Map<HexLocation, MapTile> updatedTiles;

	private IHexMapGen generator;

	private List<Relocatable> objects;

	public HexTiledMap(String name, int startX, int startD, int startH, int startR, int endX, int endD, int endH, int endR)
	{
		this.name = name;
		this.startX = startX;
		this.startD = startD;
		this.startH = startH;
		this.startR = startR;
		this.endX = endX;
		this.endD = endD;
		this.endH = endH;
		this.endR = endR;
		int wX = endX - startX;
		int wD = endD - startD;
		int wH = endH - startH;
		int wR = endR - startR;
		tiles = new MapTile[wX][wD][wH][wR];
		updatedTiles = new HashMap<>();
		objects = new ArrayList<>();
	}

	@Override
	public void setGenerator(IHexMapGen generator)
	{
		this.generator = generator;
		generator.init(this);
	}

	@Override
	public boolean checkBounds(HexLocation loc)
	{
		return loc.x >= startX && loc.d >= startD && loc.h >= startH && loc.r >= startR && loc.x < endX && loc.d < endD && loc.h < endH && loc.r < endR;
	}

	@Override
	public MapTile getTile(HexLocation loc)
	{
		if(!checkBounds(loc))
			return new MapTile(this, loc);
		int tX = loc.x - startX;
		int tD = loc.d - startD;
		int tH = loc.h - startH;
		int tR = loc.r - startR;
		MapTile updated = updatedTiles.get(new HexLocation(tX, tD, tH, tR));
		if(updated != null)
			return updated;
		MapTile tile = tiles[tX][tD][tH][tR];
		if(tile == null)
		{
			generator.generate(loc);
			tile = tiles[tX][tD][tH][tR];
		}
		return tile;
	}

	@Override
	public void setTile(MapTile tile)
	{
		assert tile.map == this;
		assert !tile.outofbounds;
		assert checkBounds(tile.loc);
		int tX = tile.loc.x - startX;
		int tD = tile.loc.d - startD;
		int tH = tile.loc.h - startH;
		int tR = tile.loc.r - startR;
		tiles[tX][tD][tH][tR] = tile;
	}

	public void setUpdatedTile(MapTile tile)
	{
		assert tile.map == this;
		assert !tile.outofbounds;
		assert checkBounds(tile.loc);
		int tX = tile.loc.x - startX;
		int tD = tile.loc.d - startD;
		int tH = tile.loc.h - startH;
		int tR = tile.loc.r - startR;
		HexLocation loc1 = new HexLocation(tX, tD, tH, tR);
		if(tiles[tX][tD][tH][tR] == null)
			generator.generate(loc1);
		if(tile.equals(tiles[tX][tD][tH][tR]))
			updatedTiles.remove(loc1);
		else
			updatedTiles.put(loc1, tile);
	}

	@Override
	public int[] getBounds()
	{
		return new int[]{startX, startD, startH, startR, endX, endD, endH, endR};
	}

	@Override
	public List<Relocatable> objectsAt(HexLocation loc)
	{
		return objects.stream().filter(e -> loc.equals(e.getLoc())).collect(Collectors.toList());
	}

	@Override
	public Relocatable objectByID(Identifier id)
	{
		return objects.stream().filter(e -> e.id.equals(id)).findFirst().orElse(null);
	}

	@Override
	public Relocatable objectBySID(String id)
	{
		return objects.stream().filter(e -> e.id.toString().equals(id)).findFirst().orElse(null);
	}

	@Override
	public Relocatable[] determineMountSlots(Identifier id, MountSlotInfo[] info)
	{
		Relocatable[] mountSlots = new Relocatable[info.length];
		objects.stream().filter(e -> id.equals(e.getMountedToID())).forEach(e -> mountSlots[e.getMountedToSlot()] = e);
		return mountSlots;
	}

	@Override
	public void addObject(Relocatable object)
	{
		objects.add(object);
	}

	@Override
	public List<Relocatable> team(int teamID)
	{
		return objects.stream().filter(e -> e instanceof Pather && ((Pather) e).getTherathic().teamSide() == teamID).collect(Collectors.toList());
	}

	@Override
	public List<Relocatable> allObjects()
	{
		return objects;
	}

	@Override
	public Map<HexLocation, MapTile> updatedTiles()
	{
		return updatedTiles;
	}

	@Override
	public void restore(InMapSave inMapSave)
	{
		updatedTiles = inMapSave.updatedTiles;
		objects = inMapSave.relocatables;
		for(Relocatable relocatable : objects)
			relocatable.map = this;
	}

	@Override
	public String name()
	{
		return name;
	}
}