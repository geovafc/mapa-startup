package br.com.mapastartup.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.mapastartup.IntegrationTest;
import br.com.mapastartup.domain.Membro;
import br.com.mapastartup.repository.MembroRepository;
import br.com.mapastartup.service.dto.MembroDTO;
import br.com.mapastartup.service.mapper.MembroMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MembroResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MembroResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_FUNCAO = "AAAAAAAAAA";
    private static final String UPDATED_FUNCAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/membros";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MembroRepository membroRepository;

    @Autowired
    private MembroMapper membroMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMembroMockMvc;

    private Membro membro;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Membro createEntity(EntityManager em) {
        Membro membro = new Membro().nome(DEFAULT_NOME).funcao(DEFAULT_FUNCAO);
        return membro;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Membro createUpdatedEntity(EntityManager em) {
        Membro membro = new Membro().nome(UPDATED_NOME).funcao(UPDATED_FUNCAO);
        return membro;
    }

    @BeforeEach
    public void initTest() {
        membro = createEntity(em);
    }

    @Test
    @Transactional
    void createMembro() throws Exception {
        int databaseSizeBeforeCreate = membroRepository.findAll().size();
        // Create the Membro
        MembroDTO membroDTO = membroMapper.toDto(membro);
        restMembroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(membroDTO)))
            .andExpect(status().isCreated());

        // Validate the Membro in the database
        List<Membro> membroList = membroRepository.findAll();
        assertThat(membroList).hasSize(databaseSizeBeforeCreate + 1);
        Membro testMembro = membroList.get(membroList.size() - 1);
        assertThat(testMembro.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testMembro.getFuncao()).isEqualTo(DEFAULT_FUNCAO);
    }

    @Test
    @Transactional
    void createMembroWithExistingId() throws Exception {
        // Create the Membro with an existing ID
        membro.setId(1L);
        MembroDTO membroDTO = membroMapper.toDto(membro);

        int databaseSizeBeforeCreate = membroRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMembroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(membroDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Membro in the database
        List<Membro> membroList = membroRepository.findAll();
        assertThat(membroList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMembros() throws Exception {
        // Initialize the database
        membroRepository.saveAndFlush(membro);

        // Get all the membroList
        restMembroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(membro.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].funcao").value(hasItem(DEFAULT_FUNCAO)));
    }

    @Test
    @Transactional
    void getMembro() throws Exception {
        // Initialize the database
        membroRepository.saveAndFlush(membro);

        // Get the membro
        restMembroMockMvc
            .perform(get(ENTITY_API_URL_ID, membro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(membro.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.funcao").value(DEFAULT_FUNCAO));
    }

    @Test
    @Transactional
    void getNonExistingMembro() throws Exception {
        // Get the membro
        restMembroMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMembro() throws Exception {
        // Initialize the database
        membroRepository.saveAndFlush(membro);

        int databaseSizeBeforeUpdate = membroRepository.findAll().size();

        // Update the membro
        Membro updatedMembro = membroRepository.findById(membro.getId()).get();
        // Disconnect from session so that the updates on updatedMembro are not directly saved in db
        em.detach(updatedMembro);
        updatedMembro.nome(UPDATED_NOME).funcao(UPDATED_FUNCAO);
        MembroDTO membroDTO = membroMapper.toDto(updatedMembro);

        restMembroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, membroDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membroDTO))
            )
            .andExpect(status().isOk());

        // Validate the Membro in the database
        List<Membro> membroList = membroRepository.findAll();
        assertThat(membroList).hasSize(databaseSizeBeforeUpdate);
        Membro testMembro = membroList.get(membroList.size() - 1);
        assertThat(testMembro.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testMembro.getFuncao()).isEqualTo(UPDATED_FUNCAO);
    }

    @Test
    @Transactional
    void putNonExistingMembro() throws Exception {
        int databaseSizeBeforeUpdate = membroRepository.findAll().size();
        membro.setId(count.incrementAndGet());

        // Create the Membro
        MembroDTO membroDTO = membroMapper.toDto(membro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMembroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, membroDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Membro in the database
        List<Membro> membroList = membroRepository.findAll();
        assertThat(membroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMembro() throws Exception {
        int databaseSizeBeforeUpdate = membroRepository.findAll().size();
        membro.setId(count.incrementAndGet());

        // Create the Membro
        MembroDTO membroDTO = membroMapper.toDto(membro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Membro in the database
        List<Membro> membroList = membroRepository.findAll();
        assertThat(membroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMembro() throws Exception {
        int databaseSizeBeforeUpdate = membroRepository.findAll().size();
        membro.setId(count.incrementAndGet());

        // Create the Membro
        MembroDTO membroDTO = membroMapper.toDto(membro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembroMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(membroDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Membro in the database
        List<Membro> membroList = membroRepository.findAll();
        assertThat(membroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMembroWithPatch() throws Exception {
        // Initialize the database
        membroRepository.saveAndFlush(membro);

        int databaseSizeBeforeUpdate = membroRepository.findAll().size();

        // Update the membro using partial update
        Membro partialUpdatedMembro = new Membro();
        partialUpdatedMembro.setId(membro.getId());

        partialUpdatedMembro.funcao(UPDATED_FUNCAO);

        restMembroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMembro.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMembro))
            )
            .andExpect(status().isOk());

        // Validate the Membro in the database
        List<Membro> membroList = membroRepository.findAll();
        assertThat(membroList).hasSize(databaseSizeBeforeUpdate);
        Membro testMembro = membroList.get(membroList.size() - 1);
        assertThat(testMembro.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testMembro.getFuncao()).isEqualTo(UPDATED_FUNCAO);
    }

    @Test
    @Transactional
    void fullUpdateMembroWithPatch() throws Exception {
        // Initialize the database
        membroRepository.saveAndFlush(membro);

        int databaseSizeBeforeUpdate = membroRepository.findAll().size();

        // Update the membro using partial update
        Membro partialUpdatedMembro = new Membro();
        partialUpdatedMembro.setId(membro.getId());

        partialUpdatedMembro.nome(UPDATED_NOME).funcao(UPDATED_FUNCAO);

        restMembroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMembro.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMembro))
            )
            .andExpect(status().isOk());

        // Validate the Membro in the database
        List<Membro> membroList = membroRepository.findAll();
        assertThat(membroList).hasSize(databaseSizeBeforeUpdate);
        Membro testMembro = membroList.get(membroList.size() - 1);
        assertThat(testMembro.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testMembro.getFuncao()).isEqualTo(UPDATED_FUNCAO);
    }

    @Test
    @Transactional
    void patchNonExistingMembro() throws Exception {
        int databaseSizeBeforeUpdate = membroRepository.findAll().size();
        membro.setId(count.incrementAndGet());

        // Create the Membro
        MembroDTO membroDTO = membroMapper.toDto(membro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMembroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, membroDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(membroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Membro in the database
        List<Membro> membroList = membroRepository.findAll();
        assertThat(membroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMembro() throws Exception {
        int databaseSizeBeforeUpdate = membroRepository.findAll().size();
        membro.setId(count.incrementAndGet());

        // Create the Membro
        MembroDTO membroDTO = membroMapper.toDto(membro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(membroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Membro in the database
        List<Membro> membroList = membroRepository.findAll();
        assertThat(membroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMembro() throws Exception {
        int databaseSizeBeforeUpdate = membroRepository.findAll().size();
        membro.setId(count.incrementAndGet());

        // Create the Membro
        MembroDTO membroDTO = membroMapper.toDto(membro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembroMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(membroDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Membro in the database
        List<Membro> membroList = membroRepository.findAll();
        assertThat(membroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMembro() throws Exception {
        // Initialize the database
        membroRepository.saveAndFlush(membro);

        int databaseSizeBeforeDelete = membroRepository.findAll().size();

        // Delete the membro
        restMembroMockMvc
            .perform(delete(ENTITY_API_URL_ID, membro.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Membro> membroList = membroRepository.findAll();
        assertThat(membroList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
