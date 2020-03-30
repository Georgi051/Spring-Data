import entities.Address;
import entities.Employee;
import entities.Project;
import entities.Town;


import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class Engine implements Runnable {
    private final EntityManager entityManager;
    private Scanner scanner = new Scanner(System.in);

    public Engine(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void run() {
        //2. Remove Objects
         removeObjects();

        //3. Contains Employee
        // containsEmployee();

        //4. Employees with Salary Over 50 000
        // employeesWithSalaryOver50000();

        //5. Employees from Department
        // employeesFromDepartment();

        //6. Adding a New Address and Updating Employee
        // addNewAddressAndUpdateEmployee();

        //7. Addresses with Employee Count
        // addressesWithEmployeeCount();

        //8. Get Employee with Project
        // getEmployeeWithProject();

        //9. Find Latest 10 Projects
        // findLatest10Projects();

        //10. Increase Salaries
        // increaseSalaries();

        //11. Remove Towns
        // deleteTown();

        //12. Find Employees by First Name
        // findEmployeeByFirstName();

        //13. Employees Maximum Salaries
        // employeesMaximumSalaries();
    }

    private void employeesMaximumSalaries() {
        Query query = entityManager.createNativeQuery("SELECT d.name,  MAX(e.salary) FROM employees e " +
                "JOIN departments d ON e.department_id = d.department_id " +
                "GROUP BY e.department_id " +
                "HAVING MAX(e.salary) NOT BETWEEN 30000 AND 70000");

        List<Object[]> employees = query.getResultList();

        for (Object[] employee : employees) {
            System.out.printf("%s %s%n", employee[0], employee[1]);
        }
    }

    private void findEmployeeByFirstName() {
        System.out.println("Enter pattern:");
        String pattern = scanner.nextLine();

        entityManager.createQuery("SELECT e FROM Employee AS e " +
                "WHERE e.firstName  LIKE CONCAT(:data,'%')", Employee.class)
                .setParameter("data", pattern)
                .getResultStream()
                .forEach(e -> System.out.printf("%s %s - %s - ($%.2f)%n",
                        e.getFirstName(), e.getLastName(),
                        e.getJobTitle(), e.getSalary()));
    }

    private void deleteTown() {
        System.out.println("Enter town name:");
        String town = scanner.nextLine();
        int count = 0;

        List<Address> addresses = getAddressInfoForCurrTown(town);

        count = addresses.size();

        List<Employee> employees = getEmployeeInfoForCurrTown(town);

        Town currTown = getTown(town);

        entityManager.getTransaction().begin();
        employees.forEach(e -> e.setAddress(null));
        entityManager.flush();
        addresses.forEach(entityManager::remove);
        entityManager.flush();
        entityManager.remove(currTown);
        entityManager.flush();
        entityManager.getTransaction().commit();

        System.out.printf("%d %s in %s deleted", count
                , count == 1 ? "address" : "addresses", town);
    }

    private Town getTown(String town) {
        return entityManager.createQuery("SELECT t FROM  Town  as t " +
                "WHERE t.name = :townName", Town.class).setParameter("townName", town).getSingleResult();
    }

    private List<Employee> getEmployeeInfoForCurrTown(String town) {
        return entityManager.createQuery("SELECT e from  Employee  as e " +
                "WHERE e.address.town.name = :townName ", Employee.class)
                .setParameter("townName", town).getResultList();
    }

    private List<Address> getAddressInfoForCurrTown(String town) {
        return entityManager.createQuery("SELECT a FROM  Address as a " +
                "where  a.town.name = :townName", Address.class)
                .setParameter("townName", town)
                .getResultList();
    }

    private void increaseSalaries() {
        this.entityManager.getTransaction().begin();
        List<Employee> employees = entityManager.createQuery("SELECT e FROM Employee AS e " +
                " WHERE e.department.name IN ('Engineering', 'Tool Design', 'Marketing','Information Service' )", Employee.class)
                .getResultList();

        employees.forEach(entityManager::detach);
        employees.forEach(e -> {
            e.setSalary(e.getSalary().multiply(BigDecimal.valueOf(1.12)));
        });
        employees.forEach(entityManager::merge);
        this.entityManager.flush();
        this.entityManager.getTransaction().commit();
        employees.forEach(e -> System.out.printf("%s %s ($%.2f)%n", e.getFirstName(), e.getLastName(), e.getSalary()));
    }

    private void findLatest10Projects() {
        List<Project> projects = entityManager.createQuery("SELECT p FROM Project AS p  " +
                "ORDER BY p.startDate desc ", Project.class)
                .getResultStream()
                .limit(10)
                .sorted(Comparator.comparing(Project::getName))
                .collect(Collectors.toList());

        projects.forEach(p -> {
            System.out.printf("Project name: %s%n        Project Description: %s%n" +
                            "        Project Start Date: %s%n        Project End Date: %s%n",
                    p.getName(),
                    p.getDescription(),
                    p.getStartDate(),
                    p.getEndDate());
        });
    }

    private void getEmployeeWithProject() {
        System.out.println("Enter employee id:");
        int id = Integer.parseInt(scanner.nextLine());
        try {
            Employee employee =
                    entityManager.createQuery("SELECT e FROM Employee  AS e " +
                            "WHERE e.id = :employeeId", Employee.class)
                            .setParameter("employeeId", id)
                            .getSingleResult();

            System.out.printf("%s %s - %s%n", employee.getFirstName(), employee.getLastName(), employee.getJobTitle());
            employee.getProjects().stream()
                    .sorted(Comparator.comparing(Project::getName))
                    .forEach(p -> System.out.printf("      %s%n", p.getName()));
        } catch (Exception e) {
            System.out.println("No employee with this id");
        }
    }

    private void addressesWithEmployeeCount() {
        List<Address> addresses = entityManager.createQuery("SELECT  a FROM Address  AS a " +
                "ORDER BY  a.employees.size DESC", Address.class)
                .getResultStream()
                .limit(10)
                .collect(Collectors.toList());

        addresses.forEach(a -> System.out.printf("%s, %s - %d employees%n",
                a.getText(),
                a.getTown().getName(),
                a.getEmployees().size()));
    }

    private void addNewAddressAndUpdateEmployee() {
        System.out.println("Enter employee last name:");
        String lastName = scanner.nextLine();
        Employee employee;
        try {
            employee = entityManager.createQuery("SELECT  e FROM Employee AS e " +
                    "WHERE e.lastName = :name", Employee.class)
                    .setParameter("name", lastName)
                    .getSingleResult();

            Address address = addNewAddress("Vitoshka 15");
            entityManager.getTransaction().begin();
            entityManager.detach(employee);
            employee.setAddress(address);
            entityManager.merge(employee);
            entityManager.flush();
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("This employee does not exist!");
        }
    }

    private Address addNewAddress(String text) {
        Address address = new Address();
        address.setText(text);
        entityManager.getTransaction().begin();
        entityManager.persist(address);
        entityManager.getTransaction().commit();
        return address;
    }

    private void employeesFromDepartment() {
        List<Employee> employees = entityManager.createQuery("SELECT e FROM Employee AS e " +
                "WHERE e.department.id = 6 ORDER BY e.salary", Employee.class).getResultList();

        employees.forEach(e -> System.out.printf("%s %s from %s - $%.2f%n", e.getFirstName(), e.getLastName()
                , e.getDepartment().getName(), e.getSalary()));
    }

    private void employeesWithSalaryOver50000() {
        List<Employee> employees = entityManager.createQuery("SELECT e FROM  Employee AS e " +
                "WHERE e.salary > 50000", Employee.class).getResultList();
        employees.forEach(e -> System.out.println(e.getFirstName()));
    }

    private void containsEmployee() {
        System.out.println("Enter name:");
        String name = scanner.nextLine();
        try {
            entityManager.createQuery("SELECT e FROM Employee AS e " +
                    "WHERE  concat(e.firstName,' ',e.lastName) = :name ", Employee.class)
                    .setParameter("name", name)
                    .getSingleResult();
            System.out.println("Yes");
        } catch (Exception e) {
            System.out.println("No");
        }
    }

    private void removeObjects() {


        entityManager.getTransaction().begin();
        List<Town> towns = entityManager.createQuery("SELECT t FROM Town as t WHERE " +
                "length(t.name) > 5 ", Town.class)
                .getResultList();

        towns.forEach(this.entityManager::detach);
        towns.forEach(e -> e.setName(e.getName().toLowerCase()));
        towns.forEach(this.entityManager::merge);
        this.entityManager.flush();
        entityManager.getTransaction().commit();
    }
}
