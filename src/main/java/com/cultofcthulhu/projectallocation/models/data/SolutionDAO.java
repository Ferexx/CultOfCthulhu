package com.cultofcthulhu.projectallocation.models.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface SolutionDAO extends JpaRepository<Solution, Integer> {
}
