package biz.thaicom.eBudgeting.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import biz.thaicom.eBudgeting.models.bgt.AssetBudget;
import biz.thaicom.eBudgeting.models.bgt.AssetCategory;

public interface AssetBudgetRepository extends JpaRepository<AssetBudget, Long> {

	public List<AssetBudget> findAllByKind_IdOrderByIdAsc(Long kindId);


	@Modifying
	@Query(""
			+ "UPDATE AssetBudget assetBudget "
			+ "SET assetBudget.category = null "
			+ "WHERE assetBudget.category = ?1 ")
	public void removeCategory(AssetCategory category);
	
}
