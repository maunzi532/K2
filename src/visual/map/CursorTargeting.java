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
	private Node last2;
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
		//results.getClosestCollision()
		Node new2 = null;
		for(CollisionResult result : results)
		{
			if(result.getGeometry().getParent().getUserData("Target") != null)
			{
				new2 = result.getGeometry().getParent();
				break;
			}
		}
		if(new2 != last2)
		{
			last2 = new2;
			if(last2 != null && last2.getUserData("R") != null)
			{
				if(last2.getUserData("H") != null)
					targetTile = new HexLocation(last2.getUserData("X"), last2.getUserData("D"),
							last2.getUserData("H"), last2.getUserData("R"));
				else
					targetTile = new HexLocation(last2.getUserData("X"), last2.getUserData("D"),
							last2.getParent().getUserData("H"), last2.getUserData("R"));
			}
			else
				targetTile = null;
			if(last2 != null && last2.getUserData("ID") != null)
			{
				targetObject = map.objectByID(last2.getUserData("ID"));
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