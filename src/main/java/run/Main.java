package run;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        System.out.println("PU=biblioPU");
        System.out.println("persistence.xml = " +
                ClassLoader.getSystemClassLoader().getResource("META-INF/persistence.xml"));

        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("biblioPU");
            em = emf.createEntityManager();

            em.getTransaction().begin();
            Object one = em.createNativeQuery("SELECT 1").getSingleResult();
            em.getTransaction().commit();

            System.out.println("Conexión OK, SELECT 1 -> " + one);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) try { em.close(); } catch (Exception ignored) {}
            if (emf != null) try { emf.close(); } catch (Exception ignored) {}
        }
    }
}
