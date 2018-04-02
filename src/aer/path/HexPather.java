package aer.path;

import aer.*;
import aer.path.team.*;
import java.util.*;
import java.util.stream.*;

public class HexPather extends HexObject
{
	private TherathicHex therathicHex;
	private HexObject mount;
	private List<PathAction> possiblePaths;

	public HexPather(IHexMap map, HexLocation loc, HexDirection direction, AirState airState, TherathicHex therathicHex)
	{
		super(map, loc, direction, airState);
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

	public void calculatePossiblePaths(TherathicHex.ItemGetType type, HexPather target)
	{
		possiblePaths = new ArrayList<>();
		PathAction start = new PathAction(this, therathicHex.actionResource(),
				therathicHex.startAction(type, target));
		if(!start.deducted.okay())
			return;
		possiblePaths.add(start);
		for(int i = 0; i < possiblePaths.size(); i++)
		{
			if(i > 5000)
			{
				possiblePaths.forEach(e -> System.out.println(e.deducted));
				throw new RuntimeException("Pathcount probably infinite");
			}
			PathAction currentPath = possiblePaths.get(i);
			for(HexItem item : therathicHex.activeItems(type, target))
			{
				currentPath.next.addAll(item.takeableActions(currentPath).stream()
						.map(action -> new PathAction(this, currentPath.deducted, action, currentPath))
						.filter(path -> path.deducted.okay()).collect(Collectors.toList()));
			}
			possiblePaths.addAll(currentPath.next);
		}
	}
}