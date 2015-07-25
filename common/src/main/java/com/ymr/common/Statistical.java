package com.ymr.common;

/**
 * Created by ymr on 15/5/28.
 */
public interface Statistical {

    String BANNER= "banner_";
    String BAOMING_TIJIAO_YES = "baoming_tijiao_yes";
    String BAOMING_TIJIAO_SHOUJIHAO = "baoming_tijiao_shoujihao";
    String BAOMING_TIJIAO_XINGMING = "baoming_tijiao_xingming";
    String SHOUYE_JIAOLIAN = "shouye_jiaolian";
    String SHOUYE_JIAGE = "shouye_jiage";
    String SHOUYE_YOUSHI = "shouye_youshi";
    String SHOUYE_CHANGDI = "shouye_changdi";
    String SHOUYE_LIUCHENG = "shouye_liucheng";
    String BAOMING = "baoming";
    String ZIXUN = "zixun";
    String ZIXUN_DIANHUA = "zixun_dianhua";

    void writeToStatistical(String actionType);

}
