package aer.path;

import aer.*;
import aer.path.takeable.*;
import aer.path.team.*;
import java.util.*;
import java.util.stream.*;
import visual.*;

public class TurnSchedule extends CommandLink
{
	public List<Integer> controlledTeams;
	public int inActionLNum;
	public IHexMap map;

	public TurnPhase mainPhase;
	public TurnPhase innerPhase;
	public boolean initFlag;
	public List<HexPather> currentTeam;

	public List<HexPather> playerControlled;
	public Iterator<HexPather> npcControlled;
	public HexPather currentControlled;

	public Iterator<TakeableAction> actions;
	public TakeableAction currentAction;

	public Iterator<HexPather> targets;
	public TargetData targetData;

	public List<Reaction> reactions;
	public List<HexPather> interrupt;
	public List<HexPather> playerInterrupt;
	public Iterator<HexPather> npcInterrupt;
	public HexPather currentInterrupt;

	public int playerControl;

	public TurnSchedule(List<Integer> controlledTeams, int startingTeamCode, IHexMap map)
	{
		this.controlledTeams = controlledTeams;
		inActionLNum = controlledTeams.indexOf(startingTeamCode);
		if(inActionLNum < 0)
			throw new RuntimeException();
		this.map = map;
		setPhase(TurnPhase.DRAW);
		initFlag = true;
	}

	public void log(int level, String message)
	{
		System.out.println(message);
	}

	public void stepForward()
	{
		//noinspection StatementWithEmptyBody
		while(stepForwardNoSkip());
	}

