package com.HireHub.HireHub.mapper;

import com.HireHub.HireHub.dto.CandidatureRequest;
import com.HireHub.HireHub.dto.CandidatureResponse;
import com.HireHub.HireHub.dto.EntretienResponse;
import com.HireHub.HireHub.entity.Candidature;
import com.HireHub.HireHub.entity.Cv;
import com.HireHub.HireHub.entity.Entretien;
import com.HireHub.HireHub.entity.OffreEmploi;
import com.HireHub.HireHub.entity.User;

import java.util.Comparator;
import java.util.List;

public final class CandidatureMapper {

    private static final Comparator<Entretien> PAR_DATE = Comparator
            .comparing(Entretien::getDate, Comparator.nullsLast(Comparator.naturalOrder()))
            .thenComparing(Entretien::getHeure, Comparator.nullsLast(Comparator.naturalOrder()))
            .thenComparing(Entretien::getId, Comparator.nullsLast(Comparator.naturalOrder()));

    private CandidatureMapper() {
    }

    public static CandidatureResponse toCandidatureResponse(Candidature candidature) {
        if (candidature == null) {
            return null;
        }
        CandidatureResponse response = new CandidatureResponse();
        response.setId(candidature.getId());
        response.setDateCandidature(candidature.getDateCandidature());
        response.setStatut(candidature.getStatut());
        response.setCandidat(UserMapper.toUserResponse(candidature.getCandidat()));
        response.setOffre(OffreMapper.toOffreResponse(candidature.getOffre()));
        response.setCv(CvMapper.toCvResponse(candidature.getCv()));
        response.setEntretiens(toEntretienResponses(candidature.getEntretiens()));
        return response;
    }

    private static List<EntretienResponse> toEntretienResponses(List<Entretien> entretiens) {
        if (entretiens == null || entretiens.isEmpty()) {
            return List.of();
        }
        return entretiens.stream()
                .sorted(PAR_DATE)
                .map(EntretienMapper::toEntretienResponse)
                .toList();
    }

    public static Candidature toCandidature(CandidatureRequest request, User candidat, OffreEmploi offre, Cv cv) {
        Candidature candidature = new Candidature();
        candidature.setCandidat(candidat);
        candidature.setOffre(offre);
        candidature.setCv(cv);
        return candidature;
    }
}
