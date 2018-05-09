package aer.resource3;

public class TransformingModifier implements Modifier
{
	private TX_AP_Transform tx_ap;
	private final Transformation transformation;
	private final int decurseResist;
	private int fullTime;

	public TransformingModifier(Transformation transformation, int fullTime)
	{
		this.transformation = transformation;
		this.decurseResist = -1;
		this.fullTime = fullTime;
	}

	public TransformingModifier(Transformation transformation, int decurseResist, int fullTime)
	{
		this.transformation = transformation;
		this.decurseResist = decurseResist;
		this.fullTime = fullTime;
	}

	@Override
	public String name()
	{
		return null;
	}

	@Override
	public void addData(TX_AP_Transform tx_ap)
	{
		this.tx_ap = tx_ap;
	}

	@Override
	public int fullTime(boolean active)
	{
		return fullTime;
	}

	@Override
	public void start()
	{
		tx_ap.transformInto(transformation);
	}

	@Override
	public void end(boolean active)
	{
		tx_ap.endTransformation(transformation);
	}

	@Override
	public boolean affectsTransformationOnly()
	{
		return false;
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