package aer.mapgen;

import aer.*;

public interface IHexGen
{
	void init(IHexMap map);

	void generate(HexLocation loc);
}