import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class App {
    private static String PROJECT_NAME = "hospital_db";
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PROJECT_NAME);
        EntityManager entityManager = emf.createEntityManager();
        Engine engine = new Engine(entityManager);
        engine.run();
    }
}
