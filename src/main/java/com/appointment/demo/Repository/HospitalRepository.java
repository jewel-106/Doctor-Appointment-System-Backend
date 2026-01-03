package com.appointment.demo.Repository;
import com.appointment.demo.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    Optional<Hospital> findByCode(String code);
    List<Hospital> findByIsActiveTrue();
    Optional<Hospital> findFirstByIsActiveTrueOrderByIdAsc();
    List<Hospital> findByDivisionIdAndIsActiveTrue(Long divisionId);
    List<Hospital> findByDivisionIdAndDistrictIdAndIsActiveTrue(Long divisionId, Long districtId);
    List<Hospital> findByDivisionIdAndDistrictIdAndUpazilaIdAndIsActiveTrue(Long divisionId, Long districtId, Long upazilaId);
}