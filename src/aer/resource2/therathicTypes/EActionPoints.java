package aer.resource2.therathicTypes;

import aer.resource2.interfaces.*;

public interface EActionPoints
{
	default boolean useAP(IAPAction action, boolean real)
	{
		return useAP(action.cost(), real);
	}

	boolean useAP(int amount, boolean real);

	default boolean useMP(IDirectionAction action, boolean real)
	{
		return useMP(action.mCost(), real);
	}

	boolean useMP(int amount, boolean real);

	default boolean useAPMP(IAPAction action1, IDirectionAction action2, boolean real)
	{
		if(real)
			return useAP(action1.cost(), false) && useMP(action2.mCost(), true) && useAP(action1.cost(), true);
		else
			return useAP(action1.cost(), false) && useMP(action2.mCost(), false);
	}

	default void drainAP(IAPAction action)
	{
		drainAP(action.cost());
	}

	void drainAP(int amount);

	default void drainMP(IDirectionAction action)
	{
		drainMP(action.mCost());
	}

	void drainMP(int amount);

	default void drainAPMP(IAPAction action1, IDirectionAction action2)
	{
		drainAP(action1.cost());
		drainMP(action2.mCost());
	}
}