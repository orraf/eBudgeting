package biz.thaicom.eBudgeting.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import biz.thaicom.eBudgeting.models.bgt.AssetBudget;

public interface AssetBudgetRepository extends JpaRepository<AssetBudget, Long> {

	public List<AssetBudget> findAllByKind_IdOrderByIdAsc(Long kindId);
	
}
