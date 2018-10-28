package aer.resource2.therathicType;

import aer.path.pather.*;
import aer.path.team.*;
import aer.relocatable.*;
import aer.relocatable.mount.*;
import aer.resource2.items.item2.*;
import java.util.*;

public class TX_CP_AlliedMount extends TX_CP
{
	public List<Identifier> whitelist;
	private transient List<Pather> steering;

	public TX_CP_AlliedMount(int teamSide, Identifier... whitelist)
	{
		super(teamSide);
		npc_control = new UselessNPC();
		endItem = new FloorMovementItem2(this);
		items.add(endItem);
		this.whitelist = Arrays.asList(whitelist);
	}

	@Override
	public MType mType()
	{
		return new MTypeObject();//TODO
	}

	@Override
	public AIValue aiValue()
	{
		return null;
	}

	public void mountSlotUpdateInfo()
	{
		steering = new ArrayList<>();
		for(int i = 0; i < pather.getMountSlotInfo().length; i++)
		{
			if(pather.getMountSlotInfo(i).allowSteering)
			{
				Relocatable m = pather.getMountSlotAt(i);
				if(m instanceof Pather && whitelist.contains(m.id) &&
						(pather.getMountSlotInfo(i).allowRotatedSteering || m.getDirection().equals(pather.getDirection())))
					steering.add((Pather) m);
			}
		}
	}

	public List<Pather> getSteering()
	{
		if(steering == null)
			mountSlotUpdateInfo();
		return steering;
	}

	@Override
	public boolean playerControlled()
	{
		return getSteering().stream().anyMatch(e -> e.getTherathic().playerControlled());
	}
}