package aer.commands;

import aer.*;

public class CMount implements ICommand
{
	public final Relocatable targetM;
	public final int targetSlot;

	public CMount(Relocatable x1)
	{
		targetM = x1.getMountedTo();
		targetSlot = x1.getMountedToSlot();
	}

	public static void issueCommand(Relocatable x1, boolean move)
	{
		x1.addCommand(new CMount(x1));
		if(move)
			x1.addCommand(new CMove(x1));
	}
}