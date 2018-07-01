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
				showChoiceOptions();
				break;
			case MINUSD:
				num--;
				if(num < 0)
					num = reactions.size() - 1;
				showChoiceOptions();
				break;
			case ACCEPT:
				if(num == 0 || reactions.get(num).available)
					return reactions.get(num);
				break;
		}
		return null;
	}

	public void showChoiceOptions()
	{
		System.out.println("Choose Reaction with enter key");
		System.out.println("Reactions:");
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < reactions.size(); i++)
			sb.append(i == num ? "> " : "| ").append(reactions.get(i).text)
					.append(i == 0 || reactions.get(i).available ? "" : " (Not available)").append("\n");
		System.out.print(sb.toString());
	}
}