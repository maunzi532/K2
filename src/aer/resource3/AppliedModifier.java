package aer.resource3;

public class AppliedModifier
{
	public final Modifier modifier;
	public int timePassed;

	public AppliedModifier(Modifier modifier, TX_AP_Transform tx_ap)
	{
		this.modifier = modifier;
		modifier.addData(tx_ap);
		modifier.start();
	}

	public boolean tick(boolean active)
	{
		if(modifier.tick(active))
			return true;
		int fullTime = modifier.fullTime(active);
		if(fullTime >= 0)
		{
			timePassed++;
			return timePassed >= fullTime;
		}
		return false;
	}

	public void end(boolean active)
	{
		modifier.end(active);
	}
}