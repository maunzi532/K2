package aer;

import visual.*;

public class Relocatable extends CommandLink
{
	public final Identifier id;
	public final ITiledMap map;
	private HexLocation loc;
	private HexDirection direction;
	private AirState airState;
	private Relocatable mount;

	public Relocatable(Identifier id, ITiledMap map, HexLocation loc, HexDirection direction, AirState airState)
	{
		this.id = id;
		this.map = map;
		this.loc = loc;
		this.direction = direction;
		this.airState = airState;
	}

	public HexLocation getLoc()
	{
		if(mount != null)
			return mount.getLoc();
		return loc;
	}

	public void setLoc(HexLocation loc)
	{
		if(mount != null)
		{
			mount.setLoc(loc);
			return;
		}
		this.loc = loc;
	}

	public HexDirection getDirection()
	{
		if(mount != null)
			return HexDirection.plus(direction, mount.getDirection());
		return direction;
	}

	public void setDirection(HexDirection direction)
	{
		if(mount != null)
		{
			this.direction = HexDirection.minus(direction, mount.getDirection());
			return;
		}
		this.direction = direction;
	}

	public AirState getAirState()
	{
		if(mount != null)
			return AirState.MOUNT;
		return airState;
	}

	public void setAirState(AirState airState)
	{
		this.airState = airState;
	}

	public Relocatable getMount()
	{
		return mount;
	}

	public void setMount(Relocatable mount)
	{
		if(mount == null)
		{
			loc = this.mount.getLoc();
			direction = HexDirection.plus(direction, this.mount.getDirection());
			this.mount = null;
		}
		else
		{
			this.mount = mount;
			direction = HexDirection.minus(direction, this.mount.getDirection());
		}
	}

	public MapTile currentTile()
	{
		return map.getTile(loc);
	}

	@Override
	public String name()
	{
		return id.toString();
	}
}