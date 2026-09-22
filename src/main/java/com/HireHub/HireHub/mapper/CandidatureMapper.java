package com.HireHub.HireHub.mapper;

import com.HireHub.HireHub.dto.CandidatureRequest;
import com.HireHub.HireHub.dto.CandidatureResponse;
import com.HireHub.HireHub.entity.Candidature;
import com.HireHub.HireHub.entity.OffreEmploi;
import com.HireHub.HireHub.entity.User;

public final class CandidatureMapper {

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
        return response;
    }

    public static Candidature toCandidature(CandidatureRequest request, User candidat, OffreEmploi offre) {
        Candidature candidature = new Candidature();
        candidature.setCandidat(candidat);
        candidature.setOffre(offre);
        return candidature;
    }
}
