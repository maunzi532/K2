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
		new FieldWithText(this, 1, "AP", TFieldWithText.W1, 0.02f, 0.025f, 0.20f, 0.97f, 0.99f);
		new FieldWithText(this, 1, "MP", TFieldWithText.W1, 0.02f, 0.025f, 0.20f, 0.94f, 0.96f);
		new FieldWithText(this, 1, "Take", TFieldWithText.W1, 0.02f, 0.025f, 0.20f, 0.91f, 0.93f);
		new FieldWithText(this, 1, "Path", TFieldWithText.W1, 0.02f, 0.025f, 0.20f, 0.76f, 0.90f);
		new FieldWithText(this, 1, "Target", TFieldWithText.W1, 0.02f, 0.025f, 0.20f, 0.73f, 0.75f);
		new FieldWithText(this, 1, "Options", TFieldWithText.W1, 0.02f, 0.025f, 0.20f, 0.58f, 0.72f);
	}
}