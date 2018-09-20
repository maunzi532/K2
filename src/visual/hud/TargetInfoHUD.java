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
		new FieldWithText(this, 1, "FloorType", TFieldWithText.W1, 0.72f, 0.725f, 0.86f, 0.97f, 0.99f);
		addNode(2);
		new FieldWithText(this, 2, "Name", TFieldWithText.W1, 0.72f, 0.725f, 0.86f, 0.97f, 0.99f);
		new FieldWithText(this, 2, "AirState", TFieldWithText.W1, 0.72f, 0.725f, 0.86f, 0.93f, 0.95f);
		new FieldWithText(this, 2, "MountedTo", TFieldWithText.W1, 0.72f, 0.725f, 0.86f, 0.89f, 0.91f);
		new FieldWithText(this, 2, "PlayerControlled", TFieldWithText.W1, 0.87f, 0.875f, 0.99f, 0.97f, 0.99f);
		new FieldWithText(this, 2, "Team", TFieldWithText.W1, 0.87f, 0.875f, 0.99f, 0.93f, 0.95f);
		new FieldWithText(this, 2, "AP", TFieldWithText.W1, 0.87f, 0.875f, 0.99f, 0.89f, 0.91f);
		new FieldWithText(this, 2, "MP", TFieldWithText.W1, 0.87f, 0.875f, 0.99f, 0.85f, 0.87f);
		new FieldWithText(this, 2, "RequiredFall", TFieldWithText.W1, 0.87f, 0.875f, 0.99f, 0.81f, 0.83f);
		new FieldWithText(this, 2, "Health", TFieldWithText.W1, 0.87f, 0.875f, 0.99f, 0.77f, 0.79f);
		new FieldWithText(this, 2, "Active", TFieldWithText.W1, 0.87f, 0.875f, 0.99f, 0.73f, 0.75f);
		new FieldWithText(this, 2, "Lives", TFieldWithText.W1, 0.87f, 0.875f, 0.99f, 0.69f, 0.71f);
	}
}