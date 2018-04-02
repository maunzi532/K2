package aer.resource2.resourceTypes;

import aer.*;

public interface RBasicData
{
	HexLocation dLocation();

	HexDirection dDirection();

	AirState dAirState();

	HexObject dMount();

	boolean dMDUsed();

	boolean dEnd();
}