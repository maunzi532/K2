package visual.map;

import com.jme3.font.*;
import com.jme3.math.*;
import com.jme3.renderer.queue.*;
import com.jme3.scene.*;
import com.jme3.system.*;
import java.util.*;

public class VisHUD
{
	public Node guiNode;
	public BitmapFont guiFont;
	public AppSettings settings;
	public HUDMode currentMode;
	public HashMap<HUDMode, Node> modes;

	public VisHUD(Node guiNode, BitmapFont guiFont, AppSettings settings)
	{
		this.guiNode = guiNode;
		this.guiFont = guiFont;
		this.settings = settings;
		modes = new HashMap<>();
		setup();
		currentMode = HUDMode.NONE;
		modes.get(HUDMode.NONE).setCullHint(Spatial.CullHint.Inherit);

		/*BitmapText t0 = new BitmapText(guiFont, false);
		t0.setQueueBucket(RenderQueue.Bucket.Gui);
		t0.setSize(guiFont.getCharSet().getRenderedSize());
		t0.setColor(ColorRGBA.White);
		t0.setText("Wugu\nW");
		t0.setLocalTranslation(300, t0.getHeight(), 0);
		guiNode.attachChild(t0);*/
	}

	public void setup()
	{
		{
			Node node = addNode(HUDMode.NONE);
		}
		{
			Node node = addNode(HUDMode.ACTION);
			createText(node, "AP", "AP", true, 0.98f, 0.02f, true, true);
			createText(node, "MP", "Movement", true, 0.98f, 0.06f, true, true);
		}
	}

	private Node addNode(HUDMode mode)
	{
		Node node = new Node();
		node.setCullHint(Spatial.CullHint.Always);
		modes.put(mode, node);
		guiNode.attachChild(node);
		return node;
	}

	private void createText(Node node, String name, String initialContent, boolean bg, float xs, float ys, boolean l, boolean u)
	{
		BitmapText text = new BitmapText(guiFont, false);
		text.setName(name);
		text.setQueueBucket(RenderQueue.Bucket.Gui);
		text.setSize(guiFont.getCharSet().getRenderedSize());
		text.setColor(ColorRGBA.Red);
		text.setText(initialContent);
		text.setLocalTranslation(xs * settings.getWidth() - (l ? text.getLineWidth() : 0),
				ys * settings.getHeight() + (u ? text.getHeight() : 0), 0);
		node.attachChild(text);
	}

	public void changeMode(HUDMode newMode)
	{
		if(newMode != currentMode)
		{
			modes.get(currentMode).setCullHint(Spatial.CullHint.Always);
			modes.get(newMode).setCullHint(Spatial.CullHint.Inherit);
			currentMode = newMode;
		}
	}

	public void updateText(HUDMode mode, String name, String newText)
	{
		((BitmapText) modes.get(mode).getChild(name)).setText(newText);
	}
}