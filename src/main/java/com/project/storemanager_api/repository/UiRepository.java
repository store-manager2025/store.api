package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.ui.entity.UiLayout;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UiRepository {

    void saveUi(UiLayout layout);
}
