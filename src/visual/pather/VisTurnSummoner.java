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
			visFinder.attachAndRegister(((EntryToSummon) command).toSummon);
		}
	}
}