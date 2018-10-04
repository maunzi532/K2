package aer.path.pather;

import aer.path.takeable.*;
import aer.path.team.*;

public interface EndPatherItem extends PatherItem
{
	TakeableAction endAction(ActionResource resource, Therathic therathic);
}