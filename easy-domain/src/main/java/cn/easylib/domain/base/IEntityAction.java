package cn.easylib.domain.base;

/**
 * @author lixiaojing10
 */
public interface IEntityAction {

    default EntityAction entityActions() {
        return null;
    }
}
