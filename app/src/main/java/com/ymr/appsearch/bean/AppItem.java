package com.ymr.appsearch.bean;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.ymr.appsearch.App;
import com.ymr.appsearch.db.DBHelper;
import com.ymr.appsearch.t9.ISeachItem;
import com.ymr.dao.AbsBean;

import java.sql.SQLException;

/**
 * Created by ymr on 15/11/21.
 */
@DatabaseTable(tableName = "db_app")
public class AppItem extends AbsBean<AppItem, Integer> implements ISeachItem<AppItem> {

    public static final String COL_PACKAGE_NAME = "package_name";
    public static final String COL_STATE = "state";

    public static final int STATE_ENABLE = 0;
    public static final int STATE_DISABLE = 1;
    @DatabaseField(generatedId = true, columnName = "id")
    private int id;

    /*
     * Application : app name; Contact : display name
     */
    @DatabaseField(columnName = "name")
    private String name;

    // search string 1
    @DatabaseField(index = true, columnName = "pin_yin")
    private String pinyin;

    // search string 2
    @DatabaseField(index = true, columnName = "full_pin_yin")
    private String fullpinyin;

    // Application item only
    @DatabaseField(uniqueCombo = true, columnName = COL_PACKAGE_NAME)
    private String packageName;
    @DatabaseField(columnName = "intent")
    private String baseIntent;

    @DatabaseField(columnName = "open_count")
    private int openCount;

    @DatabaseField(columnName = "image")
    private String image;

    @DatabaseField(columnName = COL_STATE)
    private int state = STATE_ENABLE;

    public AppItem() throws SQLException {
        super(DBHelper.getInstance(App.getApp()));
    }

    @Override
    public String getPinYin() {
        return fullpinyin;
    }

    @Override
    public String getShortPinYin() {
        return pinyin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getFullpinyin() {
        return fullpinyin;
    }

    public void setFullpinyin(String fullpinyin) {
        this.fullpinyin = fullpinyin;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getBaseIntent() {
        return baseIntent;
    }

    public void setBaseIntent(String baseIntent) {
        this.baseIntent = baseIntent;
    }

    public int getOpenCount() {
        return openCount;
    }

    public void setOpenCount(int openCount) {
        this.openCount = openCount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "AppItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", fullpinyin='" + fullpinyin + '\'' +
                ", packageName='" + packageName + '\'' +
                ", baseIntent='" + baseIntent + '\'' +
                ", openCount=" + openCount +
                ", image='" + image + '\'' +
                '}';
    }

    //匹配规则
    @Override
    public int compareTo(AppItem another) {
        //打开次数
        int i = another.openCount - openCount;
        if (i == 0) {
            //如果打开次数相同 名字长度从小到大排列。
            i = name.length() - another.getName().length();
        }
        return i;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
