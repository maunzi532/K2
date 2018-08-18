package aer.resource3;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.resource2.resource.*;
import aer.resource2.therathicType.*;
import java.util.*;

public class TX_AP_Transform implements Therathic, E_AP_MP
{
	private Pather pather;
	private List<Transformation> transforms;
	private CostTable costTable;
	private NPC_Control npc_control;
	private int actionPoints;
	private int movePoints;
	private int reqFall;
	public List<AppliedModifier> modifiers;

	public TX_AP_Transform(Transformation transform0)
	{
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
		costTable = transformation.costTable;
		if(pather != null)
			pather.updateMountSlots(transformation.transformKeepMounted(), transformation.mountSlotInfo());
	}

	public void endTransformation(Transformation transformation)
	{
		if(!transforms.remove(transformation))
			throw new RuntimeException();
		costTable = currentTransform().costTable;
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
		this.pather = pather;
		pather.updateMountSlots(currentTransform().transformKeepMounted(), currentTransform().mountSlotInfo());
	}

	@Override
	public Pather pather()
	{
		return pather;
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
		return new InitAction(costTable);
	}

	@Override
	public ActionResource actionResource()
	{
		return new Resource_AP_MP(actionPoints, movePoints, pather.getDirection(), pather.getAirState(), reqFall, pather.getLoc(), pather.getMountedTo(), pather.getMountedToSlot());
	}

	@Override
	public boolean drawPhase()
	{
		tickModifiers();
		actionPoints = costTable.startingAP;
		movePoints = costTable.startingM;
		reqFall = pather.getAirState().isAerial ? costTable.requiredFall : 0;
		currentTransform().drawPhase();
		return true;
	}

	@Override
	public PathAction endPhase()
	{
		PathAction ac1 = currentTransform().endPhase();
		if(ac1 != null)
			return ac1;
		return endPath();
	}

	@Override
	public int teamSide()
	{
		return 0;
	}

	@Override
	public boolean playerControlled()
	{
		return true;
	}

	@Override
	public NPC_Control npcControl()
	{
		return npc_control;
	}

	@Override
	public boolean useAP(int amount, Use use)
	{
		if(use == Use.DRAIN)
		{
			actionPoints = Math.max(actionPoints - amount, 0);
			return true;
		}
		boolean ok = actionPoints >= amount;
		if(ok && use == Use.REAL)
			actionPoints -= amount;
		return ok;
	}

	@Override
	public boolean useMP(int amount, Use use)
	{
		if(use == Use.DRAIN)
		{
			movePoints = Math.max(movePoints - amount, 0);
			return true;
		}
		boolean ok = movePoints >= amount;
		if(ok && use == Use.REAL)
			movePoints -= amount;
		return ok;
	}
}