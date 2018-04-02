package visual;

import com.jme3.asset.*;
import com.jme3.renderer.*;
import com.jme3.scene.*;
import com.jme3.scene.control.*;
import java.util.*;
import m.*;

public abstract class VisualR<T extends VisualLink> extends AbstractControl
{
	public T linked;
	public ArrayList<VisualCommand> commands = new ArrayList<>();
	public ArrayList<VisualCommand> blocking = new ArrayList<>();
	public Node node;
	public AssetManager assetManager;

	public VisualR(T linked)
	{
		this.linked = linked;
	}

	@Override
	public void setSpatial(Spatial spatial)
	{
		super.setSpatial(spatial);
		assert spatial instanceof Node;
		node = (Node) spatial;
		assetManager = Main.assetManager1;
	}

	public boolean finished()
	{
		return commands.isEmpty() && blocking.isEmpty();
	}

	@Override
	protected void controlUpdate(float tpf)
	{
		commands.addAll(linked.commands());
		linked.deleteCommands();
		while(blocking.size() <= 0 && commands.size() > 0)
		{
			VisualCommand command = commands.remove(0);
			execute(command);
			if(command.block())
				blocking.add(command);
		}
		while(blocking.size() > 0 && blocking.get(0).finished())
			blocking.remove(0);
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp){}

	public abstract void execute(VisualCommand command);
}