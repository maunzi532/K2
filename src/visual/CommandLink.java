package visual;

import java.util.*;

public abstract class CommandLink implements VisualLink
{
	protected final ArrayList<VisualCommand> commands = new ArrayList<>();

	public void addCommand(VisualCommand command)
	{
		commands.add(command);
	}

	@Override
	public List<VisualCommand> commands()
	{
		return commands;
	}

	@Override
	public void deleteCommands()
	{
		commands.clear();
	}
}