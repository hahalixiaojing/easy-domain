package easy.domain.rules;

public class Pair {

    private static final Pair pair = new Pair(true, new Object[0]);

    public Pair(boolean isSatisfy, Object[] params) {
        this.isSatisfy = isSatisfy;
        this.params = params;
        this.autoFormat = true;
    }

    public Pair(boolean isSatisfy, Object[] params, boolean isAutoFormat) {
        this(isSatisfy, params);
        this.autoFormat = isAutoFormat;
    }


    private final boolean isSatisfy;
    private final Object[] params;
    private boolean autoFormat;

    public boolean isSatisfy() {
        return isSatisfy;
    }

    public Object[] getParams() {
        return params;
    }

    public static Pair ok() {
        return pair;
    }

    public boolean isAutoFormat() {
        return autoFormat;
    }
}
