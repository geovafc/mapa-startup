package br.com.mapastartup.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Membro.
 */
@Entity
@Table(name = "membro")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Membro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "funcao")
    private String funcao;

    @ManyToOne
    @JsonIgnoreProperties(value = { "membros" }, allowSetters = true)
    private Startup startup;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Membro id(Long id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return this.nome;
    }

    public Membro nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFuncao() {
        return this.funcao;
    }

    public Membro funcao(String funcao) {
        this.funcao = funcao;
        return this;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public Startup getStartup() {
        return this.startup;
    }

    public Membro startup(Startup startup) {
        this.setStartup(startup);
        return this;
    }

    public void setStartup(Startup startup) {
        this.startup = startup;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Membro)) {
            return false;
        }
        return id != null && id.equals(((Membro) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Membro{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", funcao='" + getFuncao() + "'" +
            "}";
    }
}
