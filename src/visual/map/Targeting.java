package visual.map;

import aer.*;

public interface Targeting
{
	HexLocation targetTile();

	HexObject targetObject();

	Input1 checkInput();

	boolean updated();

	void reset();
}