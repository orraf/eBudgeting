package biz.thaicom.eBudgeting.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import biz.thaicom.eBudgeting.models.hrx.Organization;

public interface OrganizationRepository extends JpaSpecificationExecutor<Long>,
		PagingAndSortingRepository<Organization, Long> {

	public List<Organization> findAllByNameLikeOrderByNameAsc(String name);
}
