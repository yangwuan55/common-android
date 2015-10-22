package com.ymr.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ymr on 15/6/25.
 */
public class Constant {

    public static String ACTION_EXIST = "ymr_action_exist";

    public interface ActionBean {
        String ACTION_TYPE_LOAD_PAGE = "loadpage";
        String ACTION_TYPE_VIDEO = "playvideo";
        String PAGE_TYPE_LINK = "link";
        String PAGE_TYPE_LINK_BROWSER = "linkBrowser";

        class Target {
            public static HashMap<String, Class> TARGET_MAP = new HashMap<>();

            public static void setTargetMap(Map<String, Class> map) {
                if (map != null && map.size() > 0) {
                    TARGET_MAP.putAll(map);
                }
            }

            static {

            }

        }
    }

    public interface ServiceApi {
    }


}
