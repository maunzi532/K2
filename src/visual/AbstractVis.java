package visual;

import aer.commands.*;
import com.jme3.renderer.*;
import com.jme3.scene.*;
import com.jme3.scene.control.*;
import java.util.*;

public abstract class AbstractVis<T extends ICommandLink> extends AbstractControl
{
	public T linked;
	public ArrayList<ICommand> commands = new ArrayList<>();
	public ArrayList<ICommand> blocking = new ArrayList<>();
	public Node node;

	public AbstractVis(T linked)
	{
		this.linked = linked;
	}

	@Override
	public void setSpatial(Spatial spatial)
	{
		super.setSpatial(spatial);
		assert spatial instanceof Node;
		node = (Node) spatial;
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
			ICommand command = commands.remove(0);
			execute(command);
			if(command.block())
				blocking.add(command);
		}
		while(blocking.size() > 0 && blocking.get(0).finished())
			blocking.remove(0);
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp){}

	public abstract void execute(ICommand command);
}