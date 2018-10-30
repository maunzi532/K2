package aer.summoner;

import aer.locate.*;
import aer.path.pather.*;
import aer.path.team.*;
import aer.relocatable.*;
import aer.relocatable.mount.*;

public class ToSummonBuilder
{
	private int trigger;
	private int offset;
	private int team;
	private int phase;

	private Identifier id;
	private HexLocation loc;
	private HexDirection direction;
	private AirState airState;

	private MType mType;

	private Therathic therathic;

	public ToSummonBuilder(){}

	public ToSummonBuilder setSummonConditions(int trigger, int offset, int phase)
	{
		this.trigger = trigger;
		this.offset = offset;
		this.phase = phase;
		return this;
	}

	public ToSummonBuilder setPosition(HexLocation loc, HexDirection direction, AirState airState)
	{
		this.loc = loc;
		this.direction = direction;
		this.airState = airState;
		return this;
	}

	public ToSummonBuilder setMType(MType mType)
	{
		this.mType = mType;
		return this;
	}

	public ToSummonBuilder setTherathic(Therathic therathic)
	{
		this.therathic = therathic;
		team = therathic.teamSide();
		return this;
	}

	public ToSummonBuilder setIdentifier(Identifier id)
	{
		this.id = id;
		return this;
	}

	public EntryToSummon build()
	{
		assert id != null;
		if(mType == null)
			mType = new MTypeObject();
		Relocatable relocatable;
		if(therathic != null)
		{
			relocatable = new Pather(id, loc, direction, airState, mType, therathic);
		}
		else
		{
			relocatable = new Relocatable(id, loc, direction, airState, mType);
		}
		mType.setMain(relocatable);
		return new EntryToSummon(trigger, offset, team, phase, relocatable);
	}
}