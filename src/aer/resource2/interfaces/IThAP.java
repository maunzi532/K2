package aer.resource2.interfaces;

import aer.path.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.resource2.therathicTypes.*;
import java.util.*;

public interface IThAP extends TakeableAction
{
	@Override
	default boolean executeStart(HexPather xec)
	{
		if(!(xec.getTherathicHex() instanceof E_AP_MP))
		{
			System.out.println("Cast Error");
			return true;
		}
		return executeStart(xec, xec.getTherathicHex(), (E_AP_MP) xec.getTherathicHex());
	}

	default boolean executeStart(HexPather xec0, TherathicHex xec1, E_AP_MP xec2)
	{
		return false;
	}

	@Override
	default List<HexPather> targets(HexPather xec)
	{
		return targets(xec, xec.getTherathicHex());
	}

	default List<HexPather> targets(HexPather xec0, TherathicHex xec1)
	{
		return null;
	}

	@Override
	default List<Reaction> targetOptions(HexPather xec, HexPather target)
	{
		return targetOptions(xec.getTherathicHex(), target.getTherathicHex(),
				target.getTherathicHex() instanceof E_AP_MP ? (E_AP_MP) target.getTherathicHex() : null);
	}

	default List<Reaction> targetOptions(TherathicHex xec1, TherathicHex target1, E_AP_MP target2)
	{
		return null;
	}

	@Override
	default List<Integer> interruptTeamNumbers(HexPather xec, HexPather target)
	{
		return interruptTeamNumbers(xec.getTherathicHex(), target.getTherathicHex());
	}

	default List<Integer> interruptTeamNumbers(TherathicHex xec1, TherathicHex target1)
	{
		return null;
	}

	@Override
	default boolean executeOn(HexPather xec, HexPather target, Reaction chosen)
	{
		return executeOn(xec.getTherathicHex(), (E_AP_MP) xec.getTherathicHex(), target.getTherathicHex(),
				target.getTherathicHex() instanceof E_AP_MP ? (E_AP_MP) target.getTherathicHex() : null, chosen);
	}

	default boolean executeOn(TherathicHex xec1, E_AP_MP xec2, TherathicHex target1, E_AP_MP target2, Reaction chosen)
	{
		return false;
	}

	@Override
	default boolean executeEnd(HexPather xec)
	{
		return executeEnd(xec, xec.getTherathicHex(), (E_AP_MP) xec.getTherathicHex());
	}

	default boolean executeEnd(HexPather xec0, TherathicHex xec1, E_AP_MP xec2)
	{
		return false;
	}
}