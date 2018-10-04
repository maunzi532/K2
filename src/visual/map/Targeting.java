package visual.map;

import aer.locate.*;
import aer.relocatable.*;

public interface Targeting
{
	HexLocation targetTile();

	Relocatable targetObject();

	Input1 checkInput();

	boolean updated();

	void reset();
}