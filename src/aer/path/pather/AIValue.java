package aer.path.pather;

import aer.path.takeable.*;

public interface AIValue extends Comparable<AIValue>
{
	AIValue update(ActionResource resource, TakeableAction action);
}