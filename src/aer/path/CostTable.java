package aer.path;

import aer.*;

public interface CostTable
{
	/*public CostTable()
	{
		this(100, 5, 30, 20, 5, 5, 2,
				10, 4, 100, 8, 0, 0, 20, 12, 20, 0, 10, 25, 2, 4);
	}*/

	default int startingAP()
	{
		return 100;
	}

	default int init()
	{
		return 5;
	}

	default int mountCost()
	{
		return 30;
	}

	default int dismountCost()
	{
		return 20;
	}

	default int initMove()
	{
		return 0;
	}

	default int fallCost()
	{
		return 0;
	}

	default int landingCost()
	{
		return 20;
	}

	default int initAirdash()
	{
		return 0;
	}

	default int requiredFall()
	{
		return 4;
	}

	default int startingM()
	{
		return 6;
	}

	default int turnCostM()
	{
		return 1;
	}

	default int mountCostM()
	{
		return 0;
	}

	default int dismountCostM()
	{
		return 0;
	}

	default int initMoveM()
	{
		return 1;
	}

	default int moveCostM()
	{
		return 1;
	}

	default int fallCostM()
	{
		return 1;
	}

	default int landingCostM()
	{
		return 0;
	}

	default int initAirdashM()
	{
		return 1;
	}

	default int airdashMoveM()
	{
		return 2;
	}

	default int turn1()
	{
		return 2;
	}

	default int turn2()
	{
		return 4;
	}

	default int turnCost(HexDirection d0, HexDirection d1)
	{
		if(d0 == null || d1 == null)
			return 0;
		int turn = HexDirection.turnCost(d0, d1);
		if(turn <= turn1())
			return 0;
		if(turn <= turn2())
			return turnCostM();
		return turnCostM() * 2;
	}

	default int moveCost(HexLocation l0, HexLocation l1)
	{
		int diff = HexLocation.xdzDifference(l0, l1);
		return initMoveM() + moveCostM() * diff;
	}

	default int moveCostSeeker(int len)
	{
		return initMoveM() + moveCostM() * len;
	}

	default int airdashCost(HexLocation l0, HexLocation l1)
	{
		int diff = HexLocation.xdzDifference(l0, l1);
		return initAirdashM() + airdashMoveM() * diff;
	}

	default int maxMovement(int movePoints)
	{
		return (movePoints - initMoveM()) / moveCostM();
	}

	default int maxAirdash(int movePoints)
	{
		return (movePoints - initAirdashM()) / airdashMoveM();
	}

	/*static CostTable v1()
	{
		return new CostTable(100, 5, 30, 20, 0, 0, 20,
				0, 4, 6, 1, 0, 0, 1, 1, 1, 0, 1, 2, 2, 4);
	}*/

	class V1 implements CostTable
	{

	}
}