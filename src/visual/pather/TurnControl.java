package visual.pather;

import aer.commands.*;
import com.jme3.math.*;
import com.jme3.renderer.*;
import com.jme3.scene.*;
import com.jme3.scene.control.*;
import visual.map.*;

public class TurnControl extends AbstractControl
{
	private CTurn cTurn;
	private Quaternion localStart;
	private Quaternion localEnd;
	private float time;
	private float timeLeft;

	public TurnControl(float time, CTurn cTurn)
	{
		this.cTurn = cTurn;
		this.time = time;
		timeLeft = time;
		localEnd = VisTiledMap.conv(cTurn.targetD);
	}

	@Override
	public void setSpatial(Spatial spatial)
	{
		super.setSpatial(spatial);
		if(spatial == null)
			return;
		localStart = spatial.getLocalRotation().clone();
	}

	@Override
	protected void controlUpdate(float tpf)
	{
		timeLeft -= tpf;
		if(timeLeft <= 0)
		{
			spatial.setLocalRotation(localEnd);
			spatial.removeControl(this);
			return;
		}
		float time2 = timeLeft / time;
		spatial.setLocalRotation(localStart.mult(time2).addLocal(localEnd.mult(1 - time2)).normalizeLocal());
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp){}
}