package app.qarya.model;

import org.jetbrains.annotations.Contract;

public final class ModelType{
    @Contract(pure = true)
    private ModelType(){}

    public static final int SHARE_PRIVATE = -4;
    public static final int GENERAL = -3;
    public static final int ADS = -2;
    public static final int HEADER = -1;

    public static final int POST = 0;
    public static final int NOTE = 1;
    public static final int PRODUCT = 2;
    public static final int LOST  = 3;
    public static final int STORY  = 4;

    public static final int USER = 10;
    public static final int STORE = 11;

    public static final int COMMENT = 20;
    public static final int REACT = 21;

    public static final int CONVERSATION = 30;
    public static final int MESSAGE = 31;

    public static final int FORK = 40;
    public static final int RELATION = 41;


}
