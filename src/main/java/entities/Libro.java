package entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Libro {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private Integer anioPublicacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "autor_id", nullable = false)
    private Autor autor;

    @ManyToMany
    @JoinTable(
            name = "libro_categoria",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<Categoria> categorias = new HashSet<>();

    public Libro() {}
    public Libro(String titulo, Integer anioPublicacion){
        this.titulo = titulo;
        this.anioPublicacion = anioPublicacion;
    }

    // helpers
    public void agregarCategoria(Categoria c){ categorias.add(c); c.getLibros().add(this); }
    public void quitarCategoria(Categoria c){ categorias.remove(c); c.getLibros().remove(this); }

    // getters/setters
    public Long getId(){ return id; }
    public String getTitulo(){ return titulo; }
    public void setTitulo(String titulo){ this.titulo = titulo; }
    public Integer getAnioPublicacion(){ return anioPublicacion; }
    public void setAnioPublicacion(Integer anioPublicacion){ this.anioPublicacion = anioPublicacion; }
    public Autor getAutor(){ return autor; }
    public void setAutor(Autor autor){ this.autor = autor; }
    public Set<Categoria> getCategorias(){ return categorias; }

    @Override public boolean equals(Object o){ return o instanceof Libro l && id!=null && id.equals(l.id); }
    @Override public int hashCode(){ return Objects.hashCode(id); }
}