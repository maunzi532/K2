package aer.resource3.resource4;

import java.util.*;

public class AttackCalc
{
	public static final Random RANDOM = new Random();

	public final int baseDamage;
	public final int hitrate0;
	public final int halfDamage;
	public final int hitrate1;
	public final int critMultiplier;
	public final int critrate;

	public AttackCalc(CBA attackedBy, StatItem item, AttackType a,
			CBA target, int distance, boolean retaliated, boolean dodge)
	{
		int rawDamage;
		if(retaliated)
		{
			rawDamage = a.baseDamage() +
					a.attackMultiplierR0() * attackedBy.statAttack(a.attackStatType0()) +
					a.attackMultiplierR1() * attackedBy.statAttack(a.attackStatType1());
		}
		else
		{
			rawDamage = a.baseDamage() +
					a.attackMultiplier() * attackedBy.statAttack(a.attackStatType0());
		}
		int armorReduction = 20 * target.statArmor(a.targetResType());
		int resistFactor = 500 / (5 + target.statResist(a.targetResType()));
		int damageA = rawDamage - armorReduction;
		if(damageA <= 0)
			baseDamage = 0;
		else
			baseDamage = damageA * resistFactor / 100;
		halfDamage = baseDamage / 2;
		int ath = a.baseAccuracy();
		int focus = attackedBy.statFocus();
		int avoid = target.statAvoid() * (10 - focus);
		if(dodge)
		{
			avoid += 25 + target.statAvoid() * 10;
		}
		hitrate0 = ath + focus * 10 - avoid;
		hitrate1 = hitrate0 + 50;
		critrate = a.baseCrit();
		critMultiplier = 100;
	}

	@Override
	public String toString()
	{
		return "AttackCalc{" +
				"baseDamage=" + baseDamage +
				", hitrate0=" + hitrate0 +
				", halfDamage=" + halfDamage +
				", hitrate1=" + hitrate1 +
				", critMultiplier=" + critMultiplier +
				", critrate=" + critrate +
				'}';
	}
}