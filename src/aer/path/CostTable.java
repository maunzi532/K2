package aer.path;

public class CostTable
{
	public int init, tcm, mountCost, dismountCost, initMove, moveCost, fallCost, landingCost, initAD, adMove;
	public int initM, tcmM, mountCostM, dismountCostM, initMoveM, moveCostM, fallCostM, landingCostM, initADM, adMoveM;

	public CostTable(int init, int tcm, int mountCost, int dismountCost, int initMove, int moveCost, int fallCost,
			int landingCost, int initAD, int adMove, int initM, int tcmM, int mountCostM, int dismountCostM,
			int initMoveM,
			int moveCostM, int fallCostM, int landingCostM, int initADM, int adMoveM)
	{
		this.init = init;
		this.tcm = tcm;
		this.mountCost = mountCost;
		this.dismountCost = dismountCost;
		this.initMove = initMove;
		this.moveCost = moveCost;
		this.fallCost = fallCost;
		this.landingCost = landingCost;
		this.initAD = initAD;
		this.adMove = adMove;
		this.initM = initM;
		this.tcmM = tcmM;
		this.mountCostM = mountCostM;
		this.dismountCostM = dismountCostM;
		this.initMoveM = initMoveM;
		this.moveCostM = moveCostM;
		this.fallCostM = fallCostM;
		this.landingCostM = landingCostM;
		this.initADM = initADM;
		this.adMoveM = adMoveM;
	}

	public CostTable(int init, int tcm, int mountCost, int dismountCost, int initMove, int moveCost, int fallCost,
			int landingCost, int initAD, int adMove)
	{
		this.init = init;
		this.tcm = tcm;
		this.mountCost = mountCost;
		this.dismountCost = dismountCost;
		this.initMove = initMove;
		this.moveCost = moveCost;
		this.fallCost = fallCost;
		this.landingCost = landingCost;
		this.initAD = initAD;
		this.adMove = adMove;
		this.initM = init;
		this.tcmM = tcm;
		this.mountCostM = mountCost;
		this.dismountCostM = dismountCost;
		this.initMoveM = initMove;
		this.moveCostM = moveCost;
		this.fallCostM = fallCost;
		this.landingCostM = landingCost;
		this.initADM = initAD;
		this.adMoveM = adMove;
	}

	public CostTable()
	{
		this(5, 2, 30, 20, 5, 15, 5, 2, 10, 25,
				0, 8, 0, 0, 20, 12, 20, 0, 10, 25);
	}
}