package visual.hud;

import com.jme3.font.*;
import com.jme3.math.*;
import com.jme3.renderer.queue.*;
import com.jme3.scene.*;
import com.jme3.scene.shape.*;
import visual.mesh.*;

public class FieldWithText extends Node
{
	private final float x0, xa, x1, y0, ya, ym, textSize;
	BitmapText text;
	Geometry quad;

	public FieldWithText(VisHUD visHUD, int mode, String name, TFieldWithText c, float xr0, float xra, float xr1, float yr0, float yra, float ym)
	{
		super(name);
		visHUD.modes.get(mode).attachChild(this);
		switch(c)
		{
			case WH:
				x0 = xr0 * visHUD.settings.getWidth();
				xa = xra * visHUD.settings.getWidth();
				x1 = xr1 * visHUD.settings.getWidth();
				y0 = yr0 * visHUD.settings.getHeight();
				ya = yra * visHUD.settings.getHeight();
				break;
			case W0:
				x0 = xr0 * visHUD.settings.getWidth();
				xa = xra * visHUD.settings.getWidth();
				x1 = xr1 * visHUD.settings.getWidth();
				y0 = yr0 * visHUD.settings.getWidth();
				ya = yra * visHUD.settings.getWidth();
				break;
			case W1:
				x0 = xr0 * visHUD.settings.getWidth();
				xa = xra * visHUD.settings.getWidth();
				x1 = xr1 * visHUD.settings.getWidth();
				y0 = visHUD.settings.getHeight() - (1f - yr0) * visHUD.settings.getWidth();
				ya = visHUD.settings.getHeight() - (1f - yra) * visHUD.settings.getWidth();
				break;
			case H0:
				x0 = xr0 * visHUD.settings.getHeight();
				xa = xra * visHUD.settings.getHeight();
				x1 = xr1 * visHUD.settings.getHeight();
				y0 = yr0 * visHUD.settings.getHeight();
				ya = yra * visHUD.settings.getHeight();
				break;
			case H1:
				x0 = visHUD.settings.getWidth() - (1 - xr0) * visHUD.settings.getHeight();
				xa = visHUD.settings.getWidth() - (1 - xra) * visHUD.settings.getHeight();
				x1 = visHUD.settings.getWidth() - (1 - xr1) * visHUD.settings.getHeight();
				y0 = yr0 * visHUD.settings.getHeight();
				ya = yra * visHUD.settings.getHeight();
				break;
			default:
				throw new RuntimeException();
		}
		this.ym = ym;
		int defSize = visHUD.guiFont.getCharSet().getRenderedSize();
		float maxSize = ya * 0.75f;
		textSize = Math.min(defSize, maxSize);
		setQueueBucket(RenderQueue.Bucket.Gui);

		text = new BitmapText(visHUD.guiFont, false);
		text.setSize(textSize);
		text.setColor(ColorRGBA.Red);
		text.setText("");
		text.setLocalTranslation(xa, y0, 0);
		attachChild(text);

		quad = new Geometry("Quad", new Quad(x1 - x0, ya * ym));
		quad.setLocalTranslation(x0, y0 - (ya * ym), -0.1f);
		quad.setMaterial(MeshLager.blockedMat);
		attachChild(quad);
	}
}