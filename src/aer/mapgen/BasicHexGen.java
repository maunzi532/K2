package aer.mapgen;

import aer.*;

public class BasicHexGen implements IHexGen
{
	public IHexMap map;

	@Override
	public void init(IHexMap map)
	{
		this.map = map;
	}

	@Override
	public void generate(HexLocation loc)
	{
		int[] bounds = map.getBounds();
		for(int iX = bounds[0]; iX < bounds[4]; iX++)
			for(int iD = bounds[1]; iD < bounds[5]; iD++)
				for(int iH = bounds[2]; iH < bounds[6]; iH++)
					for(int iR = bounds[3]; iR < bounds[7]; iR++)
					{
						HexLocation loc1 = new HexLocation(iX, iD, iH, iR);
						if(iH > 0)
							map.setTile(new MapTile(map, loc1, MapTileType.AIR));
						else if(iH == 0)
							map.setTile(new MapTile(map, loc1, MapTileType.FLOOR));
						else
							map.setTile(new MapTile(map, loc1, MapTileType.BLOCKED));
					}
	}
}