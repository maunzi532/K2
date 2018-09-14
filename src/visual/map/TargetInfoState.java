package visual.map;

import aer.*;
import aer.path.*;
import aer.path.team.*;
import aer.resource2.resource.*;
import aer.resource3.*;
import aer.resource3.resource4.*;
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
		//if(targeting.updated())
		{
			if(targeting.targetObject() != null)
			{
				Relocatable relocatable = targeting.targetObject();
				targetInfoHUD.updateText(2, "Name", relocatable.name());
				targetInfoHUD.updateText(2, "AirState", relocatable.getAirState().toString());
				targetInfoHUD.updateText(2, "MountedTo", relocatable.getMountedTo() != null ? "MT: " + relocatable.getMountedTo().name() : "");
				if(relocatable instanceof Pather)
				{
					Therathic therathic = ((Pather) relocatable).getTherathic();
					targetInfoHUD.updateText(2, "PlayerControlled", therathic.playerControlled() ? "Player" : "NPC");
					targetInfoHUD.updateText(2, "Team", "Team: " + therathic.teamSide());
					if(therathic instanceof TX_AP_Transform)
					{
						Resource_AP_MP resource_ap_mp = (Resource_AP_MP) therathic.actionResource();
						targetInfoHUD.updateText(2, "AP", "AP: " + String.valueOf(resource_ap_mp.dActionPoints()));
						targetInfoHUD.updateText(2, "MP", "MP: " + String.valueOf(resource_ap_mp.dMovementPoints()));
						targetInfoHUD.updateText(2, "RequiredFall",
								resource_ap_mp.dRequiredFall() > 0 ? "RQF: " + String.valueOf(resource_ap_mp.dRequiredFall()) : "");
						Transformation transformation = ((TX_AP_Transform) therathic).currentTransform();
						if(transformation instanceof Equipable)
						{
							Equipable equipable = (Equipable) transformation;
							targetInfoHUD.updateText(2, "Health", "H: " + String.valueOf(equipable.health));
							targetInfoHUD.updateText(2, "Active", equipable.active ? "Active" : "Inactive");
							targetInfoHUD.updateText(2, "Lives", "L: " + String.valueOf(equipable.lives));
						}
						else
						{
							targetInfoHUD.updateText(2, "Health", "");
							targetInfoHUD.updateText(2, "Active", "");
							targetInfoHUD.updateText(2, "Lives", "");
						}
					}
					else
					{
						targetInfoHUD.updateText(2, "AP", "");
						targetInfoHUD.updateText(2, "MP", "");
						targetInfoHUD.updateText(2, "RequiredFall", "");
						targetInfoHUD.updateText(2, "Health", "");
						targetInfoHUD.updateText(2, "Active", "");
						targetInfoHUD.updateText(2, "Lives", "");
					}
				}
				else
				{
					targetInfoHUD.updateText(2, "PlayerControlled", "");
					targetInfoHUD.updateText(2, "Team", "");
					targetInfoHUD.updateText(2, "AP", "");
					targetInfoHUD.updateText(2, "MP", "");
					targetInfoHUD.updateText(2, "RequiredFall", "");
					targetInfoHUD.updateText(2, "Health", "");
					targetInfoHUD.updateText(2, "Active", "");
					targetInfoHUD.updateText(2, "Lives", "");
				}
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