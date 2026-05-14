package com.serasa.weighing.repository;

import com.serasa.weighing.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface BranchRepository extends JpaRepository<Branch, UUID> {
}
