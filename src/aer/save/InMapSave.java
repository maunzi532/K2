package aer.save;

import aer.*;
import aer.path.*;
import java.io.*;
import java.util.*;

public class InMapSave implements Serializable
{
	public final List<Relocatable> relocatables;
	public final Map<HexLocation, MapTile> updatedTiles;
	public final int inActionLNum;
	public final TurnPhase mainPhase;

	public InMapSave(TurnSchedule turnSchedule)
	{
		relocatables = turnSchedule.map.allObjects();
		updatedTiles = turnSchedule.map.updatedTiles();
		inActionLNum = turnSchedule.inActionLNum;
		mainPhase = turnSchedule.mainPhase;
	}
}