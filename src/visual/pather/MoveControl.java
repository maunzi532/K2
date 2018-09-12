package visual.pather;

import aer.commands.*;
import com.jme3.math.*;
import com.jme3.renderer.*;
import com.jme3.scene.*;
import com.jme3.scene.control.*;
import visual.map.*;

public class MoveControl extends AbstractControl
{
	private CMove cMove;
	private Vector3f localStart;
	private Vector3f localEnd;
	private float time;
	private float timeLeft;

	public MoveControl(float time, CMove cMove)
	{
		this.cMove = cMove;
		this.time = time;
		timeLeft = time;
		localEnd = VisTiledMap.conv(cMove.targetL);
	}

	@Override
	public void setSpatial(Spatial spatial)
	{
		super.setSpatial(spatial);
		if(spatial == null)
			return;
		localStart = spatial.getLocalTranslation().clone();
	}

	@Override
	protected void controlUpdate(float tpf)
	{
		if(spatial.getControl(MountControl.class) != null)
		{
			spatial.removeControl(this);
			return;
		}
		timeLeft -= tpf;
		if(timeLeft <= 0)
		{
			spatial.setLocalTranslation(localEnd);
			spatial.removeControl(this);
			return;
		}
		float time2 = timeLeft / time;
		spatial.setLocalTranslation(localStart.mult(time2).addLocal(localEnd.mult(1 - time2)));
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp){}
}