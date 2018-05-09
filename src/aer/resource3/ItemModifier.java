package aer.resource3;

import aer.path.*;
import aer.path.team.*;

public interface ItemModifier extends Modifier
{
	HexItem item(ItemGetType type, TargetData targetData);
}