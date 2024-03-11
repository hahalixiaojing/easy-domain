package cn.easylib.domain.repository;

public interface IListByReadDAORepository<R,ListQuery>{
    R queryOneBy(ListQuery query, String returnClassName);
}
