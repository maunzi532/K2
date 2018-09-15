package aer.resource2.interfaces;

public interface IAPAction
{
	int cost();

	default boolean extraCost()
	{
		return false;
	}
}