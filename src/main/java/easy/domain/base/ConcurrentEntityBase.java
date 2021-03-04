package easy.domain.base;

public abstract class ConcurrentEntityBase<T> extends EntityBase<T> {
    private long oldVersion = 1;

    public long getOldVersion() {
        return oldVersion;
    }

    protected void setOldVersion(long oldVersion) {
        this.oldVersion = oldVersion;
    }

    public long getNewVersion() {
        long v = this.oldVersion;
        return ++v;
    }
}
