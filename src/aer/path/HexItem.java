package aer.path;

import aer.path.takeable.*;
import java.util.*;

public interface HexItem
{
	List<TakeableAction> takeableActions(PathAction pathAction);
}