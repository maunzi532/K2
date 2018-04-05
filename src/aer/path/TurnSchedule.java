package aer.path;

import aer.*;
import aer.path.takeable.*;
import java.util.*;
import java.util.stream.*;
import visual.*;

public class TurnSchedule extends CommandLink
{
	public List<Integer> controlledTeams;
	public int inActionLNum;
	public IHexMap map;

	public TurnPhase phase;
	public List<HexPather> currentTeam;

	public List<HexPather> currentControlled;
	public int npcControlled;

	public HexPather currentActionPather;
	public List<TakeableAction> actions;
	public int currentAction;

	public List<HexPather> targets;
	public int currentTarget;

	public List<Reaction> reactions;
	public List<HexPather> interrupt;
	public boolean playerInterrupt;
	public List<HexPather> currentInterrupt;
	public int npcInterrupt;

	public int playerControl;

	public TurnSchedule(List<Integer> controlledTeams, int inActionLNum, IHexMap map)
	{
		if(inActionLNum < 0 || inActionLNum >= controlledTeams.size())
			throw new RuntimeException();
		this.controlledTeams = controlledTeams;
		this.inActionLNum = inActionLNum;
		this.map = map;
		initDrawPhase();
	}

	public void stepForward()
	{
		if(interrupt != null)
		{
			if(playerInterrupt)
			{
				if(playerControl == 2)
					nextNPCReaction();
				else
					importReaction(reactions.get(0));
			}
			else
			{
				nextNPCInterrupt();
			}
		}
		else if(targets != null)
		{
			nextTarget();
		}
		else if(actions != null)
		{
			nextAction();
		}
		else
			switch(phase)
			{
				case DRAW:
					nextDraw();
					break;
				case PLAYERACTION:
					initExecPhase(false);
					break;
				case ALLYACTION:
					nextNPCPath();
					break;
				case END:
					nextEnd();
					break;
			}
		System.out.println(toString());
	}

	private void initDrawPhase()
	{
		currentTeam = new ArrayList<>();
		map.team(controlledTeams.get(inActionLNum)).forEach(e -> currentTeam.add((HexPather) e));
		currentControlled = currentTeam;
		npcControlled = 0;
		playerControl = 0;
		phase = TurnPhase.DRAW;
	}

	private void nextDraw()
	{
		if(npcControlled >= currentControlled.size())
		{
			initExecPhase(true);
		}
		else
		{
			currentControlled.get(npcControlled).getTherathicHex().drawPhase();
			npcControlled++;
		}
	}

	private void initExecPhase(boolean player)
	{
		currentControlled = currentTeam.stream().filter(e -> e.getTherathicHex().playerControlled() == player).collect(Collectors.toList());
		resetPaths();
		npcControlled = 0;
		phase = player ? TurnPhase.PLAYERACTION : TurnPhase.ALLYACTION;
		playerControl = player ? 1 : 0;
	}

	private void nextNPCPath()
	{
		if(npcControlled >= currentControlled.size())
		{
			initEndPhase();
		}
		else
		{
			HexPather xec1 = currentControlled.get(npcControlled);
			PathAction path = xec1.getTherathicHex().npcControl().path(xec1);
			if(path != null)
				importPath(path);
			else
				npcControlled++;
		}
	}

	private void initEndPhase()
	{
		currentControlled = currentTeam;
		resetPaths();
		//currentControlled.get(0).setLoc(new HexLocation(3, 4, 0, 0));
		npcControlled = 0;
		playerControl = 0;
		phase = TurnPhase.END;
	}

	private void nextEnd()
	{
		if(npcControlled >= currentControlled.size())
		{
			inActionLNum++;
			if(inActionLNum >= controlledTeams.size())
				inActionLNum = 0;
			initDrawPhase();
		}
		else
		{
			PathAction pathActionE = currentControlled.get(npcControlled).getTherathicHex().endPhase();
			if(pathActionE != null)
				importPath(pathActionE);
			else
				npcControlled++;
		}
	}

	public void importPath(PathAction pathAction)
	{
		if(pathAction.previous == null)
		{
			actions = new ArrayList<>();
			currentActionPather = pathAction.pather;
			currentAction = 0;
			playerControl = 0;
		}
		else
			importPath(pathAction.previous);
		actions.add(pathAction.action);
	}

