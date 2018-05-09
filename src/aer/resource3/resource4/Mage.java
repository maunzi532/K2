package aer.resource3.resource4;

import aer.path.*;
import aer.resource3.*;
import java.util.*;

public class Mage extends Transformation
{
	public EndHexItem movementItem;
	public HexItem personalWand;

	public Mage(TX_AP_Transform main)
	{
		super(main);
	}

	@Override
	public List<EndHexItem> endItems()
	{
		return Collections.singletonList(movementItem);
	}
}