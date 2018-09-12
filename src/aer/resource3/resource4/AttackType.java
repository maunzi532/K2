package aer.resource3.resource4;

import aer.resource3.*;

public interface AttackType
{
	int baseDamage();
	int attackMultiplier();
	int attackMultiplierR0();
	int attackMultiplierR1();
	int baseAccuracy();
	int baseCrit();
	int attackStatType0();
	int attackStatType1();
	int targetResType();
	Modifier appliesModifier();
	boolean canRetaliate();
	int cost();
	int mCost();
	int retaliateCost();
	int minRange();
	int maxRange();
	boolean noTurningRequired();
}