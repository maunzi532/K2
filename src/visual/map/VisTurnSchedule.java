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
					System.out.println("WUGU" + pathTraverse.currentAction.action.getClass().getSimpleName());
				}
			}
		}
		else
		{
			PathAction pathAction = pathTraverse.exec(targeting.checkInput(), targeting.targetTile(), targeting.targetObject());
			if(pathAction != null)
				System.out.println("END");
			if(pathTraverse.esc || pathAction != null)
				pathTraverse = null;
		}
		targeting.reset();
	}

	@Override
	public void execute(VisualCommand command)
	{

	}
}