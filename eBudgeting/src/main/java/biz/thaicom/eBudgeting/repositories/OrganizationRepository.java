package biz.thaicom.eBudgeting.repositories;

import java.util.List;

import javax.persistence.Entity;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import biz.thaicom.eBudgeting.models.hrx.Organization;

public interface OrganizationRepository extends JpaSpecificationExecutor<Long>,
		PagingAndSortingRepository<Organization, Long> {

	public List<Organization> findAllByNameLikeOrderByNameAsc(String name);

	public List<Organization> findAllByNameLikeAndCodeLikeOrderByNameAsc(String name, String code);

	
	@Query("" +
			"SELECT relation.owners " +
			"FROM ObjectiveOwnerRelation relation " +
			"WHERE relation.objective.id = ?1 " +
			"")
	public List<Organization> findAllByOwningObjective(Long objectiveId);

	public List<Organization> findAllByNameLikeAndParent_IdOrderByNameAsc(
			String query, Long parentId);

	@Query("" +
			"SELECT organization " +
			"FROM Organization organization " +
			"WHERE organization.parent.id = 0 " +
			"	AND organization.id > 110000000 ")
	public List<Organization> findAllByProvinces();
}
