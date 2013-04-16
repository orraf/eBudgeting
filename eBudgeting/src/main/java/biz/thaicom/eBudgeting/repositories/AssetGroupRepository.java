package biz.thaicom.eBudgeting.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import biz.thaicom.eBudgeting.models.bgt.AssetGroup;

public interface AssetGroupRepository extends JpaRepository<AssetGroup, Long> {
	@Query("" +
			"SELECT assetGroup " +
			"FROM AssetGroup assetGroup " +
			"ORDER BY assetGroup.id asc")
	public List<AssetGroup> findAllOrderByIdAsc();
}
