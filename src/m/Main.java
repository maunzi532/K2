package m;

import aer.*;
import aer.path.*;
import aer.resource2.*;
import com.jme3.app.*;
import com.jme3.asset.*;
import com.jme3.input.*;
import com.jme3.input.controls.*;
import com.jme3.light.*;
import com.jme3.material.*;
import com.jme3.math.*;
import com.jme3.renderer.*;
import com.jme3.scene.*;
import com.jme3.scene.control.*;
import com.jme3.scene.shape.*;
import com.jme3.system.*;
import java.awt.*;
import java.util.*;
import visual.map.*;
import visual.pather.*;

public class Main extends SimpleApplication
{
	//HAX
	public static AssetManager assetManager1;

	public static void main(String[] args)
	{
		Main app = new Main();

		//Automatically set window size
		int scw = Toolkit.getDefaultToolkit().getScreenSize().width / 400;
		app.setShowSettings(false);
		AppSettings settings = new AppSettings(true);
		/*settings.put("Width", scw * 400);
		settings.put("Height", scw * 225);*/
		settings.put("Width", scw * 320);
		settings.put("Height", scw * 180);
		app.setSettings(settings);

		app.start();
	}

	@Override
	public void simpleInitApp()
	{
		//HAX
		assetManager1 = assetManager;

		//Fix Camera
		getFlyByCamera().setDragToRotate(true);
		getFlyByCamera().setMoveSpeed(40f);
		getFlyByCamera().setZoomSpeed(10f);
		getFlyByCamera().setRotationSpeed(1f);
		inputManager.addMapping(CameraInput.FLYCAM_LOWER, new KeyTrigger(KeyInput.KEY_E));
		getCamera().setLocation(new Vector3f(-50, 40, 0));
		getCamera().setRotation(new Quaternion(-0.3f, -1f, 0.3f, -1f).normalizeLocal());

		//Origin Marker
		Box b = new Box(1, 1, 1);
		Geometry geom = new Geometry("Box", b);
		Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		mat.setBoolean("UseMaterialColors",true);
		mat.setColor("Diffuse", ColorRGBA.Blue);
		mat.setColor("Ambient", ColorRGBA.Blue);
		geom.setMaterial(mat);
		rootNode.attachChild(geom);

		//Lights
		DirectionalLight dl = new DirectionalLight(new Vector3f(0.3f, -0.5f, 0.1f).normalizeLocal());
		rootNode.addLight(dl);
		AmbientLight al = new AmbientLight(new ColorRGBA(0.3f, 0.3f, 0.3f, 1));
		rootNode.addLight(al);


		//Generate Map
		IHexMap hexMap = new HexMap("T1", -10, -2, -2, 0, 11, 10, 5, 1);
		hexMap.setGenerator(new BasicHexGen());

		//Create VisHexMap for HexMap
		attachWithNode(rootNode, "Map", new VisHexMap(hexMap, 0));

		//Create TX_AP_2 HexPather
		HexPather pather = new HexPather(10, hexMap, new HexLocation(2, 1, 0, 0),
				new HexDirection(6), AirState.FLOOR, new TX_AP_2(new CostTable()));
		hexMap.addObject(pather);

		//Create VisObject for HexPather
		attachWithNode(rootNode, "VisHexPather0", new VisObject(pather));

		//Add TurnSchedule
		TurnSchedule turnSchedule = new TurnSchedule(Collections.singletonList(0), 0, hexMap);

		//Add Targeting and VisTurnSchedule for TurnSchedule
		CursorTargeting targeting = new CursorTargeting(hexMap);
		stateManager.attach(targeting);
		attachWithNode(rootNode, "VTS", new VisTurnSchedule(turnSchedule, targeting));
	}

	public static Node attachWithNode(Node attach, String name, Control control)
	{
		Node node = new Node(name);
		attach.attachChild(node);
		node.addControl(control);
		return node;
	}

	@Override
	public void simpleUpdate(float tpf)
	{
		/*rootNode.getChild("X").getControl(VisObject.class).linked.setDirection(
				new HexDirection((rootNode.getChild("X").getControl(VisObject.class).linked.getDirection().r + 1) % 12));*/
	}

	@Override
	public void simpleRender(RenderManager rm){}

	/*public void edge()
	{
		if(renderer.getCaps().contains(Caps.GLSL100))
		{
			FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
			int numSamples = getContext().getSettings().getSamples();
			if(numSamples > 0)
				fpp.setNumSamples(numSamples);
			CartoonEdgeFilter toon = new CartoonEdgeFilter();
			toon.setEdgeWidth(1);
			toon.setEdgeIntensity(10);
			toon.setEdgeColor(ColorRGBA.Black);
			fpp.addFilter(toon);
			viewPort.addProcessor(fpp);
		}
	}*/
}