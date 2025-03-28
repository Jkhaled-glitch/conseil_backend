package com.api.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.dao.mysql.ConseilDao;
import com.api.dao.mysql.DocumentDao;
import com.api.dao.mysql.PointDao;
import com.api.dao.mysql.PresenceDao;
import com.api.entities.mysql.Conseil;
import com.api.entities.mysql.Document;
import com.api.entities.mysql.Point;
import com.api.entities.mysql.Presence;
import com.api.exception.ResourceNotFoundException;
import com.api.request.DocumentRequest;
import com.api.request.PresenceRequest;
import com.api.service.ConseilService;
import com.api.service.MembreService;

@Service
public class ConseilServiceImpl implements ConseilService {

    @Autowired
    ConseilDao dao;

    @Autowired
    DocumentDao documentDao;

    @Autowired
    MembreService membreService;

    @Autowired
    PresenceDao presenceDao;

    @Autowired
    PointDao pointDao;

    private static final String UPLOAD_DIR = "uploads/";

    @Override
    public Page<Conseil> getAll(Pageable pageable) {

        return dao.findAllByOrderByIdDesc(pageable);
    }

    @Override
    public Conseil getById(Long id) {
        return dao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conseil inexiste "));
    }

    @Override
    public Presence getPresenceById(Long id) {
        return presenceDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Presence inexiste "));
    }

    @Override
    public Point getPointById(Long id) {
        return pointDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Point inexiste "));
    }

    @Override
    public String delete(Long id) {

        dao.delete(getById(id));

        return "Conseil Supprimé avec succées";
    }

    @Override
    public Conseil AddPV(Long id, DocumentRequest request) {

        Conseil conseil = getById(id);
        request.setNom("محضر_" + conseil.getNom());

        String folderPath = UPLOAD_DIR
                + conseil.getNom().replaceAll("\\s+", "_")
                + "/"
                + "محضر_المجلس";

        Document pvDoc = addDocument(request, folderPath);

        // Mettre à jour le Conseil avec le document
        conseil.setPv(pvDoc);

        return dao.save(conseil);

    }

    public Document addDocument(DocumentRequest request, String folderPath) {

        MultipartFile file = request.getDocument();

        if (file.isEmpty()) {
            throw new RuntimeException("Le fichier est vide !");
        }

        try {
            // 1️⃣ Créer le dossier si inexistant

            File folder = new File(folderPath);

            if (!folder.exists()) {
                folder.mkdirs();
            }

            String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

            String fileName = request.getNom() != null
                    ? request.getNom().replaceAll("\\s+", "_")  + extension
                    : file.getOriginalFilename().replaceAll("\\s+", "_");

            // 3️⃣ Définir le chemin complet du fichier avec le nom mis à jour
            String filePath = folderPath + "/" + fileName;

            // 4️⃣ Sauvegarder le fichier
            Path path = Path.of(filePath);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            Document document = new Document();
            document.setName(request.getNom());
            // Défini le path relatif, sans le répertoire d'upload
            String relativePath = filePath.replace(UPLOAD_DIR, "");
            document.setPath(relativePath);

            return documentDao.save(document);

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du fichier : " + e.getMessage());
        }
    }

    @Override
    public Conseil AssignDocument(Long conseilId, DocumentRequest request) {

        Conseil conseil = getById(conseilId);

        request.setNom(request.getNom() + "_" + conseil.getNom());
        String conseilFolder = UPLOAD_DIR
                + conseil.getNom().replaceAll("\\s+", "_")
                + "/"
                + "ملف_المجلس";

        Document doc = addDocument(request, conseilFolder);

        // Mettre à jour le Conseil avec le document
        conseil.getDocuments().add(doc);

        return dao.save(conseil);

    }

    @Override
    public Presence AddDelegation(Long id, Long presenceId, MultipartFile file) {

        Conseil conseil = getById(id);

        Presence presence = getPresenceById(presenceId);

        DocumentRequest documentRequest = new DocumentRequest();
        documentRequest.setDocument(file);
        documentRequest.setNom(
                "تفويض_"
                + presence.getDelegueA().getId() + "_" + presence.getMembre().getId()
                + "__" +
        conseil.getNom().replaceAll("\\s+", "_") 
        ) ;

        String conseilFolder = UPLOAD_DIR
                + conseil.getNom().replaceAll("\\s+", "_")
                + "/"
                + "ملف_المجلس";

        Document doc = addDocument(documentRequest, conseilFolder);

        presence.setDocument(doc);

        presence = presenceDao.save(presence);

        conseil.getPresences().add(presence);

        dao.save(conseil);

        return presence;

    }

    @Override
    public Conseil addConseil(Conseil conseil) {
        return dao.save(conseil);
    }

    @Override
    public Presence addPresence(Long id, PresenceRequest request) {
        Conseil conseil = getById(id);

        Presence presence = Presence.builder()
                .membre(membreService.getById(request.getMembre()))
                .statut(request.getStatut())
                .delegueA(request.getDelegueA() != null ? membreService.getById(request.getDelegueA()) : null)
                .build();

        presence = presenceDao.save(presence);
        conseil.getPresences().add(presence);

        dao.save(conseil) ;
        return presence;
    }

    @Override
    public Point addPoint(Long id, String nom) {
        Conseil conseil = getById(id);
        Point point = Point.builder()
                .nom(nom)
                .build();
        point = pointDao.save(point);

        conseil.getPoints().add(point);
        dao.save(conseil);
        return point;

    }

    @Override
    public Point addDocumentToPoint(Long id, Long pointId, MultipartFile file, String nom) {

        Conseil conseil = getById(id);

        Point point = getPointById(pointId);

        DocumentRequest documentRequest = new DocumentRequest(nom, file);

        String conseilFolder = UPLOAD_DIR
                + conseil.getNom().replaceAll("\\s+", "_")
                + "/"
                + "ملف_المجلس";

        Document doc = addDocument(documentRequest, conseilFolder);

        point.getDocuments().add(doc);

        pointDao.save(point);

        return point;
    }

}
