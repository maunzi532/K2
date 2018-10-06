package aer.resource2.therathicType;

import aer.resource2.interfaces.*;

public interface E_AP_MP
{
	int getAP();

	default boolean useAP(IAPAction action, Use use)
	{
		return useAP(action.cost(), use);
	}

	boolean useAP(int amount, Use use);

	int getMP();

	default boolean useMP(IDirectionAction action, Use use)
	{
		return useMP(action.mCost(), use);
	}

	boolean useMP(int amount, Use use);

	default boolean useAPMP(IAPAction action1, IDirectionAction action2, Use use)
	{
		if(use == Use.REAL)
			return useAP(action1.cost(), Use.TEST) && useMP(action2.mCost(), Use.REAL) && useAP(action1.cost(), Use.REAL);
		else
			return useAP(action1.cost(), use) && useMP(action2.mCost(), use);
	}

	enum Use
	{
		TEST,
		REAL,
		DRAIN
	}

	static Use rd(boolean d)
	{
		return d ? Use.DRAIN : Use.REAL;
	}
}