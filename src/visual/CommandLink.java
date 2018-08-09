package visual;

import aer.commands.*;
import java.util.*;

public abstract class CommandLink implements ICommandLink
{
	protected final ArrayList<ICommand> commands = new ArrayList<>();

	public void addCommand(ICommand command)
	{
		commands.add(command);
	}

	@Override
	public List<ICommand> commands()
	{
		return commands;
	}

	@Override
	public void deleteCommands()
	{
		commands.clear();
	}
}