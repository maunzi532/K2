package aer.commands;

import java.util.*;

public interface ICommandLink
{
	String name();

	List<ICommand> commands();

	void deleteCommands();
}