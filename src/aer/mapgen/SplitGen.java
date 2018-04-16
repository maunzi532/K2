package aer.mapgen;

import aer.*;

public class SplitGen implements IHexGen
{
	private int dim;
	private IHexGen[] inner;
	private int[] bounds;

	public SplitGen(int dim, IHexGen... inner)
	{
		this.dim = dim;
		this.inner = inner;
	}

	@Override
	public void init(IHexMap map)
	{
		bounds = map.getBounds();
		assert inner.length == bounds[dim + 4] - bounds[dim];
		for(int i = 0; i < inner.length; i++)
			inner[i].init(map);
	}

	@Override
	public void generate(HexLocation loc)
	{
		int ld = loc.dim(dim);
		if(ld >= bounds[dim] && ld < bounds[dim + 4])
			inner[ld - bounds[dim]].generate(loc);
	}
}