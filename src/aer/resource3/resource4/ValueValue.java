package aer.resource3.resource4;

import aer.path.pather.*;
import aer.path.takeable.*;
import aer.resource2.resource.*;
import javax.annotation.*;

public class ValueValue implements AIValue
{
	public ValueValue(){}

	public ValueValue(ValueValue valueValue, Resource_AP_MP resource, TakeableAction action)
	{

	}

	@Override
	public AIValue update(ActionResource resource, TakeableAction action)
	{
		return new ValueValue(this, (Resource_AP_MP) resource, action);
	}

	@Override
	public int compareTo(@Nonnull AIValue aiValue)
	{
		if(!(aiValue instanceof ValueValue))
			return 0;
		return 0;
	}
}