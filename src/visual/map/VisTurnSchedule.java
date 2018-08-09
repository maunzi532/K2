package visual.map;

import aer.*;
import aer.commands.*;
import aer.path.*;
import aer.path.team.*;
import visual.*;
import visual.pather.*;

public class VisTurnSchedule extends VisualR<TurnSchedule>
{
	public Targeting targeting;
	public PathTraverse pathTraverse;
	public VisHUD visHUD;
	public ReactionCh reactionCh;

	public VisTurnSchedule(TurnSchedule linked, Targeting targeting, VisHUD visHUD)
	{
		super(linked);
		this.targeting = targeting;
		this.visHUD = visHUD;
	}

	@Override
	protected void controlUpdate(float tpf)
	{
		super.controlUpdate(tpf);
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
				HexObject object = targeting.targetObject();
				if(object != null && object instanceof HexPather)
				{
					HexPather pather = (HexPather) object;
					if(pather.getPossiblePaths() == null)
						pather.calculatePossiblePaths(ItemGetType.ACTION, null);
					pathTraverse = new PathTraverse(pather.getPossiblePaths(), pather, true, visHUD);
					node.getParent().getChild("Map").getControl(VisHexMap.class).lightThese(pathTraverse);
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
				node.getParent().getChild("Map").getControl(VisHexMap.class).endLighting();
				visHUD.changeMode(HUDMode.NONE);
				pathTraverse = null;
			}
			else if(pathTraverse.visualUpdateRequired)
			{
				node.getParent().getChild("Map").getControl(VisHexMap.class).lightThese(pathTraverse);
			}
		}
	}

	private void traverse2()
	{
		if(pathTraverse == null)
		{
			if(targeting.checkInput() == Input1.TARGET)
			{
				HexObject object = targeting.targetObject();
				if(object != null && object instanceof HexPather)
				{
					HexPather pather = (HexPather) object;
					if(pather.getPossiblePaths() == null)
						pather.calculatePossiblePaths(ItemGetType.INTERRUPT, linked.targetData);
					pathTraverse = new PathTraverse(pather.getPossiblePaths(), pather, false, visHUD);
					node.getParent().getChild("Map").getControl(VisHexMap.class).lightThese(pathTraverse);
				}
			}
			else if(linked.playerControl == 3)
			{
				if(reactionCh == null)
				{
					reactionCh = new ReactionCh(linked.reactions);
					reactionCh.showChoiceOptions();
				}
				Reaction reaction = reactionCh.exec(targeting.checkInput());
				if(reaction != null)
				{
					reactionCh = null;
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
				visHUD.changeMode(HUDMode.NONE);
				pathTraverse = null;
				if(reactionCh == null)
					reactionCh = new ReactionCh(linked.reactions);
				reactionCh.showChoiceOptions();
			}
		}
	}

	public void stepToPlayerPhase()
	{
		linked.stepForward(true);
	}

	@Override
	public void execute(VisualCommand command)
	{

	}
}