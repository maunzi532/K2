package aer.save;

import aer.locate.*;
import aer.map.maptiles.*;
import aer.path.schedule.*;
import aer.relocatable.*;
import java.io.*;
import java.util.*;

public class InMapSave implements Serializable
{
	public final List<Relocatable> relocatables;
	public final Map<HexLocation, MapTile> updatedTiles;
	public final int turnCounter;
	public final int inActionLNum;
	public final TurnPhase mainPhase;

	public InMapSave(TurnSchedule turnSchedule)
	{
		relocatables = turnSchedule.map.allObjects();
		updatedTiles = turnSchedule.map.updatedTiles();
		turnCounter = turnSchedule.turnCounter;
		inActionLNum = turnSchedule.inActionLNum;
		mainPhase = turnSchedule.mainPhase;
	}
}