package br.com.mapastartup.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.mapastartup.domain.Startup} entity.
 */
public class StartupDTO implements Serializable {

    private Long id;

    private String nome;

    private String segmento;

    private String descricao;

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

    public String getSegmento() {
        return segmento;
    }

    public void setSegmento(String segmento) {
        this.segmento = segmento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StartupDTO)) {
            return false;
        }

        StartupDTO startupDTO = (StartupDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, startupDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StartupDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", segmento='" + getSegmento() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
