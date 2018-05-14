package aer.commands;

import aer.*;

public class CTurn implements VisualCommand
{
	public final HexDirection targetD;

	public CTurn(HexObject x1)
	{
		targetD = x1.getDirection();
	}

	public CTurn(HexDirection targetD)
	{
		this.targetD = targetD;
	}

	public static void issueCommand(HexObject x1)
	{
		x1.addCommand(new CTurn(x1));
	}
}