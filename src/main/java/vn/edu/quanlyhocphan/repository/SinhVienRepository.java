package vn.edu.quanlyhocphan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.quanlyhocphan.entity.SinhVien;
import vn.edu.quanlyhocphan.enums.TrangThaiSinhVien;

import java.util.List;
import java.util.Optional;

public interface SinhVienRepository extends JpaRepository<SinhVien, Long> {

    Optional<SinhVien> findByMssv(String mssv);

    Optional<SinhVien> findByEmail(String email);

    /** Lay SinhVien kem Nganh theo email (tranh LazyInitializationException) */
    @Query("SELECT sv FROM SinhVien sv LEFT JOIN FETCH sv.nganh WHERE sv.email = :email")
    Optional<SinhVien> findByEmailWithNganh(@Param("email") String email);

    boolean existsByMssv(String mssv);

    boolean existsByEmail(String email);

    List<SinhVien> findByTrangThai(TrangThaiSinhVien trangThai);

    /** Lay SV kem Nganh trong 1 query (tranh N+1) */
    @Query("SELECT sv FROM SinhVien sv LEFT JOIN FETCH sv.nganh WHERE sv.id = :id")
    Optional<SinhVien> findByIdWithNganh(@Param("id") Long id);

    /** Lay tat ca SV kem Nganh — dung cho admin danh sach (tranh LazyInit) */
    @Query("SELECT sv FROM SinhVien sv LEFT JOIN FETCH sv.nganh ORDER BY sv.mssv ASC")
    List<SinhVien> findAllWithNganh();

    /** Tim SV theo nganh */
    List<SinhVien> findByNganhId(Long nganhId);
}
