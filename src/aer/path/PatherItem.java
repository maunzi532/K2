package aer.path;

import aer.path.takeable.*;
import java.io.*;
import java.util.*;

public interface PatherItem extends Serializable
{
	List<TakeableAction> takeableActions(PathAction pathAction);

	default List<TakeableAction> interrupts(TargetData targetData)
	{
		return Collections.EMPTY_LIST;
	}
}