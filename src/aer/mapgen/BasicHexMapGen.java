package aer.mapgen;

import aer.*;
import java.util.*;

public class BasicHexMapGen implements IHexMapGen
{
	public ITiledMap map;

	@Override
	public void init(ITiledMap map)
	{
		this.map = map;
	}

	@Override
	public void generate(HexLocation loc)
	{
		Random r = new Random();
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
							map.setTile(new MapTile(map, loc1, r.nextInt(20) == 0 ? MapTileType.BLOCKED : MapTileType.FLOOR));
						else
							map.setTile(new MapTile(map, loc1, MapTileType.BLOCKED));
					}
	}
}