package top.ss007.businessbase;

/**
 * Created by Ben.Wang
 *
 * @author Ben.Wang
 * @modifier
 * @createDate 2019/5/22 15:43
 * @description
 */
public class RouteTable {
    //SRouter 默认的scheme与host，确保和你在初始化SRouter时传入的scheme和host保持一致
    public static final String SCHEME = "srouter";
    public static final String HOST = "ss007.top";
    public static final String SCHEME_HOST = SCHEME + "://" + HOST;

    public static final String LIB1_ACT_PANEL = "/lib1/openPanel";
    public static final String LIB2_ACT_MY_SON = "/lib2/showMySonAct";
    public static final String LIB2_FRAG_BLANK = "/lib2/blankFragment";
}
