package aer.commands;

import aer.*;

public class CDismount implements ICommand
{
	public final Relocatable mountedTo;

	public CDismount(Relocatable x1)
	{
		mountedTo = x1.getMountedTo();
	}

	public static void issueCommand(Relocatable x1)
	{
		x1.addCommand(new CDismount(x1));
	}
}