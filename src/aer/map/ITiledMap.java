package aer.map;

import aer.commands.*;
import aer.locate.*;
import aer.map.mapgen.*;
import aer.map.maptiles.*;
import aer.relocatable.*;
import aer.save.*;
import java.util.*;

public interface ITiledMap extends ICommandLink
{
	void setGenerator(IHexMapGen generator);

	boolean checkBounds(HexLocation loc);

	MapTile getTile(HexLocation loc);

	boolean tileGenerated(HexLocation loc);

	void setTile(MapTile tile);

	int[] getBounds();

	List<Relocatable> objectsAt(HexLocation loc);

	Relocatable objectByID(Identifier id);

	Relocatable objectBySID(String id);

	Relocatable[] determineMountSlots(Identifier id, int len);

	void addObject(Relocatable object);

	List<Relocatable> team(int teamID);

	List<Relocatable> allObjects();

	Map<HexLocation, MapTile> updatedTiles();

	void restore(InMapSave inMapSave);
}