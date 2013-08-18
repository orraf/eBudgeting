package biz.thaicom.eBudgeting.repositories;

import java.util.List;

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
//			"	AND organization.id > 110000000 " +
			"	AND organization.name like ?1 " +
			"ORDER BY organization.code asc ")
	public List<Organization> findAllByProvinces(String query);
	
	@Query(""
			+ " SELECT org "
			+ " FROM Organization org "
			+ " WHERE org.parent.id=0 "
			+ " 	AND org.id > 110000000 "
			+ " ORDER BY org.id ")
	public List<Organization> findAllProvinces();

	@Query("" +
			"SELECT organization " +
			"FROM Organization organization " +
			"WHERE organization.parent.id = 0 " +
			"	AND organization.name like ?1 " +
			"ORDER BY organization.code asc ") 
	public List<Organization> findAllTopLevelByNameLike(String query);

	@Query("" +
			"SELECT org " +
			"FROM Organization org " +
			"WHERE org.id = ?1 ")
	public Organization findOneById(Long id);

	@Query(""
			+ "SELECT org "
			+ "FROM Organization org "
			+ "	LEFT JOIN FETCH org.children "
			+ " ORDER BY org.id asc")
	public List<Organization> findAllLeftJoinChildren();

	@Query("" +
			"SELECT org " +
			"FROM Organization org " +
			"WHERE org.id = 0 ")
	public Organization findRoot();
}
