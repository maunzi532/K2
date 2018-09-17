package visual.pather;

import aer.commands.*;
import aer.summoner.*;
import visual.*;

public class VisTurnSummoner extends AbstractVis<TurnSummoner>
{
	public VisFinder visFinder;

	public VisTurnSummoner(TurnSummoner linked, VisFinder visFinder)
	{
		super(linked);
		this.visFinder = visFinder;
	}

	@Override
	public void execute(ICommand command)
	{
		if(command instanceof EntryToSummon)
		{
			EntryToSummon entry = (EntryToSummon) command;
			linked.map.addObject(entry.toSummon);
			visFinder.attachAndRegister(entry.toSummon);
		}
	}
}