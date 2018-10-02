package visual.pather;

import aer.*;
import aer.commands.*;
import aer.path.*;
import com.jme3.scene.*;
import visual.*;
import visual.map.*;
import visual.mesh.*;

public class VisRelocatable extends AbstractVis<Relocatable>
{
	public Identifier id;
	private VisFinder visFinder;

	public VisRelocatable(Relocatable linked, VisFinder visFinder)
	{
		super(linked);
		this.visFinder = visFinder;
		id = linked.id;
	}

	@Override
	public void setSpatial(Spatial spatial)
	{
		super.setSpatial(spatial);
		Node node1 = new Node();
		node1.setUserData("ID", id.toString());
		node1.setUserData("Target", true);
		Geometry geom = new Geometry(id.toString(), MeshLager.objectMesh);
		geom.setMaterial(MeshLager.objectMat[colorNum(linked)]);
		//geom.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y));
		node1.attachChild(geom);
		node.setLocalTranslation(VisTiledMap.conv(linked.getLoc()));
		node.setLocalRotation(VisTiledMap.conv(linked.getDirection()));
		node.attachChild(node1);
		node.setName(id.toString());
	}

	private int colorNum(Relocatable relocatable)
	{
		if(relocatable instanceof Pather)
			return ((Pather) relocatable).getTherathic().teamSide() + 1;
		return 0;
	}

	public void replaceLink(Relocatable linkedNew)
	{
		linked = linkedNew;
		if(linked == null)
		{
			//Vanish
		}
		else
		{
			commands.add(new CMove(linked));
			commands.add(new CTurn(linked));
		}
	}

	@Override
	public void execute(ICommand command)
	{
		if(command instanceof CMove)
		{
			System.out.println("CMOVE");
			spatial.addControl(new MoveControl(1f, (CMove) command));
		}
		else if(command instanceof CTurn)
		{
			System.out.println("CTURN");
			if(spatial.getControl(MountControl.class) != null)
				spatial.getControl(MountControl.class).setTimerD(((CTurn) command).targetD, 1f);
			else
				spatial.addControl(new TurnControl(1f, (CTurn) command));
		}
		else if(command instanceof CMount)
		{
			System.out.println("CMOUNT");
			spatial.addControl(new MountControl(visFinder, (CMount) command));
		}
		else if(command instanceof CDismount)
		{
			System.out.println("CDISMOUNT");
			spatial.removeControl(MountControl.class);
		}
	}
}