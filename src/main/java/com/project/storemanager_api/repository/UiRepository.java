package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.ui.dto.request.ChangeStyleRequestDto;
import com.project.storemanager_api.domain.ui.entity.UiLayout;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface UiRepository {

    void saveUi(UiLayout layout);

    Optional<UiLayout> findById(Long uiId);

    void updateUi(ChangeStyleRequestDto dto);
}
