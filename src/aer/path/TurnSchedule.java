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
	public ITiledMap map;

	public TurnPhase mainPhase;
	public TurnPhase innerPhase;
	public boolean initFlag;
	public List<Pather> currentTeam;

	public List<Pather> playerControlled;
	public Iterator<Pather> npcControlled;
	public Pather currentControlled;

	public Iterator<TakeableAction> actions;
	public TakeableAction currentAction;

	public Iterator<Pather> targets;
	public TargetData targetData;

	public List<Reaction> reactions;
	public List<Pather> interrupt;
	public List<Pather> playerInterrupt;
	public Iterator<Pather> npcInterrupt;
	public Pather currentInterrupt;

	public int playerControl;

	public TurnSchedule(List<Integer> controlledTeams, int startingTeamCode, ITiledMap map)
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

	public void stepForward(boolean skipToPlayerControl)
	{
		//noinspection StatementWithEmptyBody
		while(stepForwardNoSkip(skipToPlayerControl) || skipToPlayerControl && playerControl <= 0);
	}

	private boolean stepForwardNoSkip(boolean endPlayerPhase)
	{
		switch(innerPhase)
		{
			case DRAW:
				if(initFlag)
				{
					log(2, "Initiating draw phase for team " + controlledTeams.get(inActionLNum));
					currentTeam = new ArrayList<>();
					map.team(controlledTeams.get(inActionLNum)).forEach(e -> currentTeam.add((Pather) e));
					npcControlled = currentTeam.iterator();
					initFlag = false;
					return true;
				}
				if(!npcControlled.hasNext())
				{
					log(0, "Ending draw phase");
					setPhase(TurnPhase.ALLYACTION);
					return true;
				}
				else
				{
					currentControlled = npcControlled.next();
					log(2, currentControlled.name() + " executes draw phase");
					return currentControlled.getTherathic().drawPhase();
				}
			case ALLYACTION:
				if(initFlag)
				{
					log(1, "Initiating ally phase");
					npcControlled = currentTeam.stream().filter(e -> !e.getTherathic().playerControlled()).iterator();
					initFlag = false;
					return true;
				}
				if(!npcControlled.hasNext())
				{
					log(0, "Ending ally phase");
					setPhase(TurnPhase.PLAYERACTION);
					return true;
				}
				else
				{
					currentControlled = npcControlled.next();
					currentControlled.calculateActionPaths();
					PathAction path = currentControlled.getTherathic().npcControl().path(currentControlled);
					if(path != null)
						importPath(path);
					else
						log(2, currentControlled.name() + " skipped action phase");
					return path != null;
				}
			case PLAYERACTION:
				if(initFlag)
				{
					log(3, "Ready for player control");
					playerControlled = currentTeam.stream().filter(e -> e.getTherathic().playerControlled()).collect(Collectors.toList());
					playerControlled.forEach(Pather::resetPossiblePaths);
					playerControl = 1;
					initFlag = false;
					return false;
				}
				else
				{
					if(endPlayerPhase)
					{
						log(0, "Ending player phase");
						playerControl = 0;
						setPhase(TurnPhase.END);
						return true;
					}
					return false;
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
					//currentControlled.calculateEndPath();
					PathAction path = currentControlled.getTherathic().endPhase();
					if(path != null)
						importPath(path);
					else
						log(0, currentControlled.name() + " skipped end phase");
					return true;
				}
			case EXEC:
				initFlag = false;
				if(!actions.hasNext())
					return endActionPath(true);
				else
				{
					currentAction = actions.next();
					log(3, "Exec action " + currentAction.getClass().getSimpleName());
					if(currentAction.executeStart(currentControlled))
						return endActionPath(false);
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
						return endActionPath(false);
					innerPhase = TurnPhase.EXEC;
					return false;
				}
				else
				{
					targetData = new TargetData(currentControlled, currentAction, targets.next());
					log(3, "Action " + currentAction.getClass().getSimpleName() + " targeting " + targetData.target.name());
					if(targetData.reactionOptions() != null)
						setPhase(TurnPhase.ALLYINTERRUPT);
					return true;
				}
			case ALLYINTERRUPT:
				if(initFlag)
				{
					interrupt = targetData.canInterrupt(map);
					npcInterrupt = interrupt.stream().filter(e -> !e.getTherathic().playerControlled()).iterator();
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
					currentInterrupt.calculateInterrupts(targetData);
					TakeableAction intA = currentInterrupt.getTherathic().npcControl().interrupt(currentInterrupt, targetData);
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
					playerInterrupt = interrupt.stream().filter(e -> e.getTherathic().playerControlled()).collect(Collectors.toList());
					playerInterrupt.forEach(Pather::resetPossiblePaths);
					if(targetData.target.getTherathic().playerControlled())
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
					if(endPlayerPhase)
					{
						log(0, "Ending player interrupt phase");
						if(playerControl == 3)
							importReaction(reactions.get(0));
						else
							importReaction(targetData.target.getTherathic().npcControl().reaction(targetData));
						return true;
					}
					return false;
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
		log(1, "Importing path for " + pathAction.pather.name());
		playerControl = 0;
		List<TakeableAction> actions1 = new ArrayList<>();
		PathAction.pathToList(pathAction, actions1);
		actions = actions1.iterator();
		currentControlled = pathAction.pather;
		setPhase(TurnPhase.EXEC);
	}

	private boolean endActionPath(boolean finished)
	{
		if(finished)
			log(2, "Exec finished");
		else
			log(3, "Exec ended prematurely");
		innerPhase = mainPhase;
		if(mainPhase == TurnPhase.PLAYERACTION)
		{
			initFlag = true;
			return true;
		}
		return false;
	}

	public void importInterrupt(Pather xec, TakeableAction action1)
	{
		log(3, "Exec interrupt of " + xec.name());
		action1.executeEnd(xec);
		if(innerPhase == TurnPhase.PLAYERINTERRUPT)
			initFlag = true;
	}

	public void importReaction(Reaction reaction)
	{
		log(3, "Chosen reaction: " + reaction.text);
		playerControl = 0;
		if(targetData.exec(reaction))
			endActionPath(false);
		else
			innerPhase = TurnPhase.TARGET;
	}

	@Override
	public String name()
	{
		return "TurnSchedule";
	}
}