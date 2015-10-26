package com.ymr.common;

/**
 * Created by ymr on 15/10/23.
 */
public enum ProjectState {
    DBUG(Constants.DEBUG),RELEASE(Constants.RELEASE);

    private final int mIntV;

    ProjectState(int intV) {
        mIntV = intV;
    }

    public static ProjectState makeStateByInt(int intV) {
        switch (intV) {
            case Constants.DEBUG:
                return DBUG;

            case Constants.RELEASE:
                return RELEASE;
        }
        return null;
    }

    public static class Constants {
        public static final int DEBUG = 0;
        private static final int RELEASE = 1;
    }
}
