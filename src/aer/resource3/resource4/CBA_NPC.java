package aer.resource3.resource4;

import aer.path.pather.*;
import aer.path.schedule.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.resource2.therathicType.*;

public abstract class CBA_NPC implements NPC_Control
{
	@Override
	public PathAction path(Pather xec)
	{
		return path(xec, (CBA) xec.getTherathic(), (E_AP_MP) xec.getTherathic(), xec.getTherathic());
	}

	public abstract PathAction path(Pather pather, CBA cba, E_AP_MP e_ap_mp, Therathic therathic);

	@Override
	public TakeableAction interrupt(Pather xec, TargetData targetData)
	{
		return interrupt(xec, (CBA) xec.getTherathic(), (E_AP_MP) xec.getTherathic(), xec.getTherathic(), targetData);
	}

	public abstract TakeableAction interrupt(Pather pather, CBA cba, E_AP_MP e_ap_mp, Therathic therathic, TargetData targetData);

	@Override
	public Reaction reaction(TargetData targetData)
	{
		return reaction(targetData.target, (CBA) targetData.target.getTherathic(),
				(E_AP_MP) targetData.target.getTherathic(), targetData.target.getTherathic(), targetData);
	}

	public abstract Reaction reaction(Pather pather, CBA cba, E_AP_MP e_ap_mp, Therathic therathic, TargetData targetData);
}