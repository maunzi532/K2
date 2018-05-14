package aer.resource3;

public interface EWithStats
{
	/**
	 * Stats            LYellow Orange  Flower  Blue    LGreen      Fire    Air     Dark
	 * Class1           Mage    Mage    Mage    Mage    Mage        CMage   Air     Summoner
	 * Class2           Hero    Creator Unknown Hero    Unknown
	 *
	 * Height           2       3       2       3       3           3       4       3
	 * Weight           2       2       2       2       2           3       3       3
	 *
	 * Movement         4       3       3       3       3           3       3       2
	 * Agile            2       2       3       4       4           2       4       3
	 *
	 * Unlucky          4       5       2       3       4
	 * Chaos            5       2       1       3       3           4       2       2
	 *
	 * Sophisticate     3       2       5       4       1           4       2       3
	 * Magic P          3       5       3       3       4           4       3       3
	 * Dmg Mod                                                      3       4       2
	 * Acc Mod                                                      2       2       4
	 * Defend           2       1       3       4       2           3       3       4
	 *
	 * Physic R         4       5       2       3       1           4       2       2
	 * Magic R          2       3       5       2       2           4       3       3
	 * Psychic R        4       4       2       2       2           3       3       3
	 * Combat R         2       1       2       4       3           2       3       2
	 *
	 *
	 * Wand Spells      LYellow         Orange          Flower          Blue            LGreen
	 *                  Summon Block    Whirlwind       Restrict A      Restrict B      Transform
	 *                  Summon Vehicle  Decurse         Ladder 2        Slide
	 *                  Ladder 1
	 *                  Surround
	 *
	 *
	 * Less Stats       LYellow Orange  Flower  Blue    LGreen      Fire    Air     Dark
	 * Class1           Mage    Mage    Mage    Mage    Mage        CMage   Air     Summoner
	 * Class2           Hero                    Hero
	 *
	 * Height           2       3       2       3       3           3       4       3
	 * Weight           2       2       2       2       2           2       3       3
	 * Magic R          3       5       4       1       3           4       3       3
	 * Combat R         2       1       2       4       3           2       3       2
	 *
	 * Movement         3       3       3       3       3           3       4       2
	 * Luck affected    +1      +2      -1      0       +1          -       -       -
	 * Order / Chaos    +2      -1      -2      +1      +1          +1      0       -1
	 *
	 * Magic Power      3       4       3       2       3           4       3       3
	 * Attack Power     2       2       -       3       -           3       4       2
	 * Defend Power     2       1       3       4       2           3       3       4
	 */
}