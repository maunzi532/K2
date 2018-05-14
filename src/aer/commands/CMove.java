package aer.commands;

import aer.*;

public class CMove implements VisualCommand
{
	public final HexLocation targetL;
	public final AirState airState;

	public CMove(HexObject x1)
	{
		targetL = x1.getLoc();
		airState = x1.getAirState();
	}

	public CMove(HexLocation targetL, AirState airState)
	{
		this.targetL = targetL;
		this.airState = airState;
	}

	public static void issueCommand(HexObject x1)
	{
		x1.addCommand(new CMove(x1));
	}
}