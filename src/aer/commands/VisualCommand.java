package aer.commands;

public interface VisualCommand
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