package aer.resource3.resource4;

import aer.resource3.*;

public interface AttackType
{
	String name();
	int baseDamage();
	int attackMultiplier();
	int attackMultiplierR0();
	int attackMultiplierR1();
	int baseAccuracy();
	default int baseCrit()
	{
		return 0;
	}
	int attackStatType0();
	default int attackStatType1()
	{
		return 2;
	}
	int targetResType();
	default Modifier appliesModifier()
	{
		return null;
	}
	default boolean canRetaliate()
	{
		return true;
	}
	int cost();
	default int mCost()
	{
		return 1;
	}
	int retaliateCost();
	default int minRange()
	{
		return 0;
	}
	int maxRange();
	int maxRRange();
	default boolean noTurningRequired()
	{
		return false;
	}
}