package aer.path;

import aer.*;
import aer.path.team.*;
import java.util.*;

public class Pather extends Relocatable
{
	private Therathic therathic;
	private List<PathAction> possiblePaths;

	public Pather(int id, ITiledMap map, HexLocation loc, HexDirection direction, AirState airState, Therathic therathic)
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

	public void calculatePossiblePaths(ItemGetType type, TargetData targetData)
	{
		switch(type)
		{
			case ACTION:
				possiblePaths = therathic.possibleActivePaths();
				break;
			case INTERRUPT:
				possiblePaths = therathic.possibleInterrupts(targetData);
				break;
			case END:
				possiblePaths = Collections.singletonList(therathic.endPath());
				break;
		}
	}

	@Override
	public String name()
	{
		return "HexPather ID " + id;
	}
}