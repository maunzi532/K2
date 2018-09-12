package aer.resource3.resource4;

import aer.resource3.*;

public class AttackType0 implements AttackType
{
	@Override
	public int baseDamage()
	{
		return 0;
	}

	@Override
	public int attackMultiplier()
	{
		return 0;
	}

	@Override
	public int attackMultiplierR0()
	{
		return 0;
	}

	@Override
	public int attackMultiplierR1()
	{
		return 0;
	}

	@Override
	public int baseAccuracy()
	{
		return 0;
	}

	@Override
	public int baseCrit()
	{
		return 0;
	}

	@Override
	public int attackStatType0()
	{
		return 0;
	}

	@Override
	public int attackStatType1()
	{
		return 0;
	}

	@Override
	public int targetResType()
	{
		return 0;
	}

	@Override
	public Modifier appliesModifier()
	{
		return null;
	}

	@Override
	public boolean canRetaliate()
	{
		return true;
	}

	@Override
	public int cost()
	{
		return 60;
	}

	@Override
	public int mCost()
	{
		return 1;
	}

	@Override
	public int retaliateCost()
	{
		return 10;
	}

	@Override
	public int minRange()
	{
		return 0;
	}

	@Override
	public int maxRange()
	{
		return 2;
	}

	@Override
	public boolean noTurningRequired()
	{
		return false;
	}
}