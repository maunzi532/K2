package aer.path;

public class Reaction
{
	public final String text;
	public final int code;
	public final boolean available;

	public Reaction(String text, int code, boolean available)
	{
		this.text = text;
		this.code = code;
		this.available = available;
	}
}