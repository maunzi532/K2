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
		{
			Node node = addNode(1);
			createText(node, "AP", 0.02f, 0.98f, false);
			createText(node, "MP", 0.02f, 0.94f, false);
			createText(node, "Take", 0.02f, 0.90f, false);
			createText(node, "Path", 0.02f, 0.86f, false);
			createText(node, "Target", 0.02f, 0.60f, false);
			createText(node, "Options", 0.02f, 0.56f, false);
		}
	}
}