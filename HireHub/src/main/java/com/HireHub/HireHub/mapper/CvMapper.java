package com.HireHub.HireHub.mapper;

import com.HireHub.HireHub.dto.CvRequest;
import com.HireHub.HireHub.dto.CvResponse;
import com.HireHub.HireHub.entity.Cv;
import com.HireHub.HireHub.entity.User;

public final class CvMapper {

    private CvMapper() {
    }

    public static CvResponse toCvResponse(Cv cv) {
        if (cv == null) {
            return null;
        }
        CvResponse response = new CvResponse();
        response.setId(cv.getId());
        response.setNomFichier(cv.getNomFichier());
        response.setCheminFichier(cv.getCheminFichier());
        response.setDateUpload(cv.getDateUpload());
        response.setCandidat(UserMapper.toUserResponse(cv.getCandidat()));
        return response;
    }

    public static Cv toCv(CvRequest request, User candidat) {
        Cv cv = new Cv();
        cv.setCandidat(candidat);
        cv.setNomFichier(request.getNomFichier());
        cv.setCheminFichier(request.getCheminFichier());
        return cv;
    }
}
