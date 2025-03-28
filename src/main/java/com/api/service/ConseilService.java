package com.api.service;

import com.api.entities.mysql.Conseil;
import com.api.entities.mysql.Point;
import com.api.entities.mysql.Presence;
import com.api.request.DocumentRequest;
import com.api.request.PresenceRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ConseilService {

    Conseil addConseil(Conseil conseil);

    Presence addPresence(Long id, PresenceRequest presence);


    Point addPoint(Long id, String name);

    Point addDocumentToPoint(Long id ,Long pointId , MultipartFile file, String nom );

    Page<Conseil> getAll(Pageable pageable);

    Conseil getById(Long id);

    Presence getPresenceById(Long id);

    Point getPointById(Long id);

    String delete(Long id);


    Conseil AddPV(Long conseilId, DocumentRequest request);

    Conseil AssignDocument(Long conseilId, DocumentRequest request);

    Presence AddDelegation(Long id, Long presenceId, MultipartFile file);

   

}
