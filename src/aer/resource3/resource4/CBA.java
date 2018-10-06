package aer.resource3.resource4;

import aer.path.schedule.*;
import aer.path.team.*;
import java.util.*;

public interface CBA
{
	boolean canBeAttacked();

	List<Reaction> reactions(Therathic attackedBy, StatItem item, AttackType attackType, int distance);

	void takeAttack(Therathic attackedBy, StatItem item, AttackType attackType, int distance, boolean retaliated, boolean dodge);

	int statAttack(int num);

	int statArmor(int num);

	int statResist(int num);

	int statAvoid();

	int statFocus();
}