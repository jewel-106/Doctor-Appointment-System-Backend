package com.appointment.demo.Repository;
import com.appointment.demo.model.Upazila;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface UpazilaRepository extends JpaRepository<Upazila, Long> {
    List<Upazila> findByDistrictId(Long districtId);
}