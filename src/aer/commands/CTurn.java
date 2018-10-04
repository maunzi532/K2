package aer.commands;

import aer.locate.*;
import aer.relocatable.*;

public class CTurn implements ICommand
{
	public final HexDirection targetD;

	public CTurn(Relocatable x1)
	{
		targetD = new HexDirection(x1.getDirection());
	}

	public CTurn(HexDirection targetD)
	{
		this.targetD = targetD;
	}

	public static void issueCommand(Relocatable x1)
	{
		x1.addCommand(new CTurn(x1));
	}
}