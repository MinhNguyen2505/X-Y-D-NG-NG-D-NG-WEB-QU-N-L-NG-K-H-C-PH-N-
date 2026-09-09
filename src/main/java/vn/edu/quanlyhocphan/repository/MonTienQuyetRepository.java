package vn.edu.quanlyhocphan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.quanlyhocphan.entity.MonTienQuyet;

import java.util.List;

public interface MonTienQuyetRepository extends JpaRepository<MonTienQuyet, Long> {

    /**
     * Lay danh sach cac mon tien quyet cua 1 mon hoc.
     * Dung cho rang buoc nghiep vu #3 (kiem tra mon tien quyet).
     *
     * VD: monHocId = CTDL -> tra ve [OOP]
     * Service se kiem tra SV da hoan thanh tat ca cac mon nay chua.
     */
    @Query("SELECT mtq FROM MonTienQuyet mtq " +
           "JOIN FETCH mtq.monTienQuyet " +
           "WHERE mtq.monHoc.id = :monHocId")
    List<MonTienQuyet> findByMonHocIdWithTienQuyet(@Param("monHocId") Long monHocId);

    /**
     * Kiem tra nhanh: mon hoc nay co mon tien quyet nao khong.
     */
    boolean existsByMonHocId(Long monHocId);
}
