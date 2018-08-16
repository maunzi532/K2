package aer.path;

import aer.*;
import aer.path.team.*;
import java.util.*;

public class Pather extends Relocatable
{
	private Therathic therathic;
	private List<PathAction> possiblePaths;

	public Pather(Identifier id, ITiledMap map, HexLocation loc, HexDirection direction, AirState airState, Therathic therathic)
	{
		super(id, map, loc, direction, airState);
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
	public String name()
	{
		return id.toString() + " (Pather)";
	}
}