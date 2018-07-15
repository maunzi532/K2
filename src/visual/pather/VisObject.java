package visual.pather;

import aer.*;
import aer.commands.*;
import com.jme3.math.*;
import com.jme3.scene.*;
import visual.*;
import visual.map.*;

public class VisObject extends VisualR<HexObject>
{
	public VisObject(HexObject linked)
	{
		super(linked);
	}

	@Override
	public void setSpatial(Spatial spatial)
	{
		super.setSpatial(spatial);
		Node node1 = new Node();
		node1.setUserData("ID", linked.id);
		node1.setUserData("Target", true);
		HexLocation loc = linked.getLoc();
		Geometry geom = new Geometry(loc.toString(), Lager1.objectMesh);
		geom.setMaterial(Lager1.objectMat);
		geom.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y));
		node1.attachChild(geom);
		node.setLocalTranslation(VisHexMap.conv(loc));
		node.setLocalRotation(VisHexMap.conv(linked.getDirection()));
		node.attachChild(node1);
	}

	@Override
	public void execute(VisualCommand command)
	{
		if(command instanceof CMove)
		{
			System.out.println("CMOVE");
			spatial.addControl(new MoveControl(1, (CMove) command));
		}
		else if(command instanceof CTurn)
		{
			System.out.println("CTURN");
			spatial.addControl(new TurnControl(1, (CTurn) command));
		}
	}
}