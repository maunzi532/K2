package aer.summoner;

import aer.commands.*;
import aer.map.*;
import aer.path.schedule.*;
import java.util.*;

public class TurnSummoner extends CommandLink
{
	public ITiledMap map;
	public TurnSchedule turnSchedule;
	public List<SummonerEntry> entries;
	public int[] triggers;

	public TurnSummoner()
	{
		triggers = new int[1];
		entries = new ArrayList<>();
	}

	public void init(TurnSchedule turnSchedule)
	{
		this.turnSchedule = turnSchedule;
		map = turnSchedule.map;
	}

	public void callEntries(int turn, int team, int phase)
	{
		for(SummonerEntry entry : entries)
		{
			if(triggers[entry.trigger] >= 0 && turn - triggers[entry.trigger] == entry.offset && team == entry.team && phase == entry.phase)
				entry.execute(map, turnSchedule, this);
		}
	}

	@Override
	public String name()
	{
		return "TurnSummoner";
	}
}