	private boolean stepForwardNoSkip()
	{
		switch(innerPhase)
		{
			case DRAW:
				if(initFlag)
				{
					log(2, "Initiating draw phase for team " + controlledTeams.get(inActionLNum));
					currentTeam = new ArrayList<>();
					map.team(controlledTeams.get(inActionLNum)).forEach(e -> currentTeam.add((HexPather) e));
					npcControlled = currentTeam.iterator();
					initFlag = false;
					return true;
				}
				if(!npcControlled.hasNext())
				{
					log(0, "Ending draw phase");
					setPhase(TurnPhase.PLAYERACTION);
					return true;
				}
				else
				{
					currentControlled = npcControlled.next();
					log(2, currentControlled.name() + " executes draw phase");
					return currentControlled.getTherathicHex().drawPhase();
				}
			case PLAYERACTION:
				if(initFlag)
				{
					log(3, "Ready for player control");
					playerControlled = currentTeam.stream().filter(e -> e.getTherathicHex().playerControlled()).collect(Collectors.toList());
					playerControlled.forEach(HexPather::resetPossiblePaths);
					playerControl = 1;
					initFlag = false;
					return false;
				}
				else
				{
					log(0, "Ending player phase");
					playerControl = 0;
					setPhase(TurnPhase.ALLYACTION);
					return true;
				}
			case ALLYACTION:
				if(initFlag)
				{
					log(1, "Initiating ally phase");
					npcControlled = currentTeam.stream().filter(e -> !e.getTherathicHex().playerControlled()).iterator();
					initFlag = false;
					return true;
				}
				if(!npcControlled.hasNext())
				{
					log(0, "Ending ally phase");
					setPhase(TurnPhase.END);
					return true;
				}
				else
				{
					currentControlled = npcControlled.next();
					currentControlled.calculatePossiblePaths(TherathicHex.ItemGetType.ACTION, null);
					PathAction path = currentControlled.getTherathicHex().npcControl().path(currentControlled);
					if(path != null)
						importPath(path);
					else
						log(2, currentControlled.name() + " skipped action phase");
					return path != null;
				}
			case END:
				if(initFlag)
				{
					log(1, "Initiating end phase");
					npcControlled = currentTeam.iterator();
					initFlag = false;
					return true;
				}
				if(!npcControlled.hasNext())
				{
					log(0, "Ending end phase");
					inActionLNum++;
					if(inActionLNum >= controlledTeams.size())
						inActionLNum = 0;
					setPhase(TurnPhase.DRAW);
					return false;
				}
				else
				{
					currentControlled = npcControlled.next();
					currentControlled.calculatePossiblePaths(TherathicHex.ItemGetType.END, null);
					PathAction path = currentControlled.getTherathicHex().endPhase();
					if(path != null)
						importPath(path);
					else
						log(0, currentControlled.name() + " skipped end phase");
					return true;
				}
			case EXEC:
				initFlag = false;
				if(!actions.hasNext())
					return endActionPath();
				else
				{
					currentAction = actions.next();
					if(currentAction.executeStart(currentControlled))
						return endActionPath();
					else
					{
						setPhase(TurnPhase.TARGET);
						return true;
					}
				}
			case TARGET:
				if(initFlag)
				{
					targets = currentAction.targets(currentControlled).iterator();
					initFlag = false;
					return true;
				}
				if(!targets.hasNext())
				{
					if(currentAction.executeEnd(currentControlled))
						return endActionPath();
					innerPhase = TurnPhase.EXEC;
					return false;
				}
				else
				{
					targetData = new TargetData(currentControlled, currentAction, targets.next());
					setPhase(TurnPhase.ALLYINTERRUPT);
					return true;
				}
			case ALLYINTERRUPT:
				if(initFlag)
				{
					interrupt = targetData.canInterrupt(map);
					npcInterrupt = interrupt.stream().filter(e -> !e.getTherathicHex().playerControlled()).iterator();
					initFlag = false;
					return true;
				}
				if(!npcInterrupt.hasNext())
				{
					setPhase(TurnPhase.PLAYERINTERRUPT);
					return true;
				}
				else
				{
					currentInterrupt = npcInterrupt.next();
					currentInterrupt.calculatePossiblePaths(TherathicHex.ItemGetType.INTERRUPT, targetData);
					TakeableAction intA = currentInterrupt.getTherathicHex().npcControl().interrupt(currentInterrupt, targetData);
					if(intA != null)
						importInterrupt(currentInterrupt, intA);
					else
						log(0, currentInterrupt.name() + " skipped interrupt phase");
					return intA != null;
				}
			case PLAYERINTERRUPT:
				if(initFlag)
				{
					log(3, "Ready for player interrupts");
					playerInterrupt = interrupt.stream().filter(e -> e.getTherathicHex().playerControlled()).collect(Collectors.toList());
					playerInterrupt.forEach(HexPather::resetPossiblePaths);
					if(targetData.target.getTherathicHex().playerControlled())
					{
						playerControl = 3;
						reactions = targetData.reactionOptions();
					}
					else
						playerControl = 2;
					initFlag = false;
					return false;
				}
				else
				{
					log(0, "Ending player interrupt phase");
					if(playerControl == 3)
						importReaction(reactions.get(0));
					else
						importReaction(targetData.target.getTherathicHex().npcControl().reaction(targetData));
					return true;
				}
		}
		return false;
	}

	private void setPhase(TurnPhase phase)
	{
		innerPhase = phase;
		if(phase.mainPhase)
			mainPhase = phase;
		initFlag = true;
	}

	public void importPath(PathAction pathAction)
	{
		playerControl = 0;
		List<TakeableAction> actions1 = new ArrayList<>();
		importPath1(pathAction, actions1);
		actions = actions1.iterator();
		currentControlled = pathAction.pather;
		setPhase(TurnPhase.EXEC);
	}

	private void importPath1(PathAction pathAction, List<TakeableAction> actions1)
	{
		if(pathAction.previous != null)
			importPath1(pathAction.previous, actions1);
		actions1.add(pathAction.action);
	}

	private boolean endActionPath()
	{
		innerPhase = mainPhase;
		if(mainPhase == TurnPhase.PLAYERACTION)
		{
			initFlag = true;
			return true;
		}
		return false;
	}

	public void importInterrupt(HexPather xec, TakeableAction action1)
	{
		action1.executeEnd(xec);
		if(innerPhase == TurnPhase.PLAYERINTERRUPT)
			initFlag = true;
	}

	public void importReaction(Reaction reaction)
	{
		playerControl = 0;
		if(targetData.exec(reaction))
			endActionPath();
		else
			innerPhase = TurnPhase.TARGET;
	}

	@Override
	public String name()
	{
		return "TurnSchedule";
	}
}