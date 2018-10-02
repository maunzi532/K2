package aer.resource2.therathicType;

import aer.path.*;
import aer.path.takeable.*;
import java.util.*;

public abstract class TX_CP extends TX_AP implements CostTable
{
	protected List<PatherItem> items = new ArrayList<>();
	protected EndPatherItem endItem;

	public TX_CP(int teamSide)
	{
		super(teamSide);
	}

	@Override
	public List<PatherItem> activeItems()
	{
		return items;
	}

	@Override
	public List<PatherItem> interruptItems(TargetData targetData)
	{
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<EndPatherItem> endItems()
	{
		return Collections.singletonList(endItem);
	}

	@Override
	public TakeableAction startAction()
	{
		return new InitAction(this, usedFirstPath);
	}

	@Override
	public boolean drawPhase()
	{
		super.drawPhase();
		actionPoints = this.startingAP();
		movePoints = this.startingM();
		reqFall = this.requiredFall();
		return true;
	}
}