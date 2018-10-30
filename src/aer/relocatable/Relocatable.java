package aer.relocatable;

import aer.commands.*;
import aer.locate.*;
import aer.map.*;
import aer.relocatable.mount.*;
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
	private MType mType;

	public Relocatable(Identifier id, HexLocation loc, HexDirection direction, AirState airState, MType mType)
	{
		this.id = id;
		this.rLoc = loc;
		this.rDirection = direction;
		this.airState = airState;
		this.mType = mType;
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
		return mType.seats();
	}

	public Relocatable[] getMountSlots()
	{
		if(mountSlots == null)
		{
			if(map != null)
			{
				mountSlots = map.determineMountSlots(id, mType.seats());
			}
			else
			{
				mountSlots = new Relocatable[mType.seats()];
			}
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
		mType.update();
	}

	public MountSlotInfo getMountSlotInfo(int slot)
	{
		return mType.info()[slot];
	}

	public MountSlotInfo[] getMountSlotInfo()
	{
		return mType.info();
	}

	public MType getMType()
	{
		return mType;
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
			rLoc = getLoc();
			rDirection = getDirection();
			setAirState(toState, null);
			getMountedTo().setMountSlotAt(mountedToSlot, null);
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

	public void updateMType(MType newType, boolean keep, AC ac)
	{
		Relocatable[] newSlots = new Relocatable[newType.seats()];
		for(int i = 0; i < getMountSlotCount(); i++)
		{
			if(getMountSlotAt(i) != null)
			{
				if(keep && i < newSlots.length && (newType.info()[i].allowRotating || getMountSlotAt(i).getDirection().r == 0))
				{
					newSlots[i] = getMountSlotAt(i);
				}
				else
				{
					getMountSlotAt(i).dismount(AirState.FLY, ac);
				}
			}
		}
		mType = newType;
		mType.setMain(this);
		mountSlots = newSlots;
		mType.update();
	}

	@Override
	public String name()
	{
		return id.toString();
	}
}