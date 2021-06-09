package br.com.mapastartup.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.mapastartup.domain.Membro} entity.
 */
public class MembroDTO implements Serializable {

    private Long id;

    private String nome;

    private String funcao;

    private StartupDTO startup;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public StartupDTO getStartup() {
        return startup;
    }

    public void setStartup(StartupDTO startup) {
        this.startup = startup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MembroDTO)) {
            return false;
        }

        MembroDTO membroDTO = (MembroDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, membroDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MembroDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", funcao='" + getFuncao() + "'" +
            ", startup=" + getStartup() +
            "}";
    }
}
