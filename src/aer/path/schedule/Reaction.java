package aer.path.schedule;

public class Reaction
{
	public final String text;
	public final int code;
	public final int cost;
	public final boolean available;
	public final Object[] data;
	public final int aiValue;

	public Reaction(String text, int code, int cost, boolean available, int aiValue, Object... data)
	{
		this.text = text;
		this.code = code;
		this.cost = cost;
		this.available = available;
		this.aiValue = aiValue;
		this.data = data;
	}
}