	private void nextAction()
	{
		if(currentAction >= actions.size())
			endActionPath();
		else
		{
			TakeableAction action1 = actions.get(currentAction);
			if(action1.executeStart(currentActionPather))
				endActionPath();
			else
			{
				targets = action1.targets(currentActionPather);
				currentTarget = 0;
				nextTarget();
			}
		}
	}

	private void endActionPath()
	{
		actions = null;
		targets = null;
		reactions = null;
		interrupt = null;
		playerControl = phase == TurnPhase.PLAYERACTION ? 1 : 0;
		resetPaths();
		if(playerControl == 0)
			npcControlled++;
	}

	private void resetPaths()
	{
		currentControlled.forEach(HexPather::resetPossiblePaths);
	}

	private void nextTarget()
	{
		TakeableAction action1 = actions.get(currentAction);
		if(currentTarget >= targets.size())
		{
			if(action1.executeEnd(currentActionPather))
				endActionPath();
			else
			{
				targets = null;
				currentAction++;
			}
		}
		else
		{
			reactions = action1.targetOptions(currentActionPather, targets.get(currentTarget));
			interrupt = new ArrayList<>();
			for(Integer tn : action1.interruptTeamNumbers(currentActionPather, targets.get(currentTarget)))
				map.team(tn).forEach(e -> interrupt.add((HexPather) e));
			initInterrupt(false);
		}
	}

	private void initInterrupt(boolean player)
	{
		currentInterrupt = interrupt.stream().filter(e -> e.getTherathicHex().playerControlled() == player).collect(Collectors.toList());
		npcInterrupt = 0;
		playerInterrupt = player;
		if(player)
			playerControl = targets.get(currentTarget).getTherathicHex().playerControlled() ? 3 : 2;
		else
			playerControl = 0;
		if(playerControl == 2 && currentInterrupt.isEmpty())
			nextNPCReaction();
	}

	private void nextNPCInterrupt()
	{
		if(npcInterrupt >= currentInterrupt.size())
		{
			initInterrupt(true);
		}
		else
		{
			HexPather xec1 = currentControlled.get(npcControlled);
			TakeableAction action1 = xec1.getTherathicHex().npcControl().interrupt(xec1, actions.get(currentAction),
					currentActionPather, targets.get(currentTarget));
			if(action1 != null)
				importInterrupt(xec1, action1);
			npcInterrupt++;
		}
	}

	public void importInterrupt(HexPather xec, TakeableAction action1)
	{
		action1.executeEnd(xec);
	}

	private void nextNPCReaction()
	{
		HexPather xec1 = currentControlled.get(npcControlled);
		Reaction reaction1 = xec1.getTherathicHex().npcControl().reaction(xec1, actions.get(currentAction),
				currentActionPather, reactions);
		importReaction(reaction1);
	}

	public void importReaction(Reaction reaction)
	{
		reactions = null;
		interrupt = null;
		if(actions.get(currentAction).executeOn(currentActionPather, targets.get(currentTarget), reaction))
			endActionPath();
		else
		{
			playerControl = 0;
			currentTarget++;
		}
	}

	@Override
	public String name()
	{
		return "TurnSchedule";
	}

	@Override
	public String toString()
	{
		return "TurnSchedule{" +
				"controlledTeams=" + controlledTeams +
				", inActionLNum=" + inActionLNum +
				", map=" + map +
				", phase=" + phase +
				", currentTeam=" + currentTeam +
				", currentControlled=" + currentControlled +
				", npcControlled=" + npcControlled +
				", currentActionPather=" + currentActionPather +
				", actions=" + actions +
				", currentAction=" + currentAction +
				", targets=" + targets +
				", currentTarget=" + currentTarget +
				", reactions=" + reactions +
				", interrupt=" + interrupt +
				", playerInterrupt=" + playerInterrupt +
				", currentInterrupt=" + currentInterrupt +
				", npcInterrupt=" + npcInterrupt +
				", playerControl=" + playerControl +
				'}';
	}
}