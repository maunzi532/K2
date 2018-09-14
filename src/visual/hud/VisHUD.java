package visual.hud;

import com.jme3.font.*;
import com.jme3.math.*;
import com.jme3.renderer.queue.*;
import com.jme3.scene.*;
import com.jme3.system.*;
import java.util.*;

public abstract class VisHUD
{
	public Node guiNode;
	public BitmapFont guiFont;
	public AppSettings settings;
	public int currentMode;
	public HashMap<Integer, Node> modes;

	public VisHUD(Node guiNode, BitmapFont guiFont, AppSettings settings)
	{
		this.guiNode = guiNode;
		this.guiFont = guiFont;
		this.settings = settings;
		modes = new HashMap<>();
		setup();
		currentMode = 0;
		modes.get(0).setCullHint(Spatial.CullHint.Inherit);
	}

	public abstract void setup();

	protected Node addNode(int mode)
	{
		Node node = new Node();
		node.setCullHint(Spatial.CullHint.Always);
		modes.put(mode, node);
		guiNode.attachChild(node);
		return node;
	}

	protected void createText(Node node, String name, float xs, float ys, boolean u)
	{
		BitmapText text = new BitmapText(guiFont, false);
		text.setName(name);
		text.setQueueBucket(RenderQueue.Bucket.Gui);
		text.setSize(guiFont.getCharSet().getRenderedSize());
		text.setColor(ColorRGBA.Red);
		text.setText("");
		text.setLocalTranslation(xs * settings.getWidth(),
				ys * settings.getHeight() + (u ? text.getHeight() : 0), 0);
		node.attachChild(text);
	}

	public void changeMode(int newMode)
	{
		if(newMode != currentMode)
		{
			modes.get(currentMode).setCullHint(Spatial.CullHint.Always);
			modes.get(newMode).setCullHint(Spatial.CullHint.Inherit);
			currentMode = newMode;
		}
	}

	public void updateText(int mode, String name, String newText)
	{
		((BitmapText) modes.get(mode).getChild(name)).setText(newText);
	}
}