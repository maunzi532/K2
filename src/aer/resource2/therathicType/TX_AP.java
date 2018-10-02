package aer.resource2.therathicType;

import aer.path.*;
import aer.path.team.*;
import aer.resource2.resource.*;

public abstract class TX_AP implements Therathic, E_AP_MP
{
	protected Pather pather;
	protected int teamSide;
	protected NPC_Control npc_control;
	protected int actionPoints;
	protected int movePoints;
	protected boolean usedFirstPath;
	protected boolean usedFirstMovement;
	protected int reqFall;
	protected boolean mountUsed;

	public TX_AP(int teamSide)
	{
		this.teamSide = teamSide;
	}

	@Override
	public void linkTo(Pather pather)
	{
		this.pather = pather;
	}

	@Override
	public Pather pather()
	{
		return pather;
	}

	@Override
	public ActionResource actionResource()
	{
		return new Resource_AP_MP(actionPoints, movePoints, usedFirstPath ? 1 : 0, usedFirstMovement ? 1 : 0, pather.getDirection(),
				pather.getAirState(), reqFall, pather.getLoc(), pather.getMountedTo(), pather.getMountedToSlot(), mountUsed);
	}

	@Override
	public boolean drawPhase()
	{
		usedFirstPath = false;
		usedFirstMovement = false;
		mountUsed = false;
		return true;
	}

	@Override
	public PathAction endPhase()
	{
		return endPath();
	}

	@Override
	public int teamSide()
	{
		return teamSide;
	}

	@Override
	public NPC_Control npcControl()
	{
		return npc_control;
	}

	@Override
	public void setUsedFirstPath()
	{
		usedFirstPath = true;
	}

	@Override
	public void setUsedFirstMovement()
	{
		usedFirstMovement = true;
	}

	@Override
	public void setMountThisTurnUsed()
	{
		mountUsed = true;
	}

	@Override
	public boolean useAP(int amount, Use use)
	{
		if(use == Use.DRAIN)
		{
			actionPoints = Math.max(actionPoints - amount, 0);
			return true;
		}
		boolean ok = actionPoints >= amount;
		if(ok && use == Use.REAL)
			actionPoints -= amount;
		return ok;
	}

	@Override
	public boolean useMP(int amount, Use use)
	{
		if(use == Use.DRAIN)
		{
			movePoints = Math.max(movePoints - amount, 0);
			return true;
		}
		boolean ok = movePoints >= amount;
		if(ok && use == Use.REAL)
			movePoints -= amount;
		return ok;
	}
}