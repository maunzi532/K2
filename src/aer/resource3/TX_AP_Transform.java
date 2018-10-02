package aer.resource3;

import aer.*;
import aer.commands.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.resource2.therathicType.*;
import aer.resource3.resource4.*;
import java.util.*;

public class TX_AP_Transform extends TX_AP implements CBA
{
	private List<Transformation> transforms;
	public List<AppliedModifier> modifiers;

	public TX_AP_Transform(Transformation transform0, int teamSide)
	{
		super(teamSide);
		transforms = new ArrayList<>();
		modifiers = new ArrayList<>();
		transformInto(transform0);
		npc_control = new UselessNPC();
	}

	public Transformation currentTransform()
	{
		return transforms.get(transforms.size() - 1);
	}

	public Transformation prevTransform()
	{
		if(transforms.size() <= 1)
			return null;
		return transforms.get(transforms.size() - 2);
	}

	public Transformation firstTransform()
	{
		return transforms.get(0);
	}

	public void transformInto(Transformation transformation)
	{
		transforms.add(transformation);
		transformation.main = this;
		if(pather != null)
			pather.updateMountSlots(transformation.mountSlotInfo(), transformation.transformKeepMounted(), new AC());
	}

	public void endTransformation(Transformation transformation)
	{
		if(!transforms.remove(transformation))
			throw new RuntimeException();
	}

	public void applyModifier(Modifier modifier)
	{
		AppliedModifier am = new AppliedModifier(modifier, this);
		if(modifier.affectsTransformationOnly())
		{
			currentTransform().modifiers.add(am);
		}
		else
		{
			modifiers.add(am);
		}
	}

	public List<AppliedModifier> activeModifiers()
	{
		ArrayList re = new ArrayList<>();
		re.addAll(modifiers);
		re.addAll(currentTransform().modifiers);
		return re;
	}

	public void tickModifiers()
	{
		tickModifierList(modifiers, true);
		for(int i = 0; i < transforms.size(); i++)
		{
			Transformation t0 = transforms.get(i);
			tickModifierList(t0.modifiers, i == transforms.size() - 1);
		}
	}

	private static void tickModifierList(List<AppliedModifier> modifiers1, boolean active)
	{
		for(AppliedModifier modifier0 : modifiers1)
			if(modifier0.tick(active))
			{
				modifier0.end(active);
				modifiers1.remove(modifier0);
			}
	}

	@Override
	public void linkTo(Pather pather)
	{
		super.linkTo(pather);
		pather.updateMountSlots(currentTransform().mountSlotInfo(), currentTransform().transformKeepMounted(), new AC());
	}

	@Override
	public MountSlotInfo[] mountSlotInfo()
	{
		return currentTransform().mountSlotInfo();
	}

	@Override
	public List<PatherItem> activeItems()
	{
		return currentTransform().activeItems();
	}

	@Override
	public List<PatherItem> interruptItems(TargetData targetData)
	{
		return currentTransform().interruptItems(targetData);
	}

	@Override
	public List<EndPatherItem> endItems()
	{
		return currentTransform().endItems();
	}

	@Override
	public TakeableAction startAction()
	{
		return new InitAction(currentTransform(), usedFirstPath);
	}

	@Override
	public boolean drawPhase()
	{
		super.drawPhase();
		tickModifiers();
		currentTransform().drawPhase();
		CostTable transform0 = currentTransform();
		actionPoints = transform0.startingAP();
		movePoints = transform0.startingM();
		reqFall = pather.getAirState().isAerial ? transform0.requiredFall() : 0;
		return true;
	}

	@Override
	public PathAction endPhase()
	{
		PathAction ac1 = currentTransform().endPhase();
		if(ac1 != null)
			return ac1;
		return super.endPhase();
	}

	@Override
	public boolean playerControlled()
	{
		return true;
	}

	@Override
	public boolean canBeAttacked()
	{
		return currentTransform().canBeAttacked();
	}

	@Override
	public List<Reaction> reactions(Therathic attackedBy, StatItem item, AttackType attackType, int distance)
	{
		return currentTransform().reactions(attackedBy, item, attackType, distance);
	}

	@Override
	public void takeAttack(Therathic attackedBy, StatItem item, AttackType attackType,
			int distance, boolean retaliated, boolean dodge, StatItem blockWith)
	{
		currentTransform().takeAttack(attackedBy, item, attackType, distance, retaliated, dodge, blockWith);
	}
}