package m;

import aer.*;
import aer.mapgen.*;
import aer.path.*;
import aer.resource2.therathicType.*;
import aer.resource3.*;
import aer.resource3.resource4.*;
import com.jme3.app.*;
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
import visual.mesh.*;
import visual.pather.*;

public class Main extends SimpleApplication
{
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
		ITiledMap hexMap = new HexTiledMap("T1", -10, -2, -2, 0, 11, 10, 5, 1);
		hexMap.setGenerator(new BasicHexMapGen());

		//Create Meshes and Materials
		MeshLager.init(assetManager);

		CameraHeightState cameraHeightState = new CameraHeightState();
		stateManager.attach(cameraHeightState);
		cameraHeightState.setEnabled(true);

		//Create VisTiledMap for HexMap
		attachWithNode(rootNode, "Map", new VisTiledMap(hexMap, 0, cameraHeightState));

		//Create TX_AP_Transform (Mage) HexPather
		Pather pather = new Pather(new Identifier("Mage_10"), hexMap, new HexLocation(2, 1, 3, 0),
				new HexDirection(6), AirState.UP, new TX_AP_Transform(new Mage()));
		hexMap.addObject(pather);

		//Create VisObject for HexPather
		attachWithNode(rootNode, "VisHexPather_Mage_10", new VisRelocatable(pather));

		//Create TX_AP_2 HexPather
		Pather pather1 = new Pather(new Identifier("TX_AP_2_11"), hexMap, new HexLocation(4, 1, 0, 0),
				new HexDirection(3), AirState.FLOOR, new TX_AP_2(new CostTable()));
		hexMap.addObject(pather1);
		attachWithNode(rootNode, "VisHexPather_TX_AP_2_11", new VisRelocatable(pather1));

		//Add HUD
		VisHUD visHUD = new VisHUD(guiNode, guiFont, getContext().getSettings());

		//Add TurnSchedule
		TurnSchedule turnSchedule = new TurnSchedule(Collections.singletonList(0), 0, hexMap);

		//Add Targeting and VisTurnSchedule for TurnSchedule
		CursorTargeting targeting = new CursorTargeting(hexMap);
		stateManager.attach(targeting);
		attachWithNode(rootNode, "VTS", new VisTurnSchedule(turnSchedule, targeting, visHUD,
				rootNode.getChild("Map").getControl(VisTiledMap.class)));
		rootNode.getChild("VTS").getControl(VisTurnSchedule.class).stepToPlayerPhase();
	}

	public static Node attachWithNode(Node attach, String name, Control control)
	{
		Node node = new Node(name);
		attach.attachChild(node);
		node.addControl(control);
		return node;
	}

	@Override
	public void simpleUpdate(float tpf){}

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

	/*
	TODO: Level Object Spawner
	TODO: AerialMovementItem
	TODO: Scatter Objects on the same Hex
	TODO: Reaction/Interrupt HUD
	TODO: Remove Layers which are too high
	TODO: Camera Controls
	TODO: Serialize State, Restore State, Link to Vis, Save State to File
	 */
}