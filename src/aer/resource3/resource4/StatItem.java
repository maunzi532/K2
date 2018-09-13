package aer.resource3.resource4;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.resource2.resource.*;
import aer.resource3.*;
import java.util.*;

public interface StatItem extends PatherItem
{
	String name();

	StatItemType type();

	default List<AttackType> attackTypes()
	{
		return Collections.EMPTY_LIST;
	}

	default boolean canBlock()
	{
		return false;
	}

	default int blockCost()
	{
		return 0;
	}

	@Override
	default List<TakeableAction> takeableActions(PathAction pathAction)
	{
		List<TakeableAction> list = new ArrayList<>();
		R_Relocatable res1 = (R_Relocatable) pathAction.deducted;
		R_AP_MP res2 = (R_AP_MP) pathAction.deducted;
		Pather pather = pathAction.pather;
		ITiledMap map = pather.map;
		Therathic therathic = pather.getTherathic();
		CostTable costTable = ((TX_AP_Transform) therathic).currentTransform();
		if(!res1.dEnd())
		{
			for(AttackType attackType : attackTypes())
				if(res2.dActionPoints() >= attackType.cost() && res2.dMovementPoints() >= attackType.mCost())
					for(HexLocation l0 : res1.dLocation().rangeLoc(attackType.minRange(), attackType.maxRange()))
						for(Relocatable m1 : map.objectsAt(l0))
							list.add(new Attack3(costTable, pather.getLoc(), pather.getDirection(), m1, attackType,
									this, therathic, HexLocation.xdzDifference(pather.getLoc(), l0)));
		}
		return list;
	}
}