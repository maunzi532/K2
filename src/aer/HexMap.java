package aer;

import aer.mapgen.*;
import aer.path.*;
import java.util.*;
import java.util.stream.*;
import visual.*;

public class HexMap extends CommandLink implements IHexMap
{
	private String name;

	private int startX, startD, startH, startR;
	private int endX, endD, endH, endR;

	private final MapTile[][][][] tiles;
	private final HashMap<HexLocation, MapTile> updateds;

	private IHexGen generator;

	private final ArrayList<HexObject> objects;

	public HexMap(String name, int startX, int startD, int startH, int startR, int endX, int endD, int endH, int endR)
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
		updateds = new HashMap<>();
		objects = new ArrayList<>();
	}

	@Override
	public void setGenerator(IHexGen generator)
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
		MapTile updated = updateds.get(new HexLocation(tX, tD, tH, tR));
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
			updateds.remove(loc1);
		else
			updateds.put(loc1, tile);
	}

	@Override
	public int[] getBounds()
	{
		return new int[]{startX, startD, startH, startR, endX, endD, endH, endR};
	}

	@Override
	public List<HexObject> objectsAt(HexLocation loc)
	{
		return objects.stream().filter(e -> loc.equals(e.getLoc())).collect(Collectors.toList());
	}

	@Override
	public HexObject objectByID(int id)
	{
		return objects.stream().filter(e -> e.id == id).findFirst().orElse(null);
	}

	@Override
	public void addObject(HexObject object)
	{
		objects.add(object);
	}

	@Override
	public List<HexObject> team(int teamID)
	{
		return objects.stream().filter(e -> e instanceof HexPather && ((HexPather) e).getTherathicHex().teamSide() == teamID).collect(Collectors.toList());
	}

	@Override
	public String name()
	{
		return name;
	}
}