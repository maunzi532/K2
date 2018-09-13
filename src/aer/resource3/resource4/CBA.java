package aer.resource3.resource4;

import aer.path.*;
import aer.path.team.*;
import java.util.*;

public interface CBA
{
	/*int health;
	int healthCurrent;
	int regenTime;
	int regenCurrent;
	int lives;
	int livesCurrent;*/

	boolean canBeAttacked();

	List<Reaction> reactions(Therathic attackedBy, StatItem item, AttackType attackType, int distance);

	void takeAttack(Therathic attackedBy, StatItem item, AttackType attackType, int distance, boolean retaliated, boolean dodge, StatItem blockWith);
}