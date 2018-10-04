package aer.path.pather;

import aer.locate.*;
import aer.path.schedule.*;
import aer.path.team.*;
import aer.relocatable.*;
import java.util.*;

public class Pather extends Relocatable
{
	private Therathic therathic;
	private List<PathAction> possiblePaths;

	public Pather(Identifier id, HexLocation loc, HexDirection direction, AirState airState, Therathic therathic)
	{
		super(id, loc, direction, airState, therathic.mountSlotInfo());
		this.therathic = therathic;
		therathic.linkTo(this);
	}

	public Therathic getTherathic()
	{
		return therathic;
	}

	public List<PathAction> getPossiblePaths()
	{
		return possiblePaths;
	}

	public void resetPossiblePaths()
	{
		possiblePaths = null;
	}

	public void calculateActionPaths()
	{
		possiblePaths = therathic.possibleActivePaths();
	}

	public void calculateInterrupts(TargetData targetData)
	{
		possiblePaths = therathic.possibleInterrupts(targetData);
	}

	public void calculateEndPath()
	{
		possiblePaths = Collections.singletonList(therathic.endPath());
	}

	@Override
	public void mountSlotUpdateInfo()
	{
		therathic.mountSlotUpdateInfo();
	}

	@Override
	public String name()
	{
		return id.toString() + " (Pather)";
	}
}