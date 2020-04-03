package hiberspring.repository;

import hiberspring.domain.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer> {

    @Query("select e from Employee as e where size(e.branch.products) > 0")
    List<Employee> findAllByBranchWithProducts();

    Employee findEmployeeByCard_Number(String cardNum);


}
