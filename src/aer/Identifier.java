package aer;

import java.io.*;

public class Identifier implements Serializable
{
	public final String name;
	public final int num0;
	public final int num1;

	public Identifier(String name, int num0, int num1)
	{
		this.name = name;
		this.num0 = num0;
		this.num1 = num1;
	}

	public Identifier(String name)
	{
		this.name = name;
		num0 = -1;
		num1 = -1;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof Identifier)) return false;
		Identifier that = (Identifier) o;
		return num0 == that.num0 && num1 == that.num1 && name.equals(that.name);
	}

	@Override
	public int hashCode()
	{
		int result = name.hashCode();
		result = 31 * result + num0;
		result = 31 * result + num1;
		return result;
	}

	@Override
	public String toString()
	{
		return name + (num0 >= 0 ? num0 : "") + (num1 >= 0 ? "_S" + num1 : "");
	}
}