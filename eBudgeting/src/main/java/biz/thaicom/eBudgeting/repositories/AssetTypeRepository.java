package biz.thaicom.eBudgeting.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import biz.thaicom.eBudgeting.models.bgt.AssetType;

public interface AssetTypeRepository extends JpaRepository<AssetType, Long> {

	List<AssetType> findAllByGroup_IdOrderByIdAsc(Long groupId);

}
