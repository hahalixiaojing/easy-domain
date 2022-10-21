package cn.easylib.domain.base;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 集合变更跟踪处理类，用于实体集合集合类型属性变更处理
 * 该集合处理类不能更新现有项目
 * 只用来处理值类型集合
 *
 * @author lixiaojing10
 * @date 2021/9/2 9:36 下午
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

    /**
     * 增加一个新的项
     *
     * @param item
     */
    public void append(T item) {
        this.getAppendCollection().add(item);
    }

    /**
     * 增加一组新的项
     *
     * @param items
     */
    public void append(List<T> items) {
        this.getAppendCollection().addAll(items);
    }

    /**
     * 清理现有的项，并且追加
     *
     * @param items
     */
    public void clearAndAppend(List<T> items) {
        this.removeCollection.addAll(this.getInitCollection());
        this.getInitCollection().clear();
        this.getAppendCollection().clear();
        this.getAppendCollection().addAll(items);
    }

    /**
     * 移除满足条件的项，并返回移除的项
     *
     * @param predicate
     * @return
     */
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

    /**
     * 获得新增的项
     *
     * @return
     */
    public List<T> getAppendedItems() {
        return new ArrayList<>(this.getAppendCollection());
    }

    /**
     * 获得需要移除的项目
     *
     * @return
     */
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

    /**
     * 获得全部的项目
     *
     * @return
     */
    public List<T> getAllItems() {
        final int count = this.getAppendCollection().size() + this.getInitCollection().size();
        ArrayList<T> allItems = new ArrayList<>(count);
        allItems.addAll(this.getInitCollection());
        allItems.addAll(this.getAppendCollection());
        return allItems;
    }

    public void process(IValueObjectTraceCollectionHandler<T> handler) {
        handler.process(this.getAppendedItems(),this.getRemovedItems());
    }
}
