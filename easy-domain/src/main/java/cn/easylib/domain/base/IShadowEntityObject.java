package cn.easylib.domain.base;

public interface IShadowEntityObject {

    default IBoxValueObject getShadowEntityObject() {
        return null;
    }
}
