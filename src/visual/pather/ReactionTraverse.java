package visual.pather;

import aer.path.pather.*;
import aer.path.schedule.*;
import aer.path.takeable.*;
import aer.relocatable.*;
import aer.resource2.resource.*;
import java.util.*;
import java.util.stream.*;
import visual.hud.*;
import visual.map.*;

public class ReactionTraverse
{
	public final TargetData targetData;
	private final List<Pather> canInterrupt;
	public final boolean playerChoosesReaction;
	private final Targeting targeting;
	private final VisHUD visHUD;
	private final List<Reaction> reactionOptions;
	public final List<Pather> reallyCanInterrupt;
	public Pather interrupter;
	public int cursor;
	public int limit;
	public Reaction chosen0;
	public TakeableAction chosen1;
	public boolean skip0;
	public boolean visualUpdateRequired = false;

	public ReactionTraverse(TargetData targetData, List<Pather> canInterrupt, boolean playerChoosesReaction,
			Targeting targeting, VisHUD visHUD)
	{
		this.targetData = targetData;
		this.canInterrupt = canInterrupt;
		this.playerChoosesReaction = playerChoosesReaction;
		this.targeting = targeting;
		this.visHUD = visHUD;
		reallyCanInterrupt = canInterrupt.stream().filter(e -> !e.getPossibleInterrupts(targetData).isEmpty()).collect(Collectors.toList());
		if(playerChoosesReaction)
		{
			reactionOptions = targetData.reactionOptions();
			limit = reactionOptions.size();
		}
		else
			reactionOptions = null;
		visualUpdateRequired = true;
		showChoiceOptions();
	}

	public void exec()
	{
		visualUpdateRequired = false;
		if(targeting.checkInput() == null)
			return;
		switch(targeting.checkInput())
		{
			case TARGET:
				Relocatable t0 = targeting.targetObject();
				if(t0 instanceof Pather && t0 != interrupter && canInterrupt.contains(t0))
				{
					interrupter = (Pather) t0;
					cursor = 0;
					limit = interrupter.getPossibleInterrupts(targetData).size();
					showChoiceOptions();
				}
				break;
			case CHOOSE:
			case ACCEPT:
				if(interrupter != null)
				{
					if(cursor < limit)
					{
						chosen1 = interrupter.getPossibleInterrupts(targetData).get(cursor).action;
						visHUD.changeMode(0);
					}
				}
				else if(playerChoosesReaction)
				{
					if(cursor < limit && (cursor == 0 || reactionOptions.get(cursor).available))
					{
						chosen0 = reactionOptions.get(cursor);
						visHUD.changeMode(0);
					}
				}
				else if(targeting.checkInput() == Input1.ACCEPT)
				{
					skip0 = true;
					visHUD.changeMode(0);
				}
				break;
			case CHANGE:
				cursor++;
				if(cursor >= limit)
					cursor = 0;
				showChoiceOptions();
				break;
			case BACK:
			case ESCAPE:
				if(interrupter != null)
				{
					interrupter = null;
					cursor = 0;
					if(playerChoosesReaction)
						limit = reactionOptions.size();
					else
						limit = 0;
					showChoiceOptions();
				}
				break;
		}
	}

	private void showChoiceOptions()
	{
		if(interrupter != null)
		{
			if(cursor < limit && interrupter.getPossibleInterrupts(targetData).get(cursor).deducted instanceof Resource_AP_MP)
			{
				PathAction action = interrupter.getPossibleInterrupts(targetData).get(cursor);
				visHUD.updateText(1, "AP", String.valueOf(((Resource_AP_MP) action.deducted).dActionPoints()));
				visHUD.updateText(1, "MP", String.valueOf(((Resource_AP_MP) action.deducted).dMovementPoints()));
				visHUD.updateText(1, "Path", ""/*steps(currentAction)*/);
				visHUD.updateText(1, "Take", "Choose Interrupt");
			}
			else
			{
				visHUD.updateText(1, "AP", "");
				visHUD.updateText(1, "MP", "");
				visHUD.updateText(1, "Path", "");
				visHUD.updateText(1, "Take", "No available Interrupts");
			}
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < interrupter.getPossibleInterrupts(targetData).size(); i++)
				sb.append(i == cursor ? "> " : "| ").append(interrupter.getPossibleInterrupts(targetData).get(i).getClass().getSimpleName()).append("\n");
			visHUD.updateText(1, "Options", sb.toString());

		}
		else if(playerChoosesReaction)
		{
			if(cursor < limit)
			{
				Reaction reaction = reactionOptions.get(cursor);
				visHUD.updateText(1, "AP", "Cost: " + reaction.cost);
				visHUD.updateText(1, "MP", reaction.available ? "" : "Unavailable");
				visHUD.updateText(1, "Path", "");
				visHUD.updateText(1, "Take", "Choose Reaction");
			}
			else
			{
				visHUD.updateText(1, "AP", "");
				visHUD.updateText(1, "MP", "");
				visHUD.updateText(1, "Path", "");
				visHUD.updateText(1, "Take", "Error");
			}
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < reactionOptions.size(); i++)
				sb.append(i == cursor ? "> " : "| ").append(reactionOptions.get(i).text).append("\n");
			visHUD.updateText(1, "Options", sb.toString());
		}
		else
		{
			visHUD.updateText(1, "AP", "");
			visHUD.updateText(1, "MP", "");
			visHUD.updateText(1, "Path", "");
			visHUD.updateText(1, "Take", "Reaction will be chosen by NPC");
			visHUD.updateText(1, "Options", "");
		}
		visHUD.updateText(1, "Target", "");
		visHUD.changeMode(1);
	}
}