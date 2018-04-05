package aer.commands;

public class BlockCommand implements VisualCommand
{
	private boolean finished = false;

	public void finish()
	{
		finished = true;
	}

	@Override
	public boolean block()
	{
		return true;
	}

	@Override
	public boolean finished()
	{
		return finished;
	}
}