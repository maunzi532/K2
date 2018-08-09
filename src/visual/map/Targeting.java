package visual.map;

import aer.*;

public interface Targeting
{
	HexLocation targetTile();

	Relocatable targetObject();

	Input1 checkInput();

	boolean updated();

	void reset();
}