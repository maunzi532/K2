package m;

import aer.locate.*;
import aer.map.*;
import aer.path.pather.*;
import aer.path.schedule.*;
import aer.relocatable.*;
import aer.resource2.therathicType.*;
import aer.resource3.*;
import aer.resource3.resource4.*;
import aer.summoner.*;
import com.jme3.app.*;
import com.jme3.app.state.*;
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


		//Create Map
		ITiledMap hexMap = new HexTiledMap("T1", MapGen1.getMapBounds());
		//hexMap.setGenerator(new BasicHexMapGen());
		hexMap.setGenerator(MapGen1.generator());

		//Create TurnSummoner
		TurnSummoner turnSummoner = new TurnSummoner();

		//Create TurnSchedule
		TurnSchedule turnSchedule = new TurnSchedule(Arrays.asList(0, 1), 0, hexMap, turnSummoner);


		//TX_AP_Transform (Equipable) HexPather (Team 0)
		Pather pather = new Pather(new Identifier("Mage_10"), new HexLocation(2, 1, 3, 0),
				new HexDirection(6), AirState.UP, new TX_AP_Transform(new Equipable(new Agro(), new Agro(), null, null, null), 0, null));
		turnSummoner.entries.add(new EntryToSummon(0, 0, 0, 0, pather));

		//TX_AP_2 HexPather (Team 0)
		Pather pather1 = new Pather(new Identifier("TX_AP_2_11"), new HexLocation(4, 1, 0, 0),
				new HexDirection(3), AirState.FLOOR, new TX_CP_2(0));
		turnSummoner.entries.add(new EntryToSummon(0, 0, 0, 0, pather1));

		//TX_AP_Transform (Equipable) HexPather (Team 1)
		Pather pather2 = new Pather(new Identifier("Equip_12"), new HexLocation(2, 3, 0, 0),
				new HexDirection(5), AirState.FLOOR, new TX_AP_Transform(new Equipable(new Agro(), null, null, null, null), 1, new ValueNPC()));
		turnSummoner.entries.add(new EntryToSummon(0, 0, 0, 0, pather2));


		//Create Meshes and Materials
		MeshLager.init(assetManager);

		//Add CameraHeightState
		CameraHeightState cameraHeightState = attach(stateManager, new CameraHeightState());

		//Create VisTiledMap for HexMap
		VisTiledMap visTiledMap = attachWithNode(rootNode, "Map", new VisTiledMap(hexMap, 0, cameraHeightState));

		//Create VisFinder for storing VisRelocatable
		VisFinder visFinder = attachWithNode(rootNode, "VisFinder", new VisFinder());

		//Create VisTurnSummoner for TurnSummoner
		attachWithNode(rootNode, "VisTurnSummoner", new VisTurnSummoner(turnSummoner, visFinder));

		//Add Targeting
		CursorTargeting targeting = attach(stateManager, new CursorTargeting(hexMap));

		//Add TargetInfoState
		attach(stateManager, new TargetInfoState(guiNode, guiFont, getContext().getSettings(), targeting, hexMap));

		//Create Path Info HUD
		VisHUD pathInfoHUD = new PathInfoHUD(guiNode, guiFont, getContext().getSettings());

		//Create VisTurnSchedule for TurnSchedule
		VisTurnSchedule visTurnSchedule = attachWithNode(rootNode, "VTS", new VisTurnSchedule(turnSchedule, targeting, pathInfoHUD, visTiledMap));

		//Skip to the first player phase
		visTurnSchedule.stepToPlayerPhase();

		//Test serialization
		/*InMapSave inMapSave = new InMapSave(turnSchedule);
		byte[] b0 = serialize(inMapSave);
		rootNode.getChild("VTS").getControl(VisTurnSchedule.class).stepToPlayerPhase();
		visFinder.remove(new Identifier("Mage_10"));
		turnSchedule.restore((InMapSave) deserialize(b0));
		visFinder.reattach(hexMap);*/
	}

	public static <T extends Control> T attachWithNode(Node attach, String name, T control)
	{
		Node node = new Node(name);
		attach.attachChild(node);
		node.addControl(control);
		return control;
	}

	public static <T extends AppState> T attach(AppStateManager stateManager, T appState)
	{
		stateManager.attach(appState);
		return appState;
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
	TODO: AerialMovementItem
	TODO: Scatter Objects on the same Hex
	TODO: TurnSchedule control skipping
	TODO: Camera Controls
	TODO: TurnSummoner other features
	 */

	/*
	TODO: Save/Load
	Remove unused Vis
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
	Clickable HUD
	Choose Path with HUD

	TODO: Use PathSeeker
	PathSeeker with height
	PathSeeker for attacks
	Special PathSeeker for airdashes
	 */
}