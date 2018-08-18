package aer;

import visual.*;

public class Relocatable extends CommandLink
{
	public final Identifier id;
	public final ITiledMap map;
	private HexLocation loc;
	private HexDirection direction;
	private AirState airState;
	private Relocatable mountedTo;
	private int mountedToSlot;
	private Relocatable[] mountSlots;
	private MountSlotInfo[] mountSlotInfo;

	public Relocatable(Identifier id, ITiledMap map, HexLocation loc, HexDirection direction, AirState airState, MountSlotInfo... mountSlotInfo)
	{
		this.id = id;
		this.map = map;
		this.loc = loc;
		this.direction = direction;
		this.airState = airState;
		this.mountSlotInfo = mountSlotInfo;
		mountSlots = new Relocatable[mountSlotInfo.length];
	}

	public HexLocation getLoc()
	{
		if(mountedTo != null)
			return HexLocation.plus(mountedTo.getLoc(), loc);
		return loc;
	}

	public void setLoc(HexLocation loc)
	{
		if(mountedTo != null)
		{
			this.loc = HexLocation.minus(loc, mountedTo.getLoc());
			return;
		}
		this.loc = loc;
	}

	public HexDirection getDirection()
	{
		if(mountedTo != null)
			return HexDirection.plus(mountedTo.getDirection(), direction);
		return direction;
	}

	public void setDirection(HexDirection direction)
	{
		if(mountedTo != null)
		{
			this.direction = HexDirection.minus(direction, mountedTo.getDirection());
			return;
		}
		this.direction = direction;
	}

	public HexLocation getRelativeLoc()
	{
		return loc;
	}

	public void setRelativeLoc(HexLocation loc)
	{
		this.loc = loc;
	}

	public HexDirection getRelativeDirection()
	{
		return direction;
	}

	public void setRelativeDirection(HexDirection direction)
	{
		this.direction = direction;
	}

	public AirState getAirState()
	{
		if(mountedTo != null)
			return AirState.MOUNT;
		return airState;
	}

	public void setAirState(AirState airState)
	{
		this.airState = airState;
	}

	public Relocatable getMountedTo()
	{
		return mountedTo;
	}

	public int getMountedToSlot()
	{
		return mountedToSlot;
	}

	public int getMountSlotCount()
	{
		return mountSlotInfo.length;
	}

	public Relocatable getMountSlotAt(int slot)
	{
		return mountSlots[slot];
	}

	public MountSlotInfo getMountSlotInfo(int slot)
	{
		return mountSlotInfo[slot];
	}

	public void setMountedTo(Relocatable newMountedTo, int slot)
	{
		dismount(AirState.MOUNT);
		mountedTo = newMountedTo;
		mountedToSlot = slot;
		mountedTo.mountSlots[mountedToSlot] = this;
		setLoc(getRelativeLoc());
		setDirection(getRelativeDirection());
	}

	public void dismount(AirState toState)
	{
		if(mountedTo != null)
		{
			mountedTo.mountSlots[mountedToSlot] = null;
			setRelativeLoc(getLoc());
			setRelativeDirection(getDirection());
			mountedTo = null;
		}
		setAirState(toState);
	}

	public void updateMountSlots(boolean keep, MountSlotInfo... newInfo)
	{
		Relocatable[] newSlots = new Relocatable[newInfo.length];
		for(int i = 0; i < mountSlots.length; i++)
		{
			if(mountSlots[i] != null)
			{
				if(keep && i < newSlots.length && (newInfo[i].allowRotating || mountSlots[i].getDirection().r == 0))
				{
					newSlots[i] = mountSlots[i];
				}
				else
				{
					mountSlots[i].dismount(AirState.FLY);
				}
			}
		}
		mountSlotInfo = newInfo;
		mountSlots = newSlots;
	}

	@Override
	public String name()
	{
		return id.toString();
	}
}