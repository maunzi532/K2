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
	public ReactionCh reactionCh;

	public VisTurnSchedule(TurnSchedule linked, Targeting targeting)
	{
		super(linked);
		this.targeting = targeting;
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
					linked.stepForward();
		}
		targeting.reset();
	}

	private void traverse1()
	{
		if(pathTraverse == null)
		{
			if(targeting.checkInput() == Input1.CHOOSE)
			{
				HexObject object = targeting.targetObject();
				if(object != null && object instanceof HexPather)
				{
					HexPather pather = (HexPather) object;
					if(pather.getPossiblePaths() == null)
						pather.calculatePossiblePaths(TherathicHex.ItemGetType.ACTION, null);
					pathTraverse = new PathTraverse(pather.getPossiblePaths(), pather, true);
					node.getParent().getChild("Map").getControl(VisHexMap.class).lightThese(pathTraverse.locations().keySet());
				}
			}
			else if(targeting.checkInput() == Input1.ACCEPT)
				linked.stepForward();
		}
		else
		{
			PathAction pathAction = pathTraverse.exec(targeting.checkInput(), targeting.targetTile(), targeting.targetObject(), false);
			if(pathAction != null)
			{
				linked.importPath(pathAction);
				linked.stepForward();
			}
			if(pathTraverse.esc || pathAction != null)
				pathTraverse = null;
		}
	}

	private void traverse2()
	{
		if(pathTraverse == null)
		{
			if(targeting.checkInput() == Input1.CHOOSE)
			{
				HexObject object = targeting.targetObject();
				if(object != null && object instanceof HexPather)
				{
					HexPather pather = (HexPather) object;
					if(pather.getPossiblePaths() == null)
						pather.calculatePossiblePaths(TherathicHex.ItemGetType.INTERRUPT, linked.targetData);
					pathTraverse = new PathTraverse(pather.getPossiblePaths(), pather, false);
					node.getParent().getChild("Map").getControl(VisHexMap.class).lightThese(pathTraverse.locations().keySet());
				}
			}
			else if(linked.playerControl == 3)
			{
				if(reactionCh == null)
					reactionCh = new ReactionCh(linked.reactions);
				Reaction reaction = reactionCh.exec(targeting.checkInput());
				if(reaction != null)
				{
					reactionCh = null;
					linked.importReaction(reaction);
					linked.stepForward();
				}
			}
			else if(targeting.checkInput() == Input1.ACCEPT)
				linked.stepForward();
		}
		else
		{
			PathAction pathAction = pathTraverse.exec(targeting.checkInput(), targeting.targetTile(), targeting.targetObject(), true);
			if(pathAction != null)
			{
				linked.importInterrupt(pathAction.pather, pathAction.action);
				linked.stepForward();
			}
			if(pathTraverse.esc || pathAction != null)
				pathTraverse = null;
		}
	}

	@Override
	public void execute(VisualCommand command)
	{

	}
}