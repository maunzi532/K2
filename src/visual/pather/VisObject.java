package visual.pather;

import aer.*;
import aer.commands.*;
import com.jme3.material.*;
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
		YHexoMesh hexoMesh1 = new YHexoMesh(Scale.X_HEX_RADIUS * 0.5f, 0f, Scale.CELLAR_HEIGHT / 4f, 3);
		Material matFloor = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		matFloor.setBoolean("UseMaterialColors",true);
		matFloor.setColor("Diffuse", ColorRGBA.Red);
		matFloor.setColor("Ambient", ColorRGBA.Red);

		Node node1 = new Node();
		node1.setUserData("ID", linked.id);
		node1.setUserData("Target", true);
		HexLocation loc = linked.getLoc();
		Geometry geom = new Geometry(loc.toString(), hexoMesh1);
		geom.setMaterial(matFloor);
		geom.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y));
		node1.attachChild(geom);
		node1.setLocalTranslation(VisHexMap.conv(loc));
		node1.setLocalRotation(VisHexMap.conv(linked.getDirection()));
		node.attachChild(node1);
	}

	@Override
	public void execute(VisualCommand command)
	{
		if(command instanceof CMove)
		{
			System.out.println("CMOVE");
		}
		else if(command instanceof CTurn)
		{
			System.out.println("CTURN");
		}
	}
}