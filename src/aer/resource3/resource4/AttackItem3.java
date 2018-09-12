package aer.resource3.resource4;

import java.util.*;

public class AttackItem3 implements StatItem
{
	@Override
	public String name()
	{
		return "Agro";
	}

	@Override
	public StatItemType type()
	{
		return StatItemType.Hand;
	}

	@Override
	public List<AttackType> attackTypes()
	{
		return Collections.singletonList(new AttackType0());
	}
}