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
		{
			Node node = addNode(1);
			createText(node, "FloorType", 0.72f, 0.98f, false);
		}
		{
			Node node = addNode(2);
			createText(node, "Name", 0.72f, 0.98f, false);
			createText(node, "AirState", 0.72f, 0.94f, false);
			createText(node, "MountedTo", 0.72f, 0.90f, false);
			createText(node, "PlayerControlled", 0.87f, 0.98f, false);
			createText(node, "Team", 0.87f, 0.94f, false);
			createText(node, "AP", 0.87f, 0.90f, false);
			createText(node, "MP", 0.87f, 0.86f, false);
			createText(node, "RequiredFall", 0.87f, 0.82f, false);
			createText(node, "Health", 0.87f, 0.78f, false);
			createText(node, "Active", 0.87f, 0.74f, false);
			createText(node, "Lives", 0.87f, 0.70f, false);
		}
	}
}