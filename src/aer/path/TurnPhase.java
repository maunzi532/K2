package aer.path;

public enum TurnPhase
{
	DRAW(true),
	PLAYERACTION(true),
	ALLYACTION(true),
	END(true),
	EXEC(false),
	TARGET(false),
	ALLYINTERRUPT(false),
	PLAYERINTERRUPT(false);

	boolean mainPhase;

	TurnPhase(boolean mainPhase)
	{
		this.mainPhase = mainPhase;
	}
}