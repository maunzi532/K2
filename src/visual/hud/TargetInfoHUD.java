package visual.hud;

import com.jme3.font.*;
import com.jme3.scene.*;
import com.jme3.system.*;

public class TargetInfoHUD extends VisHUD
{
	public TargetInfoHUD(Node guiNode, BitmapFont guiFont, AppSettings settings)
	{
		super(guiNode, guiFont, settings);
	}

	@Override
	public void setup()
	{
		addNode(0);
		addNode(1);
		new FieldWithText(this, 1, "FloorType", TFieldWithText.H1, 0.43f, 0.435f, 0.70f, 0.97f, 0.035f, 1);
		addNode(2);
		FWTLineCreate lineCreate = new FWTLineCreate(this, 2, TFieldWithText.H1,
				0.43f, 0.005f, 0.70f, 0.97f, 0.035f, 0.01f);
		lineCreate.create("Name");
		lineCreate.create("AirState");
		lineCreate.create("MountedTo");
		lineCreate.reset(0.715f, 0.985f);
		lineCreate.create("PlayerControlled");
		lineCreate.create("Team");
		lineCreate.create("AP");
		lineCreate.create("MP");
		lineCreate.create("RequiredFall");
		lineCreate.create("Health");
		lineCreate.create("Active");
		lineCreate.create("Lives");
	}
}