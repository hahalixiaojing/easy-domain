package easy.domain.base;

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
public class ValueObjectCollection<T> {

    private final List<T> initCollection = new ArrayList<>();
    private final List<T> appendCollection = new ArrayList<>();
    private final List<T> removeCollection = new ArrayList<>();

    public ValueObjectCollection() {
    }

    public ValueObjectCollection(List<T> initCollection) {
        this.initCollection.addAll(initCollection);
    }

    /**
     * 增加一个新的项
     *
     * @param item
     */
    public void append(T item) {
        this.appendCollection.add(item);
    }

    /**
     * 增加一组新的项
     *
     * @param items
     */
    public void append(List<T> items) {
        this.appendCollection.addAll(items);
    }

    /**
     * 清理现有的项，并且追加
     *
     * @param items
     */
    public void clearAndAppend(List<T> items) {
        this.initCollection.clear();
        this.appendCollection.clear();
        this.appendCollection.addAll(items);
    }

    /**
     * 移除满足条件的项
     *
     * @param predicate
     * @return
     */
    public List<T> removeItems(Predicate<? super T> predicate) {
        List<T> needRemoveList = this.initCollection.stream().filter(predicate).collect(Collectors.toList());
        this.initCollection.removeAll(needRemoveList);
        this.removeCollection.addAll(needRemoveList);
        return needRemoveList;
    }

    public void replaceItem(T newItem, Predicate<? super T> predicate) {
        this.initCollection.removeIf(predicate);
        this.appendCollection.add(newItem);
    }

    /**
     * 清理所用的项
     */
    public void clearAll() {
        this.initCollection.clear();
        this.appendCollection.clear();
    }

    /**
     * 获得新增的项
     *
     * @return
     */
    public List<T> getAppendedItems() {
        return new ArrayList<>(this.appendCollection);
    }

    /**
     * 获得需要移除的项目
     *
     * @return
     */
    public List<T> getRemovedItems() {
        return new ArrayList(this.removeCollection);
    }

    /**
     * 获得全部的项目
     *
     * @return
     */
    public List<T> getAllItems() {
        final int count = this.appendCollection.size() + this.initCollection.size();
        ArrayList<T> allItems = new ArrayList<>(count);
        allItems.addAll(this.initCollection);
        allItems.addAll(this.appendCollection);
        return allItems;
    }


    /**
     * 是否数据项目为空
     *
     * @return
     */
    public boolean isEmpty() {
        return this.initCollection.isEmpty() && this.appendCollection.isEmpty();
    }
}
