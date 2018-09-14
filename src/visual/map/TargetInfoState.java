package visual.map;

import aer.*;
import com.jme3.app.*;
import com.jme3.app.state.*;
import com.jme3.font.*;
import com.jme3.scene.*;
import com.jme3.system.*;
import visual.hud.*;

public class TargetInfoState extends BaseAppState
{
	private Node guiNode;
	private BitmapFont font;
	private AppSettings settings;
	private TargetInfoHUD targetInfoHUD;
	private Targeting targeting;
	private ITiledMap map;

	public TargetInfoState(Node guiNode, BitmapFont font, AppSettings settings, Targeting targeting, ITiledMap map)
	{
		this.guiNode = guiNode;
		this.font = font;
		this.settings = settings;
		this.targeting = targeting;
		this.map = map;
	}

	@Override
	protected void initialize(Application app)
	{
		targetInfoHUD = new TargetInfoHUD(guiNode, font, settings);
	}

	@Override
	protected void cleanup(Application app){}

	@Override
	protected void onEnable(){}

	@Override
	protected void onDisable(){}

	@Override
	public void update(float tpf)
	{
		if(targeting.updated())
		{
			if(targeting.targetObject() != null)
			{
				//targetInfoHUD.updateText();
				targetInfoHUD.changeMode(2);
			}
			else if(targeting.targetTile() != null)
			{
				MapTile mapTile = map.getTile(targeting.targetTile());
				targetInfoHUD.updateText(1, "FloorType", mapTile.type.toString());
				targetInfoHUD.changeMode(1);
			}
			else
			{
				targetInfoHUD.changeMode(0);
			}
		}
	}
}