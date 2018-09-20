package aer.summoner;

import aer.*;
import aer.commands.*;
import aer.path.*;

public class EntryToSummon extends SummonerEntry implements ICommand
{
	public final Relocatable toSummon;

	public EntryToSummon(int trigger, int offset, int team, int timing, Relocatable toSummon)
	{
		super(trigger, offset, team, timing);
		this.toSummon = toSummon;
	}

	@Override
	public void execute(ITiledMap map, TurnSchedule turnSchedule, TurnSummoner turnSummoner)
	{
		map.addObject(toSummon);
		toSummon.map = map;
		turnSummoner.addCommand(this);
		turnSchedule.log(1, toSummon.name() + " summoned");
	}
}