package visual.pather;

import aer.*;
import aer.commands.*;
import com.jme3.scene.*;
import visual.*;
import visual.map.*;
import visual.mesh.*;

public class VisRelocatable extends AbstractVis<Relocatable>
{
	public Identifier id;
	private Node objectsNode;

	public VisRelocatable(Relocatable linked, Node objectsNode)
	{
		super(linked);
		this.objectsNode = objectsNode;
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
		geom.setMaterial(MeshLager.objectMat);
		//geom.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y));
		node1.attachChild(geom);
		node.setLocalTranslation(VisTiledMap.conv(linked.getLoc()));
		node.setLocalRotation(VisTiledMap.conv(linked.getDirection()));
		node.attachChild(node1);
		node.setName(id.toString());
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
			spatial.addControl(new MoveControl(1, (CMove) command));
		}
		else if(command instanceof CTurn)
		{
			System.out.println("CTURN");
			spatial.addControl(new TurnControl(1, (CTurn) command));
		}
		else if(command instanceof CMount)
		{
			System.out.println("CMOUNT");
			/*Vector3f wt = spatial.getWorldTranslation(); DOES NPT WORK
			Quaternion wr = spatial.getWorldRotation();
			objectsNode.depthFirstTraversal(e ->
			{
				if(((CMount) command).targetM.id.toString().equals(e.getName()))
				{
					((Node) e).attachChild(spatial);
					spatial.setLocalTranslation(wt.subtract(e.getWorldTranslation()));
					spatial.setLocalRotation(wr.subtract(e.getWorldRotation()));
				}
			});*/
			//spatial.addControl(new MoveControl(1, (CMove) command));
		}
		else if(command instanceof CDismount)
		{
			System.out.println("CDISMOUNT");
			/*Vector3f wt = spatial.getWorldTranslation();
			Quaternion wr = spatial.getWorldRotation();
			objectsNode.attachChild(spatial);
			spatial.setLocalTranslation(wt.subtract(objectsNode.getWorldTranslation()));
			spatial.setLocalRotation(wr.subtract(objectsNode.getWorldRotation()));*/
			//spatial.addControl(new TurnControl(1, (CTurn) command));
		}
	}
}