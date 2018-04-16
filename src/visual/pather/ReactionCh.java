package visual.pather;

import aer.path.*;
import java.util.*;
import visual.map.*;

public class ReactionCh
{
	public final List<Reaction> reactions;
	public int num;

	public ReactionCh(List<Reaction> reactions)
	{
		this.reactions = reactions;
		if(reactions.isEmpty())
			throw new RuntimeException();
	}

	public Reaction exec(Input1 input1)
	{
		if(input1 == null)
			return null;
		switch(input1)
		{
			case PLUSD:
			case CHANGE:
				num++;
				if(num >= reactions.size())
					num = 0;
				break;
			case MINUSD:
				num--;
				if(num < 0)
					num = reactions.size() - 1;
				break;
			case ACCEPT:
				return reactions.get(num);
		}
		return null;
	}
}