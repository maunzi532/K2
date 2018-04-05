package aer.commands;

import aer.*;

public class CMove implements VisualCommand
{
	public final HexLocation targetL;

	public CMove(HexLocation targetL)
	{
		this.targetL = targetL;
	}
}