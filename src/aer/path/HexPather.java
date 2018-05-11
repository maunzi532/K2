package aer.path;

import aer.*;
import aer.path.team.*;
import java.util.*;

public class HexPather extends HexObject
{
	private TherathicHex therathicHex;
	private HexObject mount;
	private List<PathAction> possiblePaths;

	public HexPather(int id, IHexMap map, HexLocation loc, HexDirection direction, AirState airState, TherathicHex therathicHex)
	{
		super(id, map, loc, direction, airState);
		this.therathicHex = therathicHex;
		therathicHex.linkTo(this);
	}

	public TherathicHex getTherathicHex()
	{
		return therathicHex;
	}

	@Override
	public HexLocation getLoc()
	{
		if(mount != null)
			return mount.getLoc();
		return super.getLoc();
	}

	@Override
	public void setLoc(HexLocation loc)
	{
		if(mount != null)
		{
			mount.setLoc(loc);
			return;
		}
		super.setLoc(loc);
	}

	@Override
	public HexDirection getDirection()
	{
		if(mount != null)
			return HexDirection.plus(direction, mount.getDirection());
		return super.getDirection();
	}

	@Override
	public void setDirection(HexDirection direction)
	{
		if(mount != null)
			this.direction = HexDirection.minus(direction, mount.getDirection());
		super.setDirection(direction);
	}

	@Override
	public AirState getAirState()
	{
		if(mount != null)
			return AirState.MOUNT;
		return super.getAirState();
	}

	public HexObject getMount()
	{
		return mount;
	}

	public void setMount(HexObject mount)
	{
		if(mount == null)
		{
			loc = this.mount.getLoc();
			direction = HexDirection.plus(direction, this.mount.getDirection());
			this.mount = null;
		}
		else
		{
			this.mount = mount;
			direction = HexDirection.minus(direction, this.mount.getDirection());
		}
	}

	public List<PathAction> getPossiblePaths()
	{
		return possiblePaths;
	}

	public void resetPossiblePaths()
	{
		possiblePaths = null;
	}

	public void calculatePossiblePaths(ItemGetType type, TargetData targetData)
	{
		/*possiblePaths = new ArrayList<>();
		PathAction start = new PathAction(this, therathicHex.actionResource(), therathicHex.startAction(type));
		if(!start.deducted.okay())
			return;
		possiblePaths.add(start);
		for(int i = 0; i < possiblePaths.size(); i++)
		{
			if(i > 5000)
			{
				possiblePaths.forEach(e -> System.out.println(e.deducted));
				throw new RuntimeException("Pathcount > 5000");
			}
			PathAction currentPath = possiblePaths.get(i);
			for(HexItem item : therathicHex.activeItems(type, targetData))
			{
				currentPath.next.addAll(item.takeableActions(currentPath).stream()
						.map(action -> new PathAction(this, currentPath.deducted, action, currentPath))
						.filter(path -> path.deducted.okay()).collect(Collectors.toList()));
			}
			possiblePaths.addAll(currentPath.next);
		}*/
		switch(type)
		{
			case ACTION:
				possiblePaths = therathicHex.possibleActivePaths();
				break;
			case INTERRUPT:
				possiblePaths = therathicHex.possibleInterrupts(targetData);
				break;
			case END:
				possiblePaths = Collections.singletonList(therathicHex.endPath());
				break;
		}
	}

	@Override
	public String name()
	{
		return "HexPather ID " + id;
	}
}