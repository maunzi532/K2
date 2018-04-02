package visual.map;

import aer.*;
import aer.path.*;
import aer.path.team.*;
import visual.*;
import visual.pather.*;

public class VisTurnSchedule extends VisualR<TurnSchedule>
{
	public Targeting targeting;
	public PathTraverse pathTraverse;

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
				break;
			case 3:
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
				System.out.println(object);
				if(object != null && object instanceof HexPather)
				{
					HexPather pather = (HexPather) object;
					if(pather.getPossiblePaths() == null)
						pather.calculatePossiblePaths(TherathicHex.ItemGetType.ACTION, null);
					pathTraverse = new PathTraverse(pather.getPossiblePaths());
					node.getParent().getChild("Map").getControl(VisHexMap.class).lightThese(pathTraverse.locations().keySet());
					System.out.println("WUGU" + pathTraverse.currentAction.action.getClass().getSimpleName());
				}
			}
			else if(targeting.checkInput() == Input1.ACCEPT)
				linked.stepForward();
		}
		else
		{
			PathAction pathAction = pathTraverse.exec(targeting.checkInput(), targeting.targetTile(), targeting.targetObject());
			if(pathAction != null)
			{
				System.out.println("END");
				linked.importPath(pathAction);
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