package visual.map;

import aer.*;
import aer.commands.*;
import aer.path.*;
import visual.*;
import visual.pather.*;

public class VisTurnSchedule extends AbstractVis<TurnSchedule>
{
	public Targeting targeting;
	public PathTraverse pathTraverse;
	public VisHUD visHUD;
	public VisTiledMap visTiledMap;
	public ReactionChooser reactionChooser;

	public VisTurnSchedule(TurnSchedule linked, Targeting targeting, VisHUD visHUD, VisTiledMap visTiledMap)
	{
		super(linked);
		this.targeting = targeting;
		this.visHUD = visHUD;
		this.visTiledMap = visTiledMap;
	}

	@Override
	protected void controlUpdate(float tpf)
	{
		super.controlUpdate(tpf);
		if(targeting.updated())
		{
			if(targeting.targetTile() != null)
			{
				visTiledMap.markCurrentTargetLocation(targeting.targetTile(), targeting.targetObject() == null);
			}
			else
				visTiledMap.markCurrentTargetLocation(null, false);
		}
		switch(linked.playerControl)
		{
			case 1:
				traverse1();
				break;
			case 2:
			case 3:
				traverse2();
				break;
			default:
				if(targeting.checkInput() == Input1.ACCEPT)
					linked.stepForward(true);
				if(targeting.checkInput() == Input1.CHOOSE)
					linked.stepForward(false);
		}
		targeting.reset();
	}

	private void traverse1()
	{
		if(pathTraverse == null)
		{
			if(targeting.checkInput() == Input1.TARGET)
			{
				Relocatable object = targeting.targetObject();
				if(object != null && object instanceof Pather)
				{
					Pather pather = (Pather) object;
					if(pather.getPossiblePaths() == null)
						pather.calculateActionPaths();
					pathTraverse = new PathTraverse(pather.getPossiblePaths(), pather, true, visHUD);
					visTiledMap.lightThese(pathTraverse);
				}
			}
			else if(targeting.checkInput() == Input1.ACCEPT)
				linked.stepForward(true);
			else if(targeting.checkInput() == Input1.CHOOSE)
				linked.stepForward(false);
		}
		else
		{
			PathAction pathAction = pathTraverse.exec(targeting.checkInput(), targeting.targetTile(), targeting.targetObject(), false);
			if(pathAction != null)
			{
				linked.importPath(pathAction);
				linked.stepForward(true);
			}
			if(pathTraverse.esc || pathAction != null)
			{
				visTiledMap.endLighting();
				visHUD.changeMode(0);
				pathTraverse = null;
			}
			else if(pathTraverse.visualUpdateRequired)
			{
				visTiledMap.lightThese(pathTraverse);
			}
		}
	}

	private void traverse2()
	{
		if(pathTraverse == null)
		{
			if(targeting.checkInput() == Input1.TARGET)
			{
				Relocatable object = targeting.targetObject();
				if(object != null && object instanceof Pather)
				{
					Pather pather = (Pather) object;
					if(pather.getPossiblePaths() == null)
						pather.calculateInterrupts(linked.targetData);
					pathTraverse = new PathTraverse(pather.getPossiblePaths(), pather, false, visHUD);
					node.getParent().getChild("Map").getControl(VisTiledMap.class).lightThese(pathTraverse);
				}
			}
			else if(linked.playerControl == 3)
			{
				if(reactionChooser == null)
				{
					reactionChooser = new ReactionChooser(linked.reactions);
					reactionChooser.showChoiceOptions();
				}
				Reaction reaction = reactionChooser.exec(targeting.checkInput());
				if(reaction != null)
				{
					reactionChooser = null;
					linked.importReaction(reaction);
					linked.stepForward(false);
				}
			}
			else if(targeting.checkInput() == Input1.ACCEPT)
				linked.stepForward(true);
			else if(targeting.checkInput() == Input1.CHOOSE)
				linked.stepForward(false);
		}
		else
		{
			PathAction pathAction = pathTraverse.exec(targeting.checkInput(), targeting.targetTile(), targeting.targetObject(), true);
			if(pathAction != null)
			{
				linked.importInterrupt(pathAction.pather, pathAction.action);
				linked.stepForward(false);
			}
			if(pathTraverse.esc || pathAction != null)
			{
				visHUD.changeMode(0);
				pathTraverse = null;
				if(reactionChooser == null)
					reactionChooser = new ReactionChooser(linked.reactions);
				reactionChooser.showChoiceOptions();
			}
		}
	}

	public void stepToPlayerPhase()
	{
		linked.stepForward(true);
	}

	@Override
	public void execute(ICommand command)
	{

	}
}