package aer.resource3;

public class StatModifier implements Modifier
{
	public final TStat modified;
	private final int decurseResist;
	private final boolean transformationOnly;
	private int fullTime;
	public int change;

	public StatModifier(TStat modified, boolean transformationOnly, int fullTime, int change)
	{
		this.modified = modified;
		this.decurseResist = -1;
		this.transformationOnly = transformationOnly;
		this.fullTime = fullTime;
		this.change = change;
	}

	public StatModifier(TStat modified, int decurseResist, boolean transformationOnly, int fullTime, int change)
	{
		this.modified = modified;
		this.decurseResist = decurseResist;
		this.transformationOnly = transformationOnly;
		this.fullTime = fullTime;
		this.change = change;
	}

	@Override
	public String name()
	{
		return modified.name() + (change >= 0 ? " +" : " ") + change;
	}

	@Override
	public int fullTime(boolean active)
	{
		return fullTime;
	}

	@Override
	public boolean affectsTransformationOnly()
	{
		return transformationOnly;
	}

	@Override
	public boolean curse()
	{
		return decurseResist >= 0;
	}

	@Override
	public int decurseResist()
	{
		return decurseResist;
	}
}