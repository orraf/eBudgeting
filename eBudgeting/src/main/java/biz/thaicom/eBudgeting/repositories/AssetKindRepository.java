package biz.thaicom.eBudgeting.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import biz.thaicom.eBudgeting.models.bgt.AssetKind;

public interface AssetKindRepository extends JpaRepository<AssetKind, Long> {

	List<AssetKind> findAllByType_idOrderByIdAsc(Long typeId);

}
