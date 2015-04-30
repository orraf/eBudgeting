package biz.thaicom.eBudgeting.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import biz.thaicom.eBudgeting.models.bgt.AssetBudget;
import biz.thaicom.eBudgeting.models.bgt.AssetCategory;
import biz.thaicom.eBudgeting.models.bgt.AssetPOExternal;

public interface AssetBudgetRepository extends JpaRepository<AssetBudget, Long> {

	public List<AssetBudget> findAllByKind_IdOrderByIdAsc(Long kindId);


	@Modifying
	@Query(""
			+ "UPDATE AssetBudget assetBudget "
			+ "SET assetBudget.category = null "
			+ "WHERE assetBudget.category = ?1 ")
	public void removeCategory(AssetCategory category);
	
	
	@Query(""
			+ "SELECT po.poContract, sum(po.assetAmount)"
			+ "FROM AssetPOExternal po "
			+ "WHERE po.poContract like ?1 "
			+ "GROUP BY po.poContract "
			+ "")
	public List<Object[]> findAssetAmountByPoNumber(String poNumber);

	@Query(""
			+ "SELECT po "
			+ "FROM AssetPOExternal po "
			+ "WHERE po.poContract like ?1 "
			+ "	AND po.fiscalYear = ?2 "
			+ "ORDER BY faCode asc"
			+ "")
	public List<AssetPOExternal> findAssetAllocatoinBudgetSigendByPOAndFiscalYear(
			String poNumber, Integer fiscalYear);
}
