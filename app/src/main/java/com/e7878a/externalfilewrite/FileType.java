package com.e7878a.externalfilewrite;

public enum FileType {

    DOCUMENTS("Documents"), PICTURES("Pictures"), MUSIC("Music"), MOVIES("Movies"), OTHERS("Others");

    private final String mValue;

    FileType(String value) {
        this.mValue = value;
    }

    public String getValue() {
        return mValue;
    }
}
