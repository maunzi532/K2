package aer.path.schedule;

public class Reaction
{
	public final String text;
	public final int code;
	public final int cost;
	public final boolean available;
	public final Object[] data;

	public Reaction(String text, int code, int cost, boolean available, Object... data)
	{
		this.text = text;
		this.code = code;
		this.cost = cost;
		this.available = available;
		this.data = data;
	}
}