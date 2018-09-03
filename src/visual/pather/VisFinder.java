package visual.pather;

import aer.*;
import com.jme3.renderer.*;
import com.jme3.scene.*;
import com.jme3.scene.control.*;
import java.util.*;

public class VisFinder extends AbstractControl
{
	public Node finderNode;
	public Map<Identifier, VisRelocatable> byIdentifier;

	public VisFinder()
	{
		byIdentifier = new HashMap<>();
	}

	@Override
	public void setSpatial(Spatial spatial)
	{
		super.setSpatial(spatial);
		finderNode = (Node) spatial;
	}

	public void reattach(ITiledMap map)
	{
		for(VisRelocatable visRelocatable : byIdentifier.values())
		{
			visRelocatable.replaceLink(map.objectByID(visRelocatable.id));
		}
		Set<Identifier> keySet = byIdentifier.keySet();
		map.allObjects().stream().filter(e -> !keySet.contains(e.id)).forEach(this::attachAndRegister);
	}

	public void attachAndRegister(Relocatable relocatable)
	{
		Node node = new Node(relocatable.id.toString());
		finderNode.attachChild(node);
		VisRelocatable visRelocatable = new VisRelocatable(relocatable, this);
		node.addControl(visRelocatable);
		byIdentifier.put(visRelocatable.id, visRelocatable);
	}

	public void remove(Identifier identifier)
	{
		finderNode.detachChildNamed(identifier.toString());
		byIdentifier.remove(identifier);
	}

	@Override
	protected void controlUpdate(float tpf){}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp){}
}