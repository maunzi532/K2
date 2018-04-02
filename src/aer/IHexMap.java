package aer;

import java.util.*;
import visual.*;

public interface IHexMap extends VisualLink
{
	void setGenerator(IHexGen generator);

	boolean checkBounds(HexLocation loc);

	MapTile getTile(HexLocation loc);

	void setTile(MapTile tile);

	int[] getBounds();

	List<HexObject> objectsAt(HexLocation loc);

	HexObject objectByID(int id);

	void addObject(HexObject object);
}