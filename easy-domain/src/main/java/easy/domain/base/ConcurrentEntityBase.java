package easy.domain.base;

public abstract class ConcurrentEntityBase<T> extends EntityBase<T> {
    private long oldVersion = 1;
    private boolean newVersionIsGenerate = false;
    private long newVersion = 0;

    public long getOldVersion() {
        return oldVersion;
    }

    protected void setOldVersion(long oldVersion) {
        this.oldVersion = oldVersion;
    }

    public long getNewVersion() {
        if (!this.newVersionIsGenerate) {

            long v = this.oldVersion;
            this.newVersion = ++v;
            this.newVersionIsGenerate = true;
        }
        return this.newVersion;
    }
}
