package aer.commands;

import aer.*;

public class CMove implements ICommand
{
	public final HexLocation targetL;
	public final AirState airState;

	public CMove(Relocatable x1)
	{
		targetL = new HexLocation(x1.getLoc());
		airState = x1.getAirState();
	}

	public CMove(HexLocation targetL, AirState airState)
	{
		this.targetL = targetL;
		this.airState = airState;
	}

	public static void issueCommand(Relocatable x1)
	{
		x1.addCommand(new CMove(x1));
	}
}