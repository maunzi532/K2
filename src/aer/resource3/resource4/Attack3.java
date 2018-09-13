package aer.resource3.resource4;

import aer.*;
import aer.path.*;
import aer.path.team.*;
import aer.resource2.items.item3.*;
import aer.resource2.therathicType.*;
import java.util.*;

public class Attack3 extends BaseTargeting3
{
	private AttackType attackType;
	private StatItem item;
	private Therathic user;
	private int distance;

	public Attack3(CostTable costs, HexLocation start, HexDirection from, Relocatable target, AttackType attackType,
			StatItem item, Therathic user, int distance)
	{
		super(costs, start, from, target, attackType.noTurningRequired());
		this.attackType = attackType;
		this.item = item;
		this.user = user;
		this.distance = distance;
	}

	@Override
	public int cost()
	{
		return super.cost() + attackType.cost();
	}

	@Override
	public int mCost()
	{
		return super.mCost() + attackType.mCost();
	}

	@Override
	public List<Pather> targets(Pather xec0, Therathic xec1)
	{
		if(target instanceof Pather && ((Pather) target).getTherathic() instanceof CBA &&
				((CBA) ((Pather) target).getTherathic()).canBeAttacked())
			return Collections.singletonList((Pather) target);
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<Reaction> targetOptions(Therathic xec1, Therathic target1, E_AP_MP target2)
	{
		return ((CBA) target1).reactions(user, item, attackType, distance);
	}

	@Override
	public boolean executeOn(Therathic xec1, E_AP_MP xec2, Therathic target1, E_AP_MP target2, Reaction chosen)
	{
		if(!target2.useAP(chosen.cost, E_AP_MP.Use.REAL))
		{
			chosen = targetOptions(xec1, target1, target2).get(0);
		}
		CBA xec3 = (CBA) xec1;
		CBA target3 = (CBA) target1;
		switch(chosen.code)
		{
			case 0: //Nothing
				target3.takeAttack(xec1, item, attackType, distance, false, false, null);
				break;
			case 1: //Retaliate
				target3.takeAttack(xec1, item, attackType, distance, false, false, null);
				xec3.takeAttack(target1, (StatItem) chosen.data[0], (AttackType) chosen.data[1], distance, true, false, null);
				break;
			case 2: //Dodge
				target3.takeAttack(xec1, item, attackType, distance, false, true, null);
				break;
			case 3: //Block
				target3.takeAttack(xec1, item, attackType, distance, false, false, (StatItem) chosen.data[2]);
				break;
		}
		return false;
	}
}