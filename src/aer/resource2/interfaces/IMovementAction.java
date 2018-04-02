package aer.resource2.interfaces;

import aer.*;
import java.util.*;

public interface IMovementAction extends IAirStateAction
{
	default HexLocation movesTo()
	{
		return null;
	}

	default List<HexLocation> freelyMovingN()
	{
		return null;
	}
}