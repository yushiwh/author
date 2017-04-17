package com.ys.service;

import com.ys.bean.UserInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yushi on 2017/4/15.
 */
public interface UserInfoRepository extends CrudRepository<UserInfo, Integer> {
    UserInfo findUserInfoById(int id);

    List<UserInfo> findUserInfoByRole(String role);

    @Query(value = "select * from t_user limit ?1", nativeQuery = true)
    List<UserInfo> findAllUsersByCount(int count);


    UserInfo findUserInfoByName(String name);
}
