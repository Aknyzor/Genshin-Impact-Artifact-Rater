package me.aknyzor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public enum StatType implements OrdinalEnum {

    ATK_FLAT(1L, "A.F"),

    ATK_PERCENT(1L << 1, "A+%"),

    CRIT_RATE(1L << 2, "Crit%"),

    CRIT_DMG(1L << 3, "CritD"),

    ELEMENTAL_MASTERY(1L << 4, "EM"),

    ENERY_RECHARGE(1L << 5, "EReC"),

    PYRO_BONUS(1L << 6, "Pyro+%"),
    PHYSICAL_BONUS(1L << 23, "Phys+%"),

    HYDRO_BONUS(1L << 8, "Hyro+%"),

    CRYO_BONUS(1L << 11, "Cryo+%"),

    DENDRO_BONUS(1L << 13, "Dend+%"),

    ANEMO_BONUS(1L << 15, "Anem+%"),

    GEO_BONUS(1L << 17, "Geo+%"),

    ELECTRO_BONUS(1L << 19, "Elec+%"),

    HEALING_BONUS(1L << 21, "Heal+%"),

    HP_FLAT(1L << 40, "HP.F"),

    HP_PERCENT(1L << 41, "HP+%"),


    DEF_FLAT(1L << 42, "DEF.F"),

    DEF_PERCENT(1L << 43, "DEF+%");

    private long val;
    private String shortName;

    StatType(long val, String shortName) {
        this.val = val;
        this.shortName = shortName;
    }

    @Override
    public long getVal() {
        return val;
    }

    public static List<Long> fromCollection(long collection) {
        List<Long> statList = new ArrayList<>(46);
        for (StatType statType : map.values()) {
            if ((collection & statType.getVal()) == statType.getVal())
                statList.add(statType.getVal());
        }

        return statList;
    }

    public String getShortName() {
        return shortName;
    }

    private static transient Map<Long, StatType> map = OrdinalEnum.getValues(StatType.class);

    public static StatType fromVal(long val) {
        return (StatType)map.get(val);
    }

    public static StatType[] valuesWithoutExcluded() {
        List<StatType> valuesList = new ArrayList<>();
        for (StatType stat : StatType.values()) {
            if (!isExcluded(stat)) {
                valuesList.add(stat);
            }
        }
        return valuesList.toArray(new StatType[0]);
    }

    private static boolean isExcluded(StatType stat) {
        return stat == StatType.PYRO_BONUS || stat == StatType.HYDRO_BONUS || stat == StatType.DENDRO_BONUS
                || stat == StatType.ELECTRO_BONUS || stat == StatType.CRYO_BONUS || stat == StatType.PHYSICAL_BONUS
                || stat == StatType.ANEMO_BONUS || stat == StatType.GEO_BONUS || stat == StatType.HEALING_BONUS;
    }
}