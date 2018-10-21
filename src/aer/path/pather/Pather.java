package aer.path.pather;

import aer.locate.*;
import aer.path.schedule.*;
import aer.path.team.*;
import aer.relocatable.*;
import java.util.*;

public class Pather extends Relocatable
{
	private Therathic therathic;

	private List<PathAction> possibleActionPaths;
	private List<PathAction> possibleInterrupts;
	private PathAction endPath;

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

	public List<PathAction> getPossibleActionPaths()
	{
		if(possibleActionPaths == null)
			possibleActionPaths = therathic.possibleActivePaths();
		return possibleActionPaths;
	}

	public List<PathAction> getPossibleInterrupts(TargetData targetData)
	{
		if(possibleInterrupts == null)
			possibleInterrupts = therathic.possibleInterrupts(targetData);
		return possibleInterrupts;
	}

	public PathAction getEndPath()
	{
		if(endPath == null)
			endPath = therathic.endPath();
		return endPath;
	}

	public void resetPossiblePaths()
	{
		possibleActionPaths = null;
		possibleInterrupts = null;
		endPath = null;
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