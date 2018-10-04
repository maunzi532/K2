package aer.commands;

import aer.locate.*;
import aer.relocatable.*;

public class CMount implements ICommand
{
	public final Relocatable targetM;
	public final int targetSlot;
	public final HexDirection direction;

	public CMount(Relocatable x1)
	{
		targetM = x1.getMountedTo();
		targetSlot = x1.getMountedToSlot();
		direction = new HexDirection(x1.getDirection());
	}

	public static void issueCommand(Relocatable x1)
	{
		x1.addCommand(new CMount(x1));
	}
}