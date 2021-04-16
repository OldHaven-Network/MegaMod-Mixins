package net.oldhaven.customs.util;

import java.util.Arrays;

/*
* Taken from Rei's Minimap, b1.7.3
* Full credit on this full should go to them, not me.
* */

public class BlockColor {
    public enum TintType {
        NONE,
        GRASS,
        FOLIAGE,
        PINE,
        BIRCH,
        REDSTONE,
        GLASS,
        WATER,
        ETC;

        TintType() { }
    }

    public static final BlockColor[] blockColors = new BlockColor[4096];
    public static final boolean[] useMetadata = new boolean[256];
    public final TintType tintType;
    public int argb;
    public final float alpha;
    public final float red;
    public final float green;
    public final float blue;

    public static BlockColor getBlockColor(int id, int metadata) {
        int ptr = id(id, metadata);
        BlockColor result = blockColors[ptr];
        if (result != null) {
            return result;
        } else {
            result = blockColors[ptr & 255];
            return result != null ? result : blockColors[0];
        }
    }

    public static boolean useMetadata(int id) {
        return useMetadata[id];
    }

    public static void calcUseMetadata() {
        Arrays.fill(useMetadata, false);

        for(int id = 0; id < 256; ++id) {
            for(int meta = 0; meta < 4096 && !useMetadata[id]; meta += 256) {
                BlockColor blockColor = blockColors[id | meta];
                useMetadata[id] = blockColor != null && (blockColor.tintType == TintType.REDSTONE || meta != 0);
            }
        }

    }

    private static int id(int id, int metadata) {
        return id | metadata << 8;
    }

    BlockColor(int argb) {
        this(argb, (TintType)null);
    }

    BlockColor(int argb, TintType type) {
        this.tintType = type == null ? TintType.NONE : type;
        float a = (float)(argb >> 24 & 255) / 255.0F;
        float r = (float)(argb >> 16 & 255) / 255.0F;
        float g = (float)(argb >> 8 & 255) / 255.0F;
        float b = (float)(argb >> 0 & 255) / 255.0F;
        this.argb = argb;
        this.alpha = a;
        this.red = r;
        this.green = g;
        this.blue = b;
    }

