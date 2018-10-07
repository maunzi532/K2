package aer.path.pather;

import aer.path.takeable.*;

public interface AIValue extends Comparable<AIValue>
{
	AIValue update(Pather pather, ActionResource resource, TakeableAction action);
}