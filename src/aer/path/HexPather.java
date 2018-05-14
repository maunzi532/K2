package aer.path;

import aer.*;
import aer.path.team.*;
import java.util.*;

public class HexPather extends HexObject
{
	private TherathicHex therathicHex;
	private List<PathAction> possiblePaths;

	public HexPather(int id, IHexMap map, HexLocation loc, HexDirection direction, AirState airState, TherathicHex therathicHex)
	{
		super(id, map, loc, direction, airState);
		this.therathicHex = therathicHex;
		therathicHex.linkTo(this);
	}

	public TherathicHex getTherathicHex()
	{
		return therathicHex;
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
				possiblePaths = therathicHex.possibleActivePaths();
				break;
			case INTERRUPT:
				possiblePaths = therathicHex.possibleInterrupts(targetData);
				break;
			case END:
				possiblePaths = Collections.singletonList(therathicHex.endPath());
				break;
		}
	}

	@Override
	public String name()
	{
		return "HexPather ID " + id;
	}
}