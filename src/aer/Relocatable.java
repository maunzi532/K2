package aer;

import aer.commands.*;
import java.io.*;

public class Relocatable extends CommandLink implements Serializable
{
	public final Identifier id;
	public transient ITiledMap map;
	private HexLocation rLoc;
	private HexDirection rDirection;
	private AirState airState;
	private Identifier mountedToID;
	private transient Relocatable mountedTo;
	private int mountedToSlot;
	private transient Relocatable[] mountSlots;
	private MountSlotInfo[] mountSlotInfo;

	public Relocatable(Identifier id, ITiledMap map, HexLocation loc, HexDirection direction, AirState airState, MountSlotInfo... mountSlotInfo)
	{
		this.id = id;
		this.map = map;
		this.rLoc = loc;
		this.rDirection = direction;
		this.airState = airState;
		this.mountSlotInfo = mountSlotInfo;
	}

	public HexLocation getLoc()
	{
		if(mountedToID != null)
			return HexLocation.plus(getMountedTo().getLoc(), rLoc);
		return rLoc;
	}

	public void setLoc(HexLocation loc, AC ac)
	{
		if(mountedToID != null)
			this.rLoc = HexLocation.minus(loc, getMountedTo().getLoc());
		else
			this.rLoc = loc;
		if(ac != null)
			CMove.issueCommand(this);
	}

	public HexDirection getDirection()
	{
		if(mountedToID != null)
			return HexDirection.plus(getMountedTo().getDirection(), rDirection);
		return rDirection;
	}

	public void setDirection(HexDirection direction, AC ac)
	{
		if(mountedToID != null)
			this.rDirection = HexDirection.minus(direction, getMountedTo().getDirection());
		else
			this.rDirection = direction;
		if(ac != null)
			CTurn.issueCommand(this);
	}

	public AirState getAirState()
	{
		if(mountedToID != null)
			return AirState.MOUNT;
		return airState;
	}

	public void setAirState(AirState airState, AC ac)
	{
		this.airState = airState;
		if(ac != null)
			CMove.issueCommand(this);
	}

	public Identifier getMountedToID()
	{
		return mountedToID;
	}

	public Relocatable getMountedTo()
	{
		if(mountedTo == null && mountedToID != null)
		{
			mountedTo = map.objectByID(mountedToID);
		}
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

	private Relocatable[] getMountSlots()
	{
		if(mountSlots == null)
		{
			mountSlots = map.determineMountSlots(id, mountSlotInfo);
		}
		return mountSlots;
	}

	public Relocatable getMountSlotAt(int slot)
	{
		return getMountSlots()[slot];
	}

	public void setMountSlotAt(int slot, Relocatable m)
	{
		getMountSlots()[slot] = m;
		mountSlotUpdateInfo();
	}

	public MountSlotInfo getMountSlotInfo(int slot)
	{
		return mountSlotInfo[slot];
	}

	public MountSlotInfo[] getMountSlotInfo()
	{
		return mountSlotInfo;
	}

	public void setMountedTo(Identifier newMountedToID, int slot, AC ac)
	{
		dismount(AirState.MOUNT, ac);
		mountedToID = newMountedToID;
		mountedToSlot = slot;
		getMountedTo().setMountSlotAt(mountedToSlot, this);
		setLoc(rLoc, null);
		setDirection(rDirection, null);
		if(ac != null)
			CMount.issueCommand(this);
	}

	public void dismount(AirState toState, AC ac)
	{
		if(mountedToID != null)
		{
			getMountedTo().setMountSlotAt(mountedToSlot, null);
			rLoc = getLoc();
			rDirection = getDirection();
			setAirState(toState, null);
			if(ac != null)
				CDismount.issueCommand(this);
			mountedToID = null;
			mountedTo = null;
			if(ac != null && toState != AirState.MOUNT)
				CMove.issueCommand(this);
		}
		else
			setAirState(toState, null);
	}

	public void updateMountSlots(MountSlotInfo[] newInfo, boolean keep, AC ac)
	{
		Relocatable[] newSlots = new Relocatable[newInfo.length];
		for(int i = 0; i < getMountSlotCount(); i++)
		{
			if(getMountSlotAt(i) != null)
			{
				if(keep && i < newSlots.length && (newInfo[i].allowRotating || getMountSlotAt(i).getDirection().r == 0))
				{
					newSlots[i] = getMountSlotAt(i);
				}
				else
				{
					getMountSlotAt(i).dismount(AirState.FLY, ac);
				}
			}
		}
		mountSlotInfo = newInfo;
		mountSlots = newSlots;
		mountSlotUpdateInfo();
	}

	public void mountSlotUpdateInfo(){}

	@Override
	public String name()
	{
		return id.toString();
	}
}