package aer.resource3;

import aer.path.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.resource2.*;
import aer.resource2.otheractions.*;
import aer.resource2.therathicTypes.*;
import java.util.*;

public class TX_AP_Transform implements TherathicHex, E_AP_MP
{
	private HexPather pather;
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
		transformInto(transform0);
		modifiers = new ArrayList<>();
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
	public void linkTo(HexPather pather)
	{
		this.pather = pather;
	}

	@Override
	public HexPather pather()
	{
		return pather;
	}

	@Override
	public List<HexItem> activeItems()
	{
		return currentTransform().activeItems();
	}

	@Override
	public List<HexItem> interruptItems(TargetData targetData)
	{
		return currentTransform().interruptItems(targetData);
	}

	@Override
	public List<EndHexItem> endItems()
	{
		return currentTransform().endItems();
	}

	@Override
	public TakeableAction startAction()
	{
		return new InitAction2(costTable);
	}

	@Override
	public ActionResource actionResource()
	{
		return new BasicAPResource2(actionPoints, movePoints, pather.getDirection(), pather.getAirState(), reqFall, pather.getLoc(), pather.getMount());
	}

	@Override
	public boolean drawPhase()
	{
		actionPoints = costTable.startingAP;
		movePoints = costTable.startingM;
		reqFall = pather.getAirState().isAerial ? costTable.requiredFall : 0;
		currentTransform().drawPhase();
		return true;
	}

	@Override
	public PathAction endPhase()
	{
		tickModifiers();
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