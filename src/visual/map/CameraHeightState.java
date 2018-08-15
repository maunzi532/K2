package visual.map;

import com.jme3.app.*;
import com.jme3.app.state.*;
import com.jme3.input.*;
import com.jme3.input.controls.*;
import com.jme3.renderer.*;

public class CameraHeightState extends BaseAppState implements ActionListener
{
	private static final int STEPS = 20;

	public Camera camera;
	public int heightLevel = 0;
	private int heightInSteps = 0;

	@Override
	protected void initialize(Application app)
	{
		app.getInputManager().addMapping("HeightLevelUp", new KeyTrigger(KeyInput.KEY_R));
		app.getInputManager().addMapping("HeightLevelDown", new KeyTrigger(KeyInput.KEY_F));
		app.getInputManager().addListener(this, "HeightLevelUp", "HeightLevelDown");
		camera = app.getCamera();
	}

	@Override
	protected void cleanup(Application app){}

	@Override
	protected void onEnable(){}

	@Override
	protected void onDisable(){}

	@Override
	public void onAction(String name, boolean isPressed, float tpf)
	{
		if(isPressed)
		{
			switch(name)
			{
				case "HeightLevelUp":
					heightLevel++;
					break;
				case "HeightLevelDown":
					heightLevel--;
					break;
			}
		}
	}

	@Override
	public void update(float tpf)
	{
		if(heightInSteps < heightLevel * STEPS)
		{
			camera.setLocation(camera.getLocation().add(0, Scale.CELLAR_HEIGHT / STEPS, 0));
			heightInSteps++;
		}
		else if(heightInSteps > heightLevel * STEPS)
		{
			camera.setLocation(camera.getLocation().add(0, -Scale.CELLAR_HEIGHT / STEPS, 0));
			heightInSteps--;
		}
	}
}