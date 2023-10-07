package cn.easylib.domain.base;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * association property="contractList" resultMap="ContractList"
 *
 *  resultMap id="ContractList" type="easy.domain.base.ValueObjectTraceCollection"
 *         collection property="initCollection" ofType="BMallContract" select="BMallContractGroup.selectContractList"
 *                     column="id"
 *     /resultMap
 * @param <T>
 */
public class ValueObjectTraceCollection<T> {


    private final List<T> initCollection = new ArrayList<>();
    private final List<T> appendCollection = new ArrayList<>();
    private final List<T> removeCollection = new ArrayList<>();

    public ValueObjectTraceCollection() {
    }

    public ValueObjectTraceCollection(List<T> initCollection) {
        this.getInitCollection().addAll(initCollection);
    }

    public void append(T item) {
        this.getAppendCollection().add(item);
    }

    public void append(List<T> items) {
        this.getAppendCollection().addAll(items);
    }

    public void clearAndAppend(List<T> items) {
        this.removeCollection.addAll(this.getInitCollection());
        this.getInitCollection().clear();
        this.getAppendCollection().clear();
        this.getAppendCollection().addAll(items);
    }

    public List<T> removeItems(Predicate<? super T> predicate) {
        List<T> appendCollectionList = this.getAppendCollection().stream().filter(predicate).collect(Collectors.toList());
        List<T> needRemoveList = this.getInitCollection().stream().filter(predicate).collect(Collectors.toList());

        this.getInitCollection().removeAll(needRemoveList);
        this.getAppendCollection().removeAll(appendCollectionList);
        this.getRemoveCollection().addAll(needRemoveList);

        ArrayList<T> allRemoved = new ArrayList<>();
        allRemoved.addAll(appendCollectionList);
        allRemoved.addAll(needRemoveList);

        return allRemoved;
    }

    /**
     * 清理所有的项
     */
    public void removeAll() {
        this.getRemoveCollection().addAll(this.getInitCollection());
        this.getInitCollection().clear();
        this.getAppendCollection().clear();
    }

    public List<T> getAppendedItems() {
        return new ArrayList<>(this.getAppendCollection());
    }

    public List<T> getRemovedItems() {
        return new ArrayList<>(this.getRemoveCollection());
    }

    protected List<T> getInitCollection() {
        return this.initCollection;
    }

    protected List<T> getAppendCollection() {
        return this.appendCollection;
    }

    protected List<T> getRemoveCollection() {
        return this.removeCollection;
    }

    public List<T> getAllItems() {
        final int count = this.getAppendCollection().size() + this.getInitCollection().size();
        ArrayList<T> allItems = new ArrayList<>(count);
        allItems.addAll(this.getInitCollection());
        allItems.addAll(this.getAppendCollection());
        return allItems;
    }

    public void process(IValueObjectTraceCollectionHandler<T> handler) {
        handler.process(this.getAppendedItems(), this.getRemovedItems());
    }
}
