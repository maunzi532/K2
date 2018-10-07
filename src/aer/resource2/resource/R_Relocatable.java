package aer.resource2.resource;

import aer.locate.*;
import aer.relocatable.*;

public interface R_Relocatable
{
	HexLocation dLocation();

	HexDirection dDirection();

	AirState dAirState();

	Relocatable dMount();

	int dMountedToSlot();

	boolean dMTTUsed();

	boolean dEnd();
}