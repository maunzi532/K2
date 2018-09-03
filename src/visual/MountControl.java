package visual;

import aer.*;
import aer.commands.*;
import com.jme3.math.*;
import com.jme3.renderer.*;
import com.jme3.scene.*;
import com.jme3.scene.control.*;
import visual.map.*;
import visual.pather.*;

public class MountControl extends AbstractControl
{
	private Identifier mountedToID;
	private Spatial mountedToNode;
	private HexDirection direction0;
	private Vector3f seatLoc;
	private Vector3f relativeStartLoc;
	private Vector3f relativeEndLoc;
	private Quaternion relativeStartDir;
	private Quaternion relativeEndDir;
	private float timeL;
	private float timeLeftL;
	private float timeD;
	private float timeLeftD;

	public MountControl(VisFinder visFinder, CMount cMount)
	{
		mountedToID = cMount.targetM.id;
		mountedToNode = visFinder.byIdentifier.get(mountedToID).node;
		direction0 = cMount.direction;
	}

	@Override
	public void setSpatial(Spatial spatial)
	{
		super.setSpatial(spatial);
		if(spatial == null)
			return;
		//set SeatLoc
		seatLoc = new Vector3f(0, 2, 0);
		setTimerL(new Vector3f(), 1f);
		setTimerD(direction0, 1f);
	}

	public Vector3f seatLocTranslated()
	{
		return mountedToNode.getLocalRotation().mult(seatLoc).add(mountedToNode.getLocalTranslation());
	}

	public void setTimerL(Vector3f relativeEndLoc0, float time)
	{
		relativeStartLoc = spatial.getLocalTranslation().subtract(seatLocTranslated());
		relativeEndLoc = relativeEndLoc0;
		timeL = time;
		timeLeftL = time;
	}

	public void setTimerD(Quaternion relativeEndDir0, float time)
	{
		relativeStartDir = spatial.getLocalRotation().subtract(mountedToNode.getLocalRotation());
		relativeEndDir = relativeEndDir0;
		timeD = time;
		timeLeftD = time;
	}

	public void setTimerD(HexDirection direction, float time)
	{
		setTimerD(VisTiledMap.conv(direction).subtract(mountedToNode.getLocalRotation()), time);
	}

	@Override
	protected void controlUpdate(float tpf)
	{
		if(spatial == null)
			return;
		timeLeftL -= tpf;
		if(timeLeftL <= 0)
		{
			spatial.setLocalTranslation(seatLocTranslated().add(relativeEndLoc));
			timeLeftL = 0;
		}
		else
		{
			float timeL2 = timeLeftL / timeL;
			spatial.setLocalTranslation(relativeStartLoc.mult(timeL2)
					.addLocal(relativeEndLoc.mult(1 - timeL2)).addLocal(seatLocTranslated()));
		}

		timeLeftD -= tpf;
		if(timeLeftD <= 0)
		{
			spatial.setLocalRotation(mountedToNode.getLocalRotation().add(relativeEndDir));
			timeLeftD = 0;
		}
		else
		{
			float timeD2 = timeLeftD / timeD;
			spatial.setLocalRotation(relativeStartDir.mult(timeD2)
					.addLocal(relativeEndDir.mult(1 - timeD2)).addLocal(mountedToNode.getLocalRotation()));
		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp){}
}