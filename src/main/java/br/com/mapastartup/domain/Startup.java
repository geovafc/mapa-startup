package br.com.mapastartup.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Startup.
 */
@Entity
@Table(name = "startup")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Startup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "segmento")
    private String segmento;

    @Column(name = "descricao")
    private String descricao;

    @OneToMany(mappedBy = "startup")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "startup" }, allowSetters = true)
    private Set<Membro> membros = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Startup id(Long id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return this.nome;
    }

    public Startup nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSegmento() {
        return this.segmento;
    }

    public Startup segmento(String segmento) {
        this.segmento = segmento;
        return this;
    }

    public void setSegmento(String segmento) {
        this.segmento = segmento;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Startup descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<Membro> getMembros() {
        return this.membros;
    }

    public Startup membros(Set<Membro> membros) {
        this.setMembros(membros);
        return this;
    }

    public Startup addMembro(Membro membro) {
        this.membros.add(membro);
        membro.setStartup(this);
        return this;
    }

    public Startup removeMembro(Membro membro) {
        this.membros.remove(membro);
        membro.setStartup(null);
        return this;
    }

    public void setMembros(Set<Membro> membros) {
        if (this.membros != null) {
            this.membros.forEach(i -> i.setStartup(null));
        }
        if (membros != null) {
            membros.forEach(i -> i.setStartup(this));
        }
        this.membros = membros;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Startup)) {
            return false;
        }
        return id != null && id.equals(((Startup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Startup{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", segmento='" + getSegmento() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
