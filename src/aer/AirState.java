package aer;

public enum AirState
{
	UP2(0, true, false, true),
	UP(0, true, false, true),
	FLY(-1, true, false, true),
	DOWN(-1, true, false, true),
	DOWN2(-1, false, true, true),
	FLOOR(0, false, true, false),
	MOUNT(0, false, true, false);

	public int fall;
	public boolean canAirdash;
	public boolean canTurn;
	public boolean isAerial;

	AirState(int fall, boolean canAirdash, boolean canTurn, boolean isAerial)
	{
		this.fall = fall;
		this.canAirdash = canAirdash;
		this.canTurn = canTurn;
		this.isAerial = isAerial;
	}
}