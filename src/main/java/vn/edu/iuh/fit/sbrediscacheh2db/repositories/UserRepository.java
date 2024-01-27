package vn.edu.iuh.fit.sbrediscacheh2db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.sbrediscacheh2db.models.User;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {

}
