package visual.hud;

import com.jme3.font.*;
import com.jme3.scene.*;
import com.jme3.system.*;

public class PathInfoHUD extends VisHUD
{
	public PathInfoHUD(Node guiNode, BitmapFont guiFont, AppSettings settings)
	{
		super(guiNode, guiFont, settings);
	}

	@Override
	public void setup()
	{
		addNode(0);
		addNode(1);
		FWTLineCreate lineCreate = new FWTLineCreate(this, 1, TFieldWithText.H0,
				0.02f, 0.005f, 0.30f, 0.97f, 0.035f, 0.01f);
		lineCreate.create("AP");
		lineCreate.create("MP");
		lineCreate.create("Take", 2);
		lineCreate.create("Path", 5);
		lineCreate.create("Target");
		lineCreate.create("Options", 5);
	}
}