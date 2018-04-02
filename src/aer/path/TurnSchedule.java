package aer.path;

import aer.*;
import aer.path.takeable.*;
import java.util.*;
import visual.*;

public class TurnSchedule extends CommandLink
{
	public List<Integer> controlledTeams;
	public int inActionLNum;
	public List<HexPather> currentTeam;
	public HexPather currentActionPather;
	public List<TakeableAction> actions;
	public int currentAction;
	public List<HexPather> targets;
	public int currentTarget;
	public List<HexPather> interrupt;
	public boolean playerInterrupt;
	public TurnPhase phase;
	public IHexMap map;

	public void importPath(PathAction pathAction)
	{
		if(pathAction.previous == null)
		{
			actions = new ArrayList<>();
			currentActionPather = pathAction.pather;
		}
		else
			importPath(pathAction.previous);
		actions.add(pathAction.action);
		currentAction = 0;
	}

	public void executeNextAction()
	{
		/*if(actions.size() >= currentAction)
		{
			endExec();
			return;
		}
		TakeableAction action1 = actions.get(currentAction);
		if(currentTarget < 0)
		{
			if(action1.executeStart())
			{
				endExec();
				return;
			}
			targets = action1.targets();
			currentTarget = 0;
		}
		if(currentTarget >= targets.size())
		{
			if(action1.executeEnd())
			{
				endExec();
				return;
			}
			currentAction++;
		}
		else
		{
			//Start Interrupt Phase
			//currentTarget++
		}*/
	}

	public void endExec()
	{
		currentActionPather = null;
		actions = null;
		currentAction = -1;
		targets = null;
		currentTarget = -1;
		interrupt = null;
		playerInterrupt = false;
	}

	@Override
	public String name()
	{
		return "TurnSchedule";
	}
}