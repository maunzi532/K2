package aer;

import visual.*;

public class HexObject extends CommandLink
{
	public final int id;
	public final IHexMap map;
	protected HexLocation loc;
	protected HexDirection direction;
	protected AirState airState;

	public HexObject(int id, IHexMap map, HexLocation loc, HexDirection direction, AirState airState)
	{
		this.id = id;
		this.map = map;
		this.loc = loc;
		this.direction = direction;
		this.airState = airState;
	}

	public HexLocation getLoc()
	{
		return loc;
	}

	public void setLoc(HexLocation loc)
	{
		this.loc = loc;
	}

	public HexDirection getDirection()
	{
		return direction;
	}

	public void setDirection(HexDirection direction)
	{
		this.direction = direction;
	}

	public AirState getAirState()
	{
		return airState;
	}

	public void setAirState(AirState airState)
	{
		this.airState = airState;
	}

	public MapTile currentTile()
	{
		return map.getTile(loc);
	}

	@Override
	public String name()
	{
		return null;
	}
}