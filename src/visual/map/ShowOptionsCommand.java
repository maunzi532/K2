package visual.map;

import aer.path.*;
import java.util.*;
import visual.*;

public class ShowOptionsCommand implements VisualCommand
{
	public final List<PathAction> possibleActions;

	public ShowOptionsCommand(List<PathAction> possibleActions)
	{
		this.possibleActions = possibleActions;
	}

	@Override
	public boolean block()
	{
		return false;
	}

	@Override
	public boolean finished()
	{
		return false;
	}
}