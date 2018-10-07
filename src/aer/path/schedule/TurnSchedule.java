package aer.path.schedule;

import aer.commands.*;
import aer.map.*;
import aer.path.pather.*;
import aer.path.takeable.*;
import aer.save.*;
import aer.summoner.*;
import java.util.*;
import java.util.stream.*;

public class TurnSchedule extends CommandLink
{
	public int startingTeamCode;
	public int turnCounter;
	public TurnSummoner turnSummoner;
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

	public TurnSchedule(List<Integer> controlledTeams, int startingTeamCode, ITiledMap map, TurnSummoner turnSummoner)
	{
		this.controlledTeams = controlledTeams;
		this.startingTeamCode = startingTeamCode;
		turnCounter = 0;
		inActionLNum = controlledTeams.indexOf(startingTeamCode);
		if(inActionLNum < 0)
			throw new RuntimeException();
		this.map = map;
		this.turnSummoner = turnSummoner;
		turnSummoner.init(this);
		setPhase(TurnPhase.SUMMON);
		initFlag = true;
	}

	public void log(int level, String message)
	{
		StringBuilder sb = new StringBuilder();
		for(int i = -1; i < level; i++)
		{
			sb.append(' ');
		}
		System.out.println(sb.append(message).toString());
	}

	public void stepForward(int argh, boolean endPlayerPhase)
	{
		while(true)
		{
			if(stepForwardNoSkip(endPlayerPhase) >= argh)
				return;
		}
	}

