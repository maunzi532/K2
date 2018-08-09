package aer.resource2.therathicType;

import aer.path.*;
import aer.path.takeable.*;
import aer.path.team.*;
import java.util.*;

public interface IThAP extends TakeableAction
{
	@Override
	default boolean executeStart(Pather xec)
	{
		if(!(xec.getTherathic() instanceof E_AP_MP))
		{
			System.out.println("Cast Error");
			return true;
		}
		return executeStart(xec, xec.getTherathic(), (E_AP_MP) xec.getTherathic());
	}

	default boolean executeStart(Pather xec0, Therathic xec1, E_AP_MP xec2)
	{
		return false;
	}

	@Override
	default List<Pather> targets(Pather xec)
	{
		return targets(xec, xec.getTherathic());
	}

	default List<Pather> targets(Pather xec0, Therathic xec1)
	{
		return Collections.EMPTY_LIST;
	}

	@Override
	default List<Reaction> targetOptions(Pather xec, Pather target)
	{
		return targetOptions(xec.getTherathic(), target.getTherathic(),
				target.getTherathic() instanceof E_AP_MP ? (E_AP_MP) target.getTherathic() : null);
	}

	default List<Reaction> targetOptions(Therathic xec1, Therathic target1, E_AP_MP target2)
	{
		return null;
	}

	@Override
	default List<Integer> interruptTeamNumbers(Pather xec, Pather target)
	{
		return interruptTeamNumbers(xec.getTherathic(), target.getTherathic());
	}

	default List<Integer> interruptTeamNumbers(Therathic xec1, Therathic target1)
	{
		return null;
	}

	@Override
	default boolean executeOn(Pather xec, Pather target, Reaction chosen)
	{
		return executeOn(xec.getTherathic(), (E_AP_MP) xec.getTherathic(), target.getTherathic(),
				target.getTherathic() instanceof E_AP_MP ? (E_AP_MP) target.getTherathic() : null, chosen);
	}

	default boolean executeOn(Therathic xec1, E_AP_MP xec2, Therathic target1, E_AP_MP target2, Reaction chosen)
	{
		return false;
	}

	@Override
	default boolean executeEnd(Pather xec)
	{
		return executeEnd(xec, xec.getTherathic(), (E_AP_MP) xec.getTherathic());
	}

	default boolean executeEnd(Pather xec0, Therathic xec1, E_AP_MP xec2)
	{
		return false;
	}
}