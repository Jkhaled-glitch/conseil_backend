package com.api.service;

import com.api.entities.mysql.Membre;
import java.util.List;

public interface MembreService {

    List<Membre> getAll();

    List<Membre> getActif();

    Membre getById(Long id);

    String delete(Long id);

    Membre switchActif(Long id);

}
