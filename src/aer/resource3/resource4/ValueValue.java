package aer.resource3.resource4;

import aer.path.pather.*;
import aer.path.takeable.*;
import aer.relocatable.*;
import aer.resource2.resource.*;
import aer.resource2.therathicType.*;
import javax.annotation.*;

public class ValueValue implements AIValue
{
	public ValueValue basedOn;
	public int addHealthVal;
	public int mountedVal;
	public int addPositionVal;
	public int valAP;
	public int valMP;

	public ValueValue(Pather pather, Resource_AP_MP resource)
	{
		resourceData(pather, resource);
	}

	public ValueValue(ValueValue valueValue, Pather pather, Resource_AP_MP resource, TakeableAction action)
	{
		basedOn = valueValue;
		resourceData(pather, resource);
		if(action instanceof Attack3)
		{
			//TODO
			addHealthVal = 1;
		}
	}

	private void resourceData(Pather pather, Resource_AP_MP resource)
	{
		valAP = resource.dActionPoints();
		valMP = resource.dMovementPoints();
		if(resource.dMount() != null)
		{
			MountSlotInfo msi = resource.dMount().getMountSlotInfo(resource.dMountedToSlot());
			if(resource.dMount() instanceof Pather && ((Pather) resource.dMount()).getTherathic() instanceof TX_CP_AlliedMount)
			{
				TX_CP_AlliedMount alliedMount = (TX_CP_AlliedMount) ((Pather) resource.dMount()).getTherathic();
				boolean whitelisted = alliedMount.whitelist.contains(pather.id);
				if(whitelisted)
				{
					if(msi.allowRotatedSteering)
						mountedVal = 30;
					else if(msi.allowSteering && resource.dDirection().equals(resource.dMount().getDirection()))
						mountedVal = 25;
					else if(msi.allowSteering)
						mountedVal = 20;
					else
						mountedVal = 10;
				}
				else
				{
					if(msi.allowSteering)
						mountedVal = 5;
					else
						mountedVal = 15;
				}

			}
			else
				mountedVal = 15;
		}
	}

	@Override
	public AIValue update(Pather pather, ActionResource resource, TakeableAction action)
	{
		return new ValueValue(this, pather, (Resource_AP_MP) resource, action);
	}

	public int healthVal()
	{
		if(basedOn == null)
			return addHealthVal;
		return basedOn.healthVal() + addHealthVal;
	}

	public int positionVal()
	{
		if(basedOn == null)
			return addPositionVal;
		return basedOn.positionVal() + addPositionVal;
	}

	@Override
	public int compareTo(@Nonnull AIValue aiValue)
	{
		if(!(aiValue instanceof ValueValue))
			throw new RuntimeException();
		ValueValue valueValue = (ValueValue) aiValue;
		int healthValC = healthVal() - valueValue.healthVal();
		if(healthValC != 0)
			return healthValC;
		int mountedValC = mountedVal - valueValue.mountedVal;
		if(mountedValC != 0)
			return mountedValC;
		int positionValC = positionVal() - valueValue.positionVal();
		if(positionValC != 0)
			return positionValC;
		int valAPC = valAP - valueValue.valAP;
		if(valAPC != 0)
			return valAP;
		return valMP - valueValue.valMP;
	}
}