	private int stepForwardNoSkip(boolean endPlayerPhase)
	{
		switch(innerPhase)
		{
			case SUMMON:
				if(initFlag)
				{
					log(0, "Initiating summon phase, turn " + turnCounter + ", team " + controlledTeams.get(inActionLNum));
					initFlag = false;
					return 1;
				}
				else
				{
					turnSummoner.callEntries(turnCounter, controlledTeams.get(inActionLNum), 0);
					log(0, "Ending summon phase");
					setPhase(TurnPhase.DRAW);
					return 1;
				}
			case DRAW:
				if(initFlag)
				{
					int currentTeamCode = controlledTeams.get(inActionLNum);
					log(0, "Initiating draw phase for team " + currentTeamCode);
					currentTeam = new ArrayList(map.team(currentTeamCode));
					npcControlled = currentTeam.iterator();
					initFlag = false;
					return 0;
				}
				if(!npcControlled.hasNext())
				{
					log(0, "Ending draw phase");
					setPhase(TurnPhase.ALLYACTION);
					return 0;
				}
				else
				{
					currentControlled = npcControlled.next();
					log(1, currentControlled.name() + " executes draw phase");
					return currentControlled.getTherathic().drawPhase() ? 1 : 4;
				}
			case ALLYACTION:
				if(initFlag)
				{
					log(0, "Initiating ally phase");
					npcControlled = currentTeam.stream().filter(e -> !e.getTherathic().playerControlled()).iterator();
					initFlag = false;
					return 0;
				}
				if(!npcControlled.hasNext())
				{
					log(0, "Ending ally phase");
					setPhase(TurnPhase.PLAYERACTION);
					return 0;
				}
				else
				{
					currentControlled = npcControlled.next();
					currentControlled.calculateActionPaths();
					PathAction path = currentControlled.getTherathic().npcControl().path(currentControlled);
					if(path != null)
						importPath(path);
					else
						log(1, currentControlled.name() + " skipped action phase");
					return path != null ? 2 : 1;
				}
			case PLAYERACTION:
				if(initFlag)
				{
					log(0, "Ready for player control");
					playerControlled = currentTeam.stream().filter(e -> e.getTherathic().playerControlled()).collect(Collectors.toList());
					playerControlled.forEach(Pather::resetPossiblePaths);
					playerControl = 1;
					initFlag = false;
					return 5;
				}
				else
				{
					if(endPlayerPhase)
					{
						log(0, "Ending player phase");
						playerControl = 0;
						setPhase(TurnPhase.END);
						return 0;
					}
					return 5;
				}
			case END:
				if(initFlag)
				{
					log(0, "Initiating end phase");
					npcControlled = currentTeam.iterator();
					initFlag = false;
					return 0;
				}
				if(!npcControlled.hasNext())
				{
					log(0, "Ending end phase");
					inActionLNum++;
					if(inActionLNum >= controlledTeams.size())
						inActionLNum = 0;
					if(controlledTeams.get(inActionLNum) == startingTeamCode)
						turnCounter++;
					setPhase(TurnPhase.SUMMON);
					return 0;
				}
				else
				{
					currentControlled = npcControlled.next();
					//currentControlled.calculateEndPath();
					PathAction path = currentControlled.getTherathic().endPhase();
					if(path != null)
						importPath(path);
					else
						log(1, currentControlled.name() + " skipped end phase");
					return path != null ? 2 : 1;
				}
			case EXEC:
				initFlag = false;
				if(!actions.hasNext())
					return endActionPath(true);
				else
				{
					currentAction = actions.next();
					log(2, "Exec action " + currentAction.getClass().getSimpleName());
					if(currentAction.executeStart(currentControlled))
						return endActionPath(false);
					else
					{
						setPhase(TurnPhase.TARGET);
						return 0;
					}
				}
			case TARGET:
				if(initFlag)
				{
					targets = currentAction.targets(currentControlled).iterator();
					initFlag = false;
					return 0;
				}
				if(!targets.hasNext())
				{
					if(currentAction.executeEnd(currentControlled))
						return endActionPath(false);
					innerPhase = TurnPhase.EXEC;
					return 2;
				}
				else
				{
					targetData = new TargetData(currentControlled, currentAction, targets.next());
					log(3, "Action " + currentAction.getClass().getSimpleName() + " targeting " + targetData.target.name());
					if(targetData.reactionOptions() != null)
						setPhase(TurnPhase.ALLYINTERRUPT);
					return 0;
				}
			case ALLYINTERRUPT:
				if(initFlag)
				{
					interrupt = targetData.canInterrupt(map);
					npcInterrupt = interrupt.stream().filter(e -> !e.getTherathic().playerControlled()).iterator();
					initFlag = false;
					return 0;
				}
				if(!npcInterrupt.hasNext())
				{
					setPhase(TurnPhase.PLAYERINTERRUPT);
					return 0;
				}
				else
				{
					currentInterrupt = npcInterrupt.next();
					currentInterrupt.calculateInterrupts(targetData);
					TakeableAction intA = currentInterrupt.getTherathic().npcControl().interrupt(currentInterrupt, targetData);
					if(intA != null)
						importInterrupt(currentInterrupt, intA);
					else
						log(4, currentInterrupt.name() + " skipped interrupt phase");
					return intA != null ? 2 : 1;
				}
			case PLAYERINTERRUPT:
				if(initFlag)
				{
					log(3, "Ready for player interrupts");
					playerInterrupt = interrupt.stream().filter(e -> e.getTherathic().playerControlled()).collect(Collectors.toList());
					playerInterrupt.forEach(e -> e.calculateInterrupts(targetData));
					if(targetData.target.getTherathic().playerControlled())
					{
						playerControl = 3;
						reactions = targetData.reactionOptions();
					}
					else
					{
						if(playerInterrupt.isEmpty())
						{
							log(4, "Player interrupt phase skipped");
							importReaction(targetData.target.getTherathic().npcControl().reaction(targetData));
							return 0;
						}
						else
							playerControl = 2;
					}
					initFlag = false;
					return 5;
				}
				else
				{
					if(endPlayerPhase)
					{
						log(3, "Ending player interrupt phase");
						if(playerControl == 3)
							importReaction(reactions.get(0));
						else
							importReaction(targetData.target.getTherathic().npcControl().reaction(targetData));
						return 0;
					}
					return 5;
				}
		}
		throw new RuntimeException();
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

	private int endActionPath(boolean finished)
	{
		if(finished)
			log(1, "Exec finished");
		else
			log(1, "Exec ended prematurely");
		innerPhase = mainPhase;
		if(mainPhase == TurnPhase.PLAYERACTION)
		{
			initFlag = true;
			return 0;
		}
		return 2;
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
		{
			innerPhase = TurnPhase.TARGET;
			initFlag = false;
		}
	}

	public void restore(InMapSave inMapSave)
	{
		map.restore(inMapSave);
		turnCounter = inMapSave.turnCounter;
		inActionLNum = inMapSave.inActionLNum;
		setPhase(inMapSave.mainPhase);
		currentTeam = new ArrayList(map.team(controlledTeams.get(inActionLNum)));
		stepForward(5, true);
	}

	@Override
	public String name()
	{
		return "TurnSchedule";
	}
}