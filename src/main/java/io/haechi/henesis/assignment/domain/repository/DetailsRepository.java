package io.haechi.henesis.assignment.domain.repository;

import io.haechi.henesis.assignment.domain.Details;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface DetailsRepository extends JpaRepository<Details, String> {

    @Transactional
    @Query("SELECT d FROM Details d WHERE d.detailId = :detailId")
    Optional<Details> getDetailById(@Param("detailId") final int detailId);

    @Transactional
    @Modifying
    @Query("UPDATE Details d SET d.status=:details.status, d.updatedAt =:details.updatedAt WHERE d.detailId = :details.detailId")
    void update(@Param("details") final Details details);
}
