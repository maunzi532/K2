package aer.resource3;

public interface Modifier
{
	String name();

	default void addData(TX_AP_Transform tx_ap){}

	//int power();

	//HexItem item();

	default int fullTime(boolean active)
	{
		return -1;
	}

	default void start(){}

	default boolean tick(boolean active)
	{
		return false;
	}

	default void end(boolean active){}

	boolean affectsTransformationOnly();

	default boolean curse()
	{
		return false;
	}

	default int decurseResist()
	{
		return 0;
	}
}