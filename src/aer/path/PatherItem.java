package aer.path;

import aer.path.takeable.*;
import java.util.*;

public interface PatherItem
{
	List<TakeableAction> takeableActions(PathAction pathAction);

	default List<TakeableAction> interrupts(TargetData targetData)
	{
		return Collections.EMPTY_LIST;
	}
}