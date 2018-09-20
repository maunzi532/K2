package visual.hud;

public class FWTLineCreate
{
	private VisHUD visHUD;
	private int mode;
	private TFieldWithText c;
	private float xr0, xrs, xr1, yr0, yra, yrd;
	private float yr0s;

	public FWTLineCreate(VisHUD visHUD, int mode, TFieldWithText c,
			float xr0, float xrs, float xr1, float yr0, float yra, float yrd)
	{
		this.visHUD = visHUD;
		this.mode = mode;
		this.c = c;
		this.xrs = xrs;
		this.yr0 = yr0;
		this.yra = yra;
		this.yrd = yrd;
		reset(xr0, xr1);
	}

	public void reset(float xr0, float xr1)
	{
		this.xr0 = xr0;
		this.xr1 = xr1;
		yr0s = yr0;
	}

	public void create(String name)
	{
		create(name, 1);
	}

	public void create(String name, float ym)
	{
		new FieldWithText(visHUD, mode, name, c, xr0, xr0 + xrs, xr1, yr0s, yra, ym);
		yr0s -= yra * ym + yrd;
	}
}