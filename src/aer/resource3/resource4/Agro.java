package aer.resource3.resource4;

import java.util.*;

public class Agro implements StatItem
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
		return Collections.singletonList(new AgroAttack());
	}

	public class AgroAttack implements AttackType
	{
		@Override
		public String name()
		{
			return "AgroAttack";
		}

		@Override
		public int baseDamage()
		{
			return 50;
		}

		@Override
		public int attackMultiplier()
		{
			return 20;
		}

		@Override
		public int attackMultiplierR0()
		{
			return 10;
		}

		@Override
		public int attackMultiplierR1()
		{
			return 10;
		}

		@Override
		public int baseAccuracy()
		{
			return 80;
		}

		@Override
		public int baseCrit()
		{
			return 5;
		}

		@Override
		public int attackStatType0()
		{
			return 0;
		}

		@Override
		public int targetResType()
		{
			return 0;
		}

		@Override
		public int cost()
		{
			return 60;
		}

		@Override
		public int retaliateCost()
		{
			return 10;
		}

		@Override
		public int maxRange()
		{
			return 2;
		}

		@Override
		public int maxRRange()
		{
			return 2;
		}
	}
}