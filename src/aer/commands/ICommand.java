package aer.commands;

public interface ICommand
{
	default boolean block()
	{
		return false;
	}

	default boolean finished()
	{
		return true;
	}
}