    static {
        blockColors[id(0, 0)] = new BlockColor(16711935);
        blockColors[id(1, 0)] = new BlockColor(-9934744);
        blockColors[id(2, 0)] = new BlockColor(-9128886, TintType.GRASS);
        blockColors[id(3, 0)] = new BlockColor(-8825542);
        blockColors[id(4, 0)] = new BlockColor(-6974059);
        blockColors[id(5, 0)] = new BlockColor(-4417438);
        blockColors[id(6, 0)] = new BlockColor(1816358162);
        blockColors[id(6, 1)] = new BlockColor(1412577569);
        blockColors[id(6, 2)] = new BlockColor(1819645267);
        blockColors[id(7, 0)] = new BlockColor(-13421773);
        blockColors[id(8, 0)] = new BlockColor(-1960157441, TintType.WATER);
        blockColors[id(9, 0)] = new BlockColor(-1960157441, TintType.WATER);
        blockColors[id(10, 0)] = new BlockColor(-2530028);
        blockColors[id(11, 0)] = new BlockColor(-2530028);
        blockColors[id(12, 0)] = new BlockColor(-2238560);
        blockColors[id(13, 0)] = new BlockColor(-7766146);
        blockColors[id(14, 0)] = new BlockColor(-7304324);
        blockColors[id(15, 0)] = new BlockColor(-7830913);
        blockColors[id(16, 0)] = new BlockColor(-9145485);
        blockColors[id(17, 0)] = new BlockColor(-10006222);
        blockColors[id(17, 1)] = new BlockColor(-13358823);
        blockColors[id(17, 2)] = new BlockColor(-3620193);
        blockColors[id(18, 0)] = new BlockColor(-1693037300, TintType.FOLIAGE);
        blockColors[id(18, 1)] = new BlockColor(-1693037300, TintType.PINE);
        blockColors[id(18, 2)] = new BlockColor(-1693037300, TintType.BIRCH);
        blockColors[id(19, 0)] = new BlockColor(-1710770);
        blockColors[id(20, 0)] = new BlockColor(1090519039, TintType.GLASS);
        blockColors[id(21, 0)] = new BlockColor(-9998201);
        blockColors[id(22, 0)] = new BlockColor(-14858330);
        blockColors[id(23, 0)] = new BlockColor(-10987432);
        blockColors[id(24, 0)] = new BlockColor(-2370913);
        blockColors[id(25, 0)] = new BlockColor(-10206158);
        blockColors[id(26, 0)] = new BlockColor(-6339259, TintType.ETC);
        blockColors[id(26, 8)] = new BlockColor(-6339259, TintType.ETC);
        blockColors[id(27, 0)] = new BlockColor(-528457632, TintType.ETC);
        blockColors[id(27, 8)] = new BlockColor(-523214752, TintType.ETC);
        blockColors[id(28, 0)] = new BlockColor(-8952744);
        blockColors[id(29, 0)] = new BlockColor(-9605779);
        blockColors[id(29, 1)] = new BlockColor(-7499421);
        blockColors[id(29, 2)] = new BlockColor(-9804194);
        blockColors[id(29, 3)] = new BlockColor(-9804194);
        blockColors[id(29, 4)] = new BlockColor(-9804194);
        blockColors[id(29, 5)] = new BlockColor(-9804194);
        blockColors[id(29, 8)] = new BlockColor(-9605779);
        blockColors[id(29, 9)] = new BlockColor(-7499421);
        blockColors[id(29, 10)] = new BlockColor(-9804194);
        blockColors[id(29, 11)] = new BlockColor(-9804194);
        blockColors[id(29, 12)] = new BlockColor(-9804194);
        blockColors[id(29, 13)] = new BlockColor(-9804194);
        blockColors[id(30, 0)] = new BlockColor(1775884761);
        blockColors[id(31, 0)] = new BlockColor(1383747097);
        blockColors[id(31, 1)] = new BlockColor(-1571782606, TintType.GRASS);
        blockColors[id(31, 2)] = new BlockColor(1330675762, TintType.GRASS);
        blockColors[id(32, 0)] = new BlockColor(1383747097);
        blockColors[id(33, 0)] = new BlockColor(-9605779);
        blockColors[id(33, 1)] = new BlockColor(-6717094);
        blockColors[id(33, 2)] = new BlockColor(-9804194);
        blockColors[id(33, 3)] = new BlockColor(-9804194);
        blockColors[id(33, 4)] = new BlockColor(-9804194);
        blockColors[id(33, 5)] = new BlockColor(-9804194);
        blockColors[id(33, 8)] = new BlockColor(-9605779);
        blockColors[id(33, 9)] = new BlockColor(-6717094);
        blockColors[id(33, 10)] = new BlockColor(-9804194);
        blockColors[id(33, 11)] = new BlockColor(-9804194);
        blockColors[id(33, 12)] = new BlockColor(-9804194);
        blockColors[id(33, 13)] = new BlockColor(-9804194);
        blockColors[id(34, 0)] = new BlockColor(-6717094);
        blockColors[id(34, 1)] = new BlockColor(-6717094);
        blockColors[id(34, 2)] = new BlockColor(-2137423526);
        blockColors[id(34, 3)] = new BlockColor(-2137423526);
        blockColors[id(34, 4)] = new BlockColor(-2137423526);
        blockColors[id(34, 5)] = new BlockColor(-2137423526);
        blockColors[id(34, 8)] = new BlockColor(-6717094);
        blockColors[id(34, 9)] = new BlockColor(-7499421);
        blockColors[id(34, 10)] = new BlockColor(-2137423526);
        blockColors[id(34, 11)] = new BlockColor(-2137423526);
        blockColors[id(34, 12)] = new BlockColor(-2137423526);
        blockColors[id(34, 13)] = new BlockColor(-2137423526);
        blockColors[id(35, 0)] = new BlockColor(-2236963);
        blockColors[id(35, 1)] = new BlockColor(-1475018);
        blockColors[id(35, 2)] = new BlockColor(-4370744);
        blockColors[id(35, 3)] = new BlockColor(-9991469);
        blockColors[id(35, 4)] = new BlockColor(-4082660);
        blockColors[id(35, 5)] = new BlockColor(-12928209);
        blockColors[id(35, 6)] = new BlockColor(-2588006);
        blockColors[id(35, 7)] = new BlockColor(-12434878);
        blockColors[id(35, 8)] = new BlockColor(-6445916);
        blockColors[id(35, 9)] = new BlockColor(-14191468);
        blockColors[id(35, 10)] = new BlockColor(-8374846);
        blockColors[id(35, 11)] = new BlockColor(-14273895);
        blockColors[id(35, 12)] = new BlockColor(-11193573);
        blockColors[id(35, 13)] = new BlockColor(-13153256);
        blockColors[id(35, 14)] = new BlockColor(-6083544);
        blockColors[id(35, 15)] = new BlockColor(-15067369);
        blockColors[id(37, 0)] = new BlockColor(-1057883902);
        blockColors[id(38, 0)] = new BlockColor(-1057552625);
        blockColors[id(39, 0)] = new BlockColor(-1064211115);
        blockColors[id(40, 0)] = new BlockColor(-1063643364);
        blockColors[id(41, 0)] = new BlockColor(-66723);
        blockColors[id(42, 0)] = new BlockColor(-1447447);
        blockColors[id(43, 0)] = new BlockColor(-5723992);
        blockColors[id(43, 1)] = new BlockColor(-1712721);
        blockColors[id(43, 2)] = new BlockColor(-7046838);
        blockColors[id(43, 3)] = new BlockColor(-8224126);
        blockColors[id(44, 0)] = new BlockColor(-5723992);
        blockColors[id(44, 1)] = new BlockColor(-1712721);
        blockColors[id(44, 2)] = new BlockColor(-7046838);
        blockColors[id(44, 3)] = new BlockColor(-8224126);
        blockColors[id(45, 0)] = new BlockColor(-6591135);
        blockColors[id(46, 0)] = new BlockColor(-2407398);
        blockColors[id(47, 0)] = new BlockColor(-4943782);
        blockColors[id(48, 0)] = new BlockColor(-14727393);
        blockColors[id(49, 0)] = new BlockColor(-15527395);
        blockColors[id(50, 0)] = new BlockColor(1627379712);
        blockColors[id(51, 0)] = new BlockColor(-4171263);
        blockColors[id(52, 0)] = new BlockColor(-14262393);
        blockColors[id(53, 0)] = new BlockColor(-4417438);
        blockColors[id(54, 0)] = new BlockColor(-7378659);
        blockColors[id(55, 0)] = new BlockColor(1827466476, TintType.REDSTONE);
        blockColors[id(56, 0)] = new BlockColor(-8287089);
        blockColors[id(57, 0)] = new BlockColor(-10428192);
        blockColors[id(58, 0)] = new BlockColor(-8038091);
        blockColors[id(59, 0)] = new BlockColor(302029071);
        blockColors[id(59, 1)] = new BlockColor(957524751);
        blockColors[id(59, 2)] = new BlockColor(1444710667);
        blockColors[id(59, 3)] = new BlockColor(-1708815608);
        blockColors[id(59, 4)] = new BlockColor(-835813369);
        blockColors[id(59, 5)] = new BlockColor(-532579833);
        blockColors[id(59, 6)] = new BlockColor(-531663353);
        blockColors[id(59, 7)] = new BlockColor(-531208953);
        blockColors[id(60, 0)] = new BlockColor(-9221331);
        blockColors[id(60, 1)] = new BlockColor(-9550295);
        blockColors[id(60, 2)] = new BlockColor(-9879003);
        blockColors[id(60, 3)] = new BlockColor(-10207967);
        blockColors[id(60, 4)] = new BlockColor(-10536675);
        blockColors[id(60, 5)] = new BlockColor(-10865383);
        blockColors[id(60, 6)] = new BlockColor(-11194347);
        blockColors[id(60, 7)] = new BlockColor(-11523055);
        blockColors[id(60, 8)] = new BlockColor(-11786226);
        blockColors[id(61, 0)] = new BlockColor(-9145228);
        blockColors[id(62, 0)] = new BlockColor(-8355712);
        blockColors[id(63, 0)] = new BlockColor(-1598779307);
        blockColors[id(64, 0)] = new BlockColor(-1064934094);
        blockColors[id(65, 0)] = new BlockColor(-2139595212);
        blockColors[id(66, 0)] = new BlockColor(-8951211);
        blockColors[id(67, 0)] = new BlockColor(-6381922);
        blockColors[id(68, 0)] = new BlockColor(-1598779307);
        blockColors[id(69, 0)] = new BlockColor(-1603709901);
        blockColors[id(70, 0)] = new BlockColor(-7368817);
        blockColors[id(71, 0)] = new BlockColor(-1061043775);
        blockColors[id(72, 0)] = new BlockColor(-4417438);
        blockColors[id(73, 0)] = new BlockColor(-6981535);
        blockColors[id(74, 0)] = new BlockColor(-6981535);
        blockColors[id(75, 0)] = new BlockColor(-2141709038);
        blockColors[id(76, 0)] = new BlockColor(-2136923117);
        blockColors[id(77, 0)] = new BlockColor(-2139851660);
        blockColors[id(78, 0)] = new BlockColor(-1314833);
        blockColors[id(79, 0)] = new BlockColor(-1619219203, TintType.WATER);
        blockColors[id(80, 0)] = new BlockColor(-986896);
        blockColors[id(81, 0)] = new BlockColor(-15695840);
        blockColors[id(82, 0)] = new BlockColor(-6380624);
        blockColors[id(83, 0)] = new BlockColor(-7094428);
        blockColors[id(84, 0)] = new BlockColor(-9811658);
        blockColors[id(85, 0)] = new BlockColor(-4417438);
        blockColors[id(86, 0)] = new BlockColor(-4229867);
        blockColors[id(87, 0)] = new BlockColor(-9751501);
        blockColors[id(88, 0)] = new BlockColor(-11255757);
        blockColors[id(89, 0)] = new BlockColor(-4157626);
        blockColors[id(90, 0)] = new BlockColor(-9231226);
        blockColors[id(91, 0)] = new BlockColor(-3893474);
        blockColors[id(92, 0)] = new BlockColor(-1848115);
        blockColors[id(93, 0)] = new BlockColor(-6843501);
        blockColors[id(94, 0)] = new BlockColor(-4156525);
        blockColors[id(95, 0)] = new BlockColor(-7378659);
        blockColors[id(96, 0)] = new BlockColor(-8495827, TintType.ETC);
        blockColors[id(96, 4)] = new BlockColor(545152301, TintType.ETC);
        calcUseMetadata();
    }
}

