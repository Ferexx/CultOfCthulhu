package com.cultofcthulhu.projectallocation.models.data;

import com.cultofcthulhu.projectallocation.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ProjectDAO extends JpaRepository<Project, Integer> {
}
