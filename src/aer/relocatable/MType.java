package aer.relocatable;

public interface MType
{
	Relocatable main();

	boolean canTransport(Relocatable target);

	int weight();

	int cMin();

	int cMax();

	int slowEffect();

	int energyReduction0();

	int energyReduction1();

	boolean moveLink();

	boolean energyLink();
}