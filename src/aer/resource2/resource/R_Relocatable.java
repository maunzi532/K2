package aer.resource2.resource;

import aer.*;

public interface R_Relocatable
{
	HexLocation dLocation();

	HexDirection dDirection();

	AirState dAirState();

	Relocatable dMount();

	boolean dMTTUsed();

	boolean dEnd();
}