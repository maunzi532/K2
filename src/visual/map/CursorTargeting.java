package visual.map;

import aer.*;
import com.jme3.app.*;
import com.jme3.app.state.*;
import com.jme3.collision.*;
import com.jme3.input.*;
import com.jme3.input.controls.*;
import com.jme3.math.*;
import com.jme3.renderer.*;
import com.jme3.scene.Node;
import java.util.*;
import java.util.stream.*;

public class CursorTargeting extends BaseAppState implements Targeting, ActionListener
{
	private Node mainNode;
	private Camera cam;
	private InputManager inputManager;
	private Node nodeTile;
	private Node nodeObject;
	private boolean updated;
	private ITiledMap map;
	private Input1 input1;
	private HexLocation targetTile;
	private Relocatable targetObject;

	public CursorTargeting(ITiledMap map)
	{
		this.map = map;
	}

	@Override
	protected void initialize(Application app)
	{
		mainNode = ((SimpleApplication) app).getRootNode();
		cam = app.getCamera();
		inputManager = app.getInputManager();
		/*Geometry q = new Geometry("W");
		q.setMesh(new Quad(6, 6));
		q.setMaterial(new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md"));
		q.setLocalTranslation(app.getContext().getSettings().getWidth() / 2f, app.getContext().getSettings().getHeight() / 2f, 0);
		((SimpleApplication) app).getGuiNode().attachChild(q);*/
		inputManager.addMapping("TARGET", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addMapping("CHOOSE", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addMapping("CHANGE", new KeyTrigger(KeyInput.KEY_TAB));
		inputManager.addMapping("ACCEPT", new KeyTrigger(KeyInput.KEY_RETURN));
		inputManager.addMapping("BACK", new KeyTrigger(KeyInput.KEY_BACK));
		inputManager.addMapping("ESCAPE", new KeyTrigger(KeyInput.KEY_RSHIFT));
		inputManager.addMapping("MINUSD", new KeyTrigger(KeyInput.KEY_M));
		inputManager.addMapping("PLUSD", new KeyTrigger(KeyInput.KEY_N));
	}

	@Override
	protected void cleanup(Application app)
	{

	}

	@Override
	protected void onEnable()
	{
		inputManager.addListener(this, Arrays.stream(Input1.values()).map(Input1::name)
				.collect(Collectors.toList()).toArray(new String[Input1.values().length]));
	}

	@Override
	protected void onDisable()
	{

	}

	@Override
	public void update(float tpf)
	{
		//System.out.println(inputManager.getCursorPosition());
		Vector3f origin    = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.0f);
		Vector3f direction = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.3f);
		direction.subtractLocal(origin).normalizeLocal();

		Ray ray = new Ray(origin, direction);
		//Ray ray = new Ray(cam.getLocation(), cam.getDirection());
		CollisionResults results = new CollisionResults();
		mainNode.collideWith(ray, results);
		Node newTile = null;
		Node newObject = null;
		for(CollisionResult result : results)
		{
			if(newTile == null && result.getGeometry().getParent().getUserData("Tile") != null)
			{
				newTile = result.getGeometry().getParent();
			}
			if(newObject == null && result.getGeometry().getParent().getUserData("Target") != null)
			{
				newObject = result.getGeometry().getParent();
			}
		}
		if(newTile != nodeTile)
		{
			nodeTile = newTile;
			if(nodeTile != null)
			{
				if(nodeTile.getUserData("H") != null)
					targetTile = new HexLocation(nodeTile.getUserData("X"), nodeTile.getUserData("D"),
							nodeTile.getUserData("H"), nodeTile.getUserData("R"));
				else
					targetTile = new HexLocation(nodeTile.getUserData("X"), nodeTile.getUserData("D"),
							nodeTile.getParent().getUserData("H"), nodeTile.getUserData("R"));
			}
			else
				targetTile = null;
			updated = true;
		}
		if(newObject != nodeObject)
		{
			nodeObject = newObject;
			if(nodeObject != null)
			{
				targetObject = map.objectByID(nodeObject.getUserData("ID"));
			}
			else
				targetObject = null;
			updated = true;
		}
	}

	@Override
	public HexLocation targetTile()
	{
		return targetTile;
	}

	@Override
	public Relocatable targetObject()
	{
		return targetObject;
	}

	@Override
	public Input1 checkInput()
	{
		return input1;
	}

	@Override
	public boolean updated()
	{
		return updated;
	}

	@Override
	public void reset()
	{
		input1 = null;
		updated = false;
	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf)
	{
		if(isPressed)
		{
			input1 = Input1.valueOf(name);
			//System.out.println(input1.name());
		}
	}
}