package aer.map.mapgen;

import aer.map.mapgen.mapgenpart.*;
import aer.map.maptiles.*;

public class SplitFillerHexMapGen
{
	public static IHexMapGen get(int dim, MapTileType... layers)
	{
		IHexMapGen[] fillers = new IHexMapGen[layers.length];
		for(int i = 0; i < layers.length; i++)
		{
			fillers[i] = new FillerMapGenPart(layers[i]);
		}
		return new SplitHexMapGen(dim, fillers);
	}
}