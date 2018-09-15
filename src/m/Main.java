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
import java.io.*;
import java.util.*;
import visual.hud.*;
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

		//Create VisFinder for storing VisRelocatable
		VisFinder visFinder = new VisFinder();
		attachWithNode(rootNode, "VisFinder", visFinder);

		//Create TX_AP_Transform (Equipable) HexPather
		Pather pather = new Pather(new Identifier("Mage_10"), hexMap, new HexLocation(2, 1, 3, 0),
				new HexDirection(6), AirState.UP, new TX_AP_Transform(new Equipable(new AttackItem3(), new AttackItem3(), null, null, null)));
		hexMap.addObject(pather);
		visFinder.attachAndRegister(pather);

		//Create TX_AP_2 HexPather
		Pather pather1 = new Pather(new Identifier("TX_AP_2_11"), hexMap, new HexLocation(4, 1, 0, 0),
				new HexDirection(3), AirState.FLOOR, new TX_AP_2(new CostTable.V1()));
		hexMap.addObject(pather1);
		visFinder.attachAndRegister(pather1);

		//Create TX_AP_Transform (Equipable) HexPather
		Pather pather2 = new Pather(new Identifier("Equip_12"), hexMap, new HexLocation(2, 3, 0, 0),
				new HexDirection(5), AirState.FLOOR, new TX_AP_Transform(new Equipable(new AttackItem3(), null, null, null, null)));
		hexMap.addObject(pather2);
		visFinder.attachAndRegister(pather2);

		//Add Path Info HUD
		VisHUD pathInfoHUD = new PathInfoHUD(guiNode, guiFont, getContext().getSettings());

		//Add TurnSchedule
		TurnSchedule turnSchedule = new TurnSchedule(Collections.singletonList(0), 0, hexMap);

		//Add Targeting and VisTurnSchedule for TurnSchedule
		CursorTargeting targeting = new CursorTargeting(hexMap);
		stateManager.attach(targeting);

		stateManager.attach(new TargetInfoState(guiNode, guiFont, getContext().getSettings(), targeting, hexMap));

		attachWithNode(rootNode, "VTS", new VisTurnSchedule(turnSchedule, targeting, pathInfoHUD,
				rootNode.getChild("Map").getControl(VisTiledMap.class)));
		rootNode.getChild("VTS").getControl(VisTurnSchedule.class).stepToPlayerPhase();

		//Test serialization
		/*InMapSave inMapSave = new InMapSave(turnSchedule);
		byte[] b0 = serialize(inMapSave);
		rootNode.getChild("VTS").getControl(VisTurnSchedule.class).stepToPlayerPhase();
		visFinder.remove(new Identifier("Mage_10"));
		turnSchedule.restore((InMapSave) deserialize(b0));
		visFinder.reattach(hexMap);*/
	}

	public static Node attachWithNode(Node attach, String name, Control control)
	{
		Node node = new Node(name);
		attach.attachChild(node);
		node.addControl(control);
		return node;
	}

	private static byte[] serialize(Serializable m)
	{
		try
		{
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			ObjectOutputStream n = new ObjectOutputStream(b);
			n.writeObject(m);
			n.close();
			b.close();
			return b.toByteArray();
			//System.out.println(b.toString());
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	private static Object deserialize(byte[] b0)
	{
		try
		{
			ByteArrayInputStream b = new ByteArrayInputStream(b0);
			ObjectInputStream n = new ObjectInputStream(b);
			Object m = n.readObject();
			n.close();
			b.close();
			return m;
		}catch(IOException | ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
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
	TODO: Fix additional movement costs
	TODO: TurnSchedule fix skipping
	TODO: Remove Layers which are too high
	TODO: TurnSummoner
	TODO: AerialMovementItem
	TODO: Scatter Objects on the same Hex
	TODO: Camera Controls
	 */

	/*
	TODO: Save/Load
	Fade unused Vis
	Save/Load via UI
	Save additional Data for Map
	Create currently loading map

	TODO: TurnSummoner Save/Load
	Serialize TurnSummoner
	Deserialize TurnSummoner
	Allow TurnSummoner to summon serialized Relocatables
	Save State to file/Load State from file
	Read data for TurnSummoner from file

	TODO: HUD improvements
	Reaction/Interrupt HUD
	Target Object Info
	Clickable HUD
	Choose Path with HUD
	 */
}