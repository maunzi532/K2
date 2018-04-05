package aer.commands;

import aer.*;

public class CTurn implements VisualCommand
{
	public final HexDirection targetD;

	public CTurn(HexDirection targetD)
	{
		this.targetD = targetD;
	}
}