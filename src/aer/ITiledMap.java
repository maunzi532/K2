package aer;

import aer.mapgen.*;
import java.util.*;
import visual.*;

public interface ITiledMap extends ICommandLink
{
	void setGenerator(IHexMapGen generator);

	boolean checkBounds(HexLocation loc);

	MapTile getTile(HexLocation loc);

	void setTile(MapTile tile);

	int[] getBounds();

	List<Relocatable> objectsAt(HexLocation loc);

	Relocatable objectByID(Identifier id);

	Relocatable objectBySID(String id);

	void addObject(Relocatable object);

	List<Relocatable> team(int teamID);
}