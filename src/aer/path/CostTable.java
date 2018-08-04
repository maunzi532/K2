package aer.path;

import aer.*;

public class CostTable
{
	public int startingAP;
	public int init;
	public int mountCost;
	public int dismountCost;
	public int initMove;
	public int fallCost;
	public int landingCost;
	public int initAirdash;
	public int requiredFall;
	public int startingM;
	protected int turnCostM;
	public int mountCostM;
	public int dismountCostM;
	protected int initMoveM;
	private int moveCostM;
	public int fallCostM;
	public int landingCostM;
	protected int initAirdashM;
	protected int airdashMoveM;
	protected int turn1;
	protected int turn2;

	public CostTable(int startingAP, int init, int mountCost, int dismountCost, int initMove, int fallCost,
			int landingCost, int initAirdash, int requiredFall, int startingM, int turnCostM, int mountCostM,
			int dismountCostM,
			int initMoveM, int moveCostM, int fallCostM, int landingCostM, int initAirdashM, int airdashMoveM,
			int turn1, int turn2)
	{
		this.startingAP = startingAP;
		this.init = init;
		this.mountCost = mountCost;
		this.dismountCost = dismountCost;
		this.initMove = initMove;
		this.fallCost = fallCost;
		this.landingCost = landingCost;
		this.initAirdash = initAirdash;
		this.requiredFall = requiredFall;
		this.startingM = startingM;
		this.turnCostM = turnCostM;
		this.mountCostM = mountCostM;
		this.dismountCostM = dismountCostM;
		this.initMoveM = initMoveM;
		this.moveCostM = moveCostM;
		this.fallCostM = fallCostM;
		this.landingCostM = landingCostM;
		this.initAirdashM = initAirdashM;
		this.airdashMoveM = airdashMoveM;
		this.turn1 = turn1;
		this.turn2 = turn2;
	}

	public CostTable()
	{
		this(100, 5, 30, 20, 5, 5, 2,
				10, 4, 100, 8, 0, 0, 20, 12, 20, 0, 10, 25, 2, 4);
	}

	public static CostTable v1()
	{
		return new CostTable(100, 5, 30, 20, 0, 0, 20,
				0, 4, 6, 1, 0, 0, 1, 1, 1, 0, 1, 2, 2, 4);
	}

	public int turnCost(HexDirection d0, HexDirection d1)
	{
		if(d0 == null || d1 == null)
			return 0;
		int turn = HexDirection.turnCost(d0, d1);
		if(turn <= turn1)
			return 0;
		if(turn <= turn2)
			return turnCostM;
		return turnCostM * 2;
	}

	public int moveCost(HexLocation l0, HexLocation l1)
	{
		int diff = HexLocation.xdzDifference(l0, l1);
		return initMoveM + moveCostM * diff;
	}

	public int airdashCost(HexLocation l0, HexLocation l1)
	{
		int diff = HexLocation.xdzDifference(l0, l1);
		return initAirdashM + airdashMoveM * diff;
	}

	public int maxMovement(int movePoints)
	{
		return (movePoints - initMoveM) / moveCostM;
	}

	public int maxAirdash(int movePoints)
	{
		return (movePoints - initAirdashM) / airdashMoveM;
	}
}