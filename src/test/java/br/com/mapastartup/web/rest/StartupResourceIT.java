package br.com.mapastartup.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.mapastartup.IntegrationTest;
import br.com.mapastartup.domain.Startup;
import br.com.mapastartup.repository.StartupRepository;
import br.com.mapastartup.service.dto.StartupDTO;
import br.com.mapastartup.service.mapper.StartupMapper;
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
 * Integration tests for the {@link StartupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StartupResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_SEGMENTO = "AAAAAAAAAA";
    private static final String UPDATED_SEGMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/startups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StartupRepository startupRepository;

    @Autowired
    private StartupMapper startupMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStartupMockMvc;

    private Startup startup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Startup createEntity(EntityManager em) {
        Startup startup = new Startup().nome(DEFAULT_NOME).segmento(DEFAULT_SEGMENTO).descricao(DEFAULT_DESCRICAO);
        return startup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Startup createUpdatedEntity(EntityManager em) {
        Startup startup = new Startup().nome(UPDATED_NOME).segmento(UPDATED_SEGMENTO).descricao(UPDATED_DESCRICAO);
        return startup;
    }

    @BeforeEach
    public void initTest() {
        startup = createEntity(em);
    }

    @Test
    @Transactional
    void createStartup() throws Exception {
        int databaseSizeBeforeCreate = startupRepository.findAll().size();
        // Create the Startup
        StartupDTO startupDTO = startupMapper.toDto(startup);
        restStartupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(startupDTO)))
            .andExpect(status().isCreated());

        // Validate the Startup in the database
        List<Startup> startupList = startupRepository.findAll();
        assertThat(startupList).hasSize(databaseSizeBeforeCreate + 1);
        Startup testStartup = startupList.get(startupList.size() - 1);
        assertThat(testStartup.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testStartup.getSegmento()).isEqualTo(DEFAULT_SEGMENTO);
        assertThat(testStartup.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void createStartupWithExistingId() throws Exception {
        // Create the Startup with an existing ID
        startup.setId(1L);
        StartupDTO startupDTO = startupMapper.toDto(startup);

        int databaseSizeBeforeCreate = startupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStartupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(startupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Startup in the database
        List<Startup> startupList = startupRepository.findAll();
        assertThat(startupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStartups() throws Exception {
        // Initialize the database
        startupRepository.saveAndFlush(startup);

        // Get all the startupList
        restStartupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(startup.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].segmento").value(hasItem(DEFAULT_SEGMENTO)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }

    @Test
    @Transactional
    void getStartup() throws Exception {
        // Initialize the database
        startupRepository.saveAndFlush(startup);

        // Get the startup
        restStartupMockMvc
            .perform(get(ENTITY_API_URL_ID, startup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(startup.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.segmento").value(DEFAULT_SEGMENTO))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    void getNonExistingStartup() throws Exception {
        // Get the startup
        restStartupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStartup() throws Exception {
        // Initialize the database
        startupRepository.saveAndFlush(startup);

        int databaseSizeBeforeUpdate = startupRepository.findAll().size();

        // Update the startup
        Startup updatedStartup = startupRepository.findById(startup.getId()).get();
        // Disconnect from session so that the updates on updatedStartup are not directly saved in db
        em.detach(updatedStartup);
        updatedStartup.nome(UPDATED_NOME).segmento(UPDATED_SEGMENTO).descricao(UPDATED_DESCRICAO);
        StartupDTO startupDTO = startupMapper.toDto(updatedStartup);

        restStartupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, startupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(startupDTO))
            )
            .andExpect(status().isOk());

        // Validate the Startup in the database
        List<Startup> startupList = startupRepository.findAll();
        assertThat(startupList).hasSize(databaseSizeBeforeUpdate);
        Startup testStartup = startupList.get(startupList.size() - 1);
        assertThat(testStartup.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testStartup.getSegmento()).isEqualTo(UPDATED_SEGMENTO);
        assertThat(testStartup.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void putNonExistingStartup() throws Exception {
        int databaseSizeBeforeUpdate = startupRepository.findAll().size();
        startup.setId(count.incrementAndGet());

        // Create the Startup
        StartupDTO startupDTO = startupMapper.toDto(startup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStartupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, startupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(startupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Startup in the database
        List<Startup> startupList = startupRepository.findAll();
        assertThat(startupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStartup() throws Exception {
        int databaseSizeBeforeUpdate = startupRepository.findAll().size();
        startup.setId(count.incrementAndGet());

        // Create the Startup
        StartupDTO startupDTO = startupMapper.toDto(startup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStartupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(startupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Startup in the database
        List<Startup> startupList = startupRepository.findAll();
        assertThat(startupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStartup() throws Exception {
        int databaseSizeBeforeUpdate = startupRepository.findAll().size();
        startup.setId(count.incrementAndGet());

        // Create the Startup
        StartupDTO startupDTO = startupMapper.toDto(startup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStartupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(startupDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Startup in the database
        List<Startup> startupList = startupRepository.findAll();
        assertThat(startupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStartupWithPatch() throws Exception {
        // Initialize the database
        startupRepository.saveAndFlush(startup);

        int databaseSizeBeforeUpdate = startupRepository.findAll().size();

        // Update the startup using partial update
        Startup partialUpdatedStartup = new Startup();
        partialUpdatedStartup.setId(startup.getId());

        partialUpdatedStartup.nome(UPDATED_NOME).segmento(UPDATED_SEGMENTO).descricao(UPDATED_DESCRICAO);

        restStartupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStartup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStartup))
            )
            .andExpect(status().isOk());

        // Validate the Startup in the database
        List<Startup> startupList = startupRepository.findAll();
        assertThat(startupList).hasSize(databaseSizeBeforeUpdate);
        Startup testStartup = startupList.get(startupList.size() - 1);
        assertThat(testStartup.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testStartup.getSegmento()).isEqualTo(UPDATED_SEGMENTO);
        assertThat(testStartup.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void fullUpdateStartupWithPatch() throws Exception {
        // Initialize the database
        startupRepository.saveAndFlush(startup);

        int databaseSizeBeforeUpdate = startupRepository.findAll().size();

        // Update the startup using partial update
        Startup partialUpdatedStartup = new Startup();
        partialUpdatedStartup.setId(startup.getId());

        partialUpdatedStartup.nome(UPDATED_NOME).segmento(UPDATED_SEGMENTO).descricao(UPDATED_DESCRICAO);

        restStartupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStartup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStartup))
            )
            .andExpect(status().isOk());

        // Validate the Startup in the database
        List<Startup> startupList = startupRepository.findAll();
        assertThat(startupList).hasSize(databaseSizeBeforeUpdate);
        Startup testStartup = startupList.get(startupList.size() - 1);
        assertThat(testStartup.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testStartup.getSegmento()).isEqualTo(UPDATED_SEGMENTO);
        assertThat(testStartup.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void patchNonExistingStartup() throws Exception {
        int databaseSizeBeforeUpdate = startupRepository.findAll().size();
        startup.setId(count.incrementAndGet());

        // Create the Startup
        StartupDTO startupDTO = startupMapper.toDto(startup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStartupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, startupDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(startupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Startup in the database
        List<Startup> startupList = startupRepository.findAll();
        assertThat(startupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStartup() throws Exception {
        int databaseSizeBeforeUpdate = startupRepository.findAll().size();
        startup.setId(count.incrementAndGet());

        // Create the Startup
        StartupDTO startupDTO = startupMapper.toDto(startup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStartupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(startupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Startup in the database
        List<Startup> startupList = startupRepository.findAll();
        assertThat(startupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStartup() throws Exception {
        int databaseSizeBeforeUpdate = startupRepository.findAll().size();
        startup.setId(count.incrementAndGet());

        // Create the Startup
        StartupDTO startupDTO = startupMapper.toDto(startup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStartupMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(startupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Startup in the database
        List<Startup> startupList = startupRepository.findAll();
        assertThat(startupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStartup() throws Exception {
        // Initialize the database
        startupRepository.saveAndFlush(startup);

        int databaseSizeBeforeDelete = startupRepository.findAll().size();

        // Delete the startup
        restStartupMockMvc
            .perform(delete(ENTITY_API_URL_ID, startup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Startup> startupList = startupRepository.findAll();
        assertThat(startupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
