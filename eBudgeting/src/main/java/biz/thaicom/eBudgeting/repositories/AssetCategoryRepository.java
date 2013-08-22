package biz.thaicom.eBudgeting.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import biz.thaicom.eBudgeting.models.bgt.AssetCategory;

public interface AssetCategoryRepository extends JpaSpecificationExecutor<AssetCategory>,
		PagingAndSortingRepository<AssetCategory, Long> {
	
	@Query("" +
			"FROM AssetCategory " +
			"ORDER BY name asc ")
	List<AssetCategory> findAllSortedByName();
}
