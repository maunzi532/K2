package aer;

import aer.commands.*;
import visual.*;

public class Relocatable extends CommandLink
{
	public final Identifier id;
	public final ITiledMap map;
	private HexLocation rLoc;
	private HexDirection rDirection;
	private AirState airState;
	private Relocatable mountedTo;
	private int mountedToSlot;
	private Relocatable[] mountSlots;
	private MountSlotInfo[] mountSlotInfo;

	public Relocatable(Identifier id, ITiledMap map, HexLocation loc, HexDirection direction, AirState airState, MountSlotInfo... mountSlotInfo)
	{
		this.id = id;
		this.map = map;
		this.rLoc = loc;
		this.rDirection = direction;
		this.airState = airState;
		this.mountSlotInfo = mountSlotInfo;
		mountSlots = new Relocatable[mountSlotInfo.length];
	}

	public HexLocation getLoc()
	{
		if(mountedTo != null)
			return HexLocation.plus(mountedTo.getLoc(), rLoc);
		return rLoc;
	}

	public void setLoc(HexLocation loc, AC ac)
	{
		if(mountedTo != null)
			this.rLoc = HexLocation.minus(loc, mountedTo.getLoc());
		else
			this.rLoc = loc;
		if(ac != null)
			CMove.issueCommand(this);
	}

	public HexDirection getDirection()
	{
		if(mountedTo != null)
			return HexDirection.plus(mountedTo.getDirection(), rDirection);
		return rDirection;
	}

	public void setDirection(HexDirection direction, AC ac)
	{
		if(mountedTo != null)
			this.rDirection = HexDirection.minus(direction, mountedTo.getDirection());
		else
			this.rDirection = direction;
		if(ac != null)
			CTurn.issueCommand(this);
	}

	public AirState getAirState()
	{
		if(mountedTo != null)
			return AirState.MOUNT;
		return airState;
	}

	public void setAirState(AirState airState, AC ac)
	{
		this.airState = airState;
		if(ac != null)
			CMove.issueCommand(this);
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

	public void setMountedTo(Relocatable newMountedTo, int slot, AC ac)
	{
		dismount(AirState.MOUNT, ac);
		mountedTo = newMountedTo;
		mountedToSlot = slot;
		mountedTo.mountSlots[mountedToSlot] = this;
		setLoc(rLoc, null);
		setDirection(rDirection, null);
		if(ac != null)
			CMount.issueCommand(this, true);
	}

	public void dismount(AirState toState, AC ac)
	{
		if(mountedTo != null)
		{
			mountedTo.mountSlots[mountedToSlot] = null;
			rLoc = getLoc();
			rDirection = getDirection();
			setAirState(toState, null);
			if(ac != null)
				CDismount.issueCommand(this, toState != AirState.MOUNT);
			mountedTo = null;
		}
		else
			setAirState(toState, null);
	}

	public void updateMountSlots(MountSlotInfo[] newInfo, boolean keep, AC ac)
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
					mountSlots[i].dismount(AirState.FLY, ac);
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