package aer;

public enum MountSlotInfo
{
	NOROTATION(false, false, false),
	NORMAL(true, false, false),
	DRIVERNOROTATION(false, true, false),
	DRIVER(true, true, false),
	DRIVERCANROTATE(true, true, true);

	public final boolean allowRotating;
	public final boolean allowSteering;
	public final boolean allowRotatedSteering;

	MountSlotInfo(boolean allowRotating, boolean allowSteering, boolean allowRotatedSteering)
	{
		this.allowRotating = allowRotating;
		this.allowSteering = allowSteering;
		this.allowRotatedSteering = allowRotatedSteering;
	}
}