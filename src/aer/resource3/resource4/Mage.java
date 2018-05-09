package aer.resource3.resource4;

import aer.path.*;
import aer.resource3.*;

public class Mage extends Transformation
{
	public EndHexItem movementItem;
	public HexItem personalWand;

	public Mage(TX_AP_Transform main)
	{
		super(main);
	}

	@Override
	public EndHexItem endItem()
	{
		return movementItem;
	}
}