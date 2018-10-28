package aer.path.pather;

import aer.locate.*;
import aer.path.schedule.*;
import aer.path.team.*;
import aer.relocatable.*;
import aer.relocatable.mount.*;
import java.util.*;

public class Pather extends Relocatable
{
	private Therathic therathic;

	private List<PathAction> possibleActionPaths;
	private List<PathAction> possibleInterrupts;

	public Pather(Identifier id, HexLocation loc, HexDirection direction, AirState airState,
			Therathic therathic, MType mType)
	{
		super(id, loc, direction, airState, mType);
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
		return therathic.endPath();
	}

	public void resetPossiblePaths()
	{
		possibleActionPaths = null;
		possibleInterrupts = null;
	}

	@Override
	public String name()
	{
		return id.toString() + " (Pather)";
	}
}