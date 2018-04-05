package visual;

import aer.commands.*;
import java.util.*;

public interface VisualLink
{
	String name();

	List<VisualCommand> commands();

	void deleteCommands();
}