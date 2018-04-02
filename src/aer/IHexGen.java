package aer;

public interface IHexGen
{
	void init(IHexMap map);

	void generate(HexLocation loc);
}