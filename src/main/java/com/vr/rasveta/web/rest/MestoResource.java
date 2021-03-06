package com.vr.rasveta.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vr.rasveta.domain.Mesto;

import com.vr.rasveta.repository.MestoRepository;
import com.vr.rasveta.web.rest.errors.BadRequestAlertException;
import com.vr.rasveta.web.rest.util.HeaderUtil;
import com.vr.rasveta.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Mesto.
 */
@RestController
@RequestMapping("/api")
public class MestoResource {

    private final Logger log = LoggerFactory.getLogger(MestoResource.class);

    private static final String ENTITY_NAME = "mesto";

    private final MestoRepository mestoRepository;

    public MestoResource(MestoRepository mestoRepository) {
        this.mestoRepository = mestoRepository;
    }

    /**
     * POST  /mestos : Create a new mesto.
     *
     * @param mesto the mesto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mesto, or with status 400 (Bad Request) if the mesto has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mestos")
    @Timed
    public ResponseEntity<Mesto> createMesto(@RequestBody Mesto mesto) throws URISyntaxException {
        log.debug("REST request to save Mesto : {}", mesto);
        if (mesto.getId() != null) {
            throw new BadRequestAlertException("A new mesto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Mesto result = mestoRepository.save(mesto);
        return ResponseEntity.created(new URI("/api/mestos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mestos : Updates an existing mesto.
     *
     * @param mesto the mesto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mesto,
     * or with status 400 (Bad Request) if the mesto is not valid,
     * or with status 500 (Internal Server Error) if the mesto couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mestos")
    @Timed
    public ResponseEntity<Mesto> updateMesto(@RequestBody Mesto mesto) throws URISyntaxException {
        log.debug("REST request to update Mesto : {}", mesto);
        if (mesto.getId() == null) {
            return createMesto(mesto);
        }
        Mesto result = mestoRepository.save(mesto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mesto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mestos : get all the mestos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mestos in body
     */
    @GetMapping("/mestos")
    @Timed
    public ResponseEntity<List<Mesto>> getAllMestos(Pageable pageable) {
        log.debug("REST request to get a page of Mestos");
        Page<Mesto> page = mestoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mestos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mestos/:id : get the "id" mesto.
     *
     * @param id the id of the mesto to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mesto, or with status 404 (Not Found)
     */
    @GetMapping("/mestos/{id}")
    @Timed
    public ResponseEntity<Mesto> getMesto(@PathVariable Long id) {
        log.debug("REST request to get Mesto : {}", id);
        Mesto mesto = mestoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(mesto));
    }

    /**
     * DELETE  /mestos/:id : delete the "id" mesto.
     *
     * @param id the id of the mesto to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mestos/{id}")
    @Timed
    public ResponseEntity<Void> deleteMesto(@PathVariable Long id) {
        log.debug("REST request to delete Mesto : {}", id);
        mestoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
