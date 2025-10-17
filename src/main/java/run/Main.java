package run;

import entities.Autor;
import entities.Categoria;
import entities.Libro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("biblioPU");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // 1) Crear categorías (se persisten explícitamente)
            Categoria ficcion = new Categoria("Ficción");
            Categoria ciencia = new Categoria("Ciencia");
            em.persist(ficcion);
            em.persist(ciencia);

            // 2) Crear autores
            Autor a1 = new Autor("Isabel Allende", "Chile", LocalDate.of(1942, 8, 2));
            Autor a2 = new Autor("Carl Sagan", "Estados Unidos", LocalDate.of(1934, 11, 9));

            // 3) Crear libros
            Libro l1 = new Libro("La casa de los espíritus", 1982);
            Libro l2 = new Libro("De amor y de sombra", 1984);
            Libro l3 = new Libro("Cosmos", 1980);

            // 4) Relacionar Autor ↔ Libro (dos libros del mismo autor)
            a1.agregarLibro(l1);   // helper mantiene ambos lados
            a1.agregarLibro(l2);
            a2.agregarLibro(l3);

            // 5) Relacionar Libro ↔ Categoría
            l1.agregarCategoria(ficcion);
            l2.agregarCategoria(ficcion);
            l3.agregarCategoria(ciencia);

            // 6) Persistir autores (por cascade ALL en Autor → Libro, guarda también los libros)
            em.persist(a1);
            em.persist(a2);

            em.getTransaction().commit();

            // 7) Consulta: listar libros con autor y categorías (JOIN FETCH para evitar N+1)
            List<Libro> resultados = em.createQuery(
                    "SELECT DISTINCT l FROM Libro l " +
                            "JOIN FETCH l.autor " +
                            "LEFT JOIN FETCH l.categorias " +
                            "ORDER BY l.titulo", Libro.class
            ).getResultList();

            System.out.println("\n=== LIBROS, AUTORES Y CATEGORÍAS ===");
            for (Libro l : resultados) {
                String cats = l.getCategorias().stream()
                        .map(Categoria::getNombre)
                        .sorted()
                        .reduce((c1, c2) -> c1 + ", " + c2)
                        .orElse("(sin categorías)");
                System.out.printf("- %s (%d) — Autor: %s — Categorías: %s%n",
                        l.getTitulo(), l.getAnioPublicacion(), l.getAutor().getNombre(), cats);
            }

        } finally {
            if (em != null) em.close();
            if (emf != null) emf.close();
        }
    }
}