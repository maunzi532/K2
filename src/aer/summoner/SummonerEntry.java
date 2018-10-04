package aer.summoner;

import aer.map.*;
import aer.path.schedule.*;

public abstract class SummonerEntry
{
	public final int trigger;
	public final int offset;
	public final int team;
	public final int phase;

	public SummonerEntry(int trigger, int offset, int team, int phase)
	{
		this.trigger = trigger;
		this.offset = offset;
		this.team = team;
		this.phase = phase;
	}

	public abstract void execute(ITiledMap map, TurnSchedule turnSchedule, TurnSummoner turnSummoner);
}