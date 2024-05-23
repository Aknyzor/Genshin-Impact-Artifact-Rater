package me.aknyzor;

import java.util.Map;

public enum ArtifactSetType implements IntegerEnum {

    Gladiator(1, RarityType.FIVE),
    Wanderer(11, RarityType.FIVE),
    NOBLESSE(21, RarityType.FIVE),
    BLOODSTAINED(31, RarityType.FIVE),
    MAIDEN(41, RarityType.FIVE),
    VIRIDESCENT(51, RarityType.FIVE),
    ARCHAIC(61, RarityType.FIVE),
    RETRACING(71, RarityType.FIVE),
    THUNDERSOOTHER(81, RarityType.FIVE),
    THUNDERING(91, RarityType.FIVE),
    LAVAWALKER(101, RarityType.FIVE),
    CRIMSON_WITCH(111, RarityType.FIVE),
    BLIZZARD(121, RarityType.FIVE),
    HEART_OF_DEPTH(131, RarityType.FIVE),
    TENACITY(141, RarityType.FIVE),
    PALE_FLAME(151, RarityType.FIVE),
    SHIMENAWA(161, RarityType.FIVE),
    EMBLEM(171, RarityType.FIVE),
    HUSK_OF_OPULENT_DREAMS(181, RarityType.FIVE),
    OCEAN_HUED_CLAM(191, RarityType.FIVE),
    VERMILLION_HEREAFTER(201, RarityType.FIVE),
    ECHOES_OF_AN_OFFERING(211, RarityType.FIVE),
    DEEPWOOD_MEMORIES(221, RarityType.FIVE),
    GILDED_DREAMS(231, RarityType.FIVE),
    DESERT_PAVILION_CHRONICLE(241, RarityType.FIVE),
    FLOWER_OF_PARADISE_LOST(251, RarityType.FIVE),
    NYMPHS_DREAM(261, RarityType.FIVE),
    VOURUKASHAS_GLOW(271, RarityType.FIVE),
    MARECHAUSSEE_HUNTER(281, RarityType.FIVE),
    GOLDEN_TROUPE(291, RarityType.FIVE),
    SONG_OF_DAYS_PAST(301, RarityType.FIVE),
    NIGHTTIME_WHISPERS(311, RarityType.FIVE),
    FRAGMENT_OF_HARMONIC_WHIMSY(321, RarityType.FIVE),
    UNFINISHED_REVERIE(331, RarityType.FIVE);


    private int val;
    private RarityType maxRarity;

    ArtifactSetType(int val, RarityType maxRarity) {
        this.val = val;
        this.maxRarity = maxRarity;
    }

    @Override
    public int getVal() {
        return val;
    }

    public RarityType getMaxRarity() {
        return maxRarity;
    }

    private static transient Map<Integer, ArtifactSetType> map = IntegerEnum.getValues(ArtifactSetType.class);

    public static ArtifactSetType fromVal(int val) {
        return (ArtifactSetType)map.get(val);
    }

}