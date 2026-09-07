package com.HireHub.HireHub.service;

import com.HireHub.HireHub.entity.Cv;
import com.HireHub.HireHub.repository.CvRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class CvService {

    private final CvRepository cvRepository;
     public CvService(CvRepository cvRepository) {
         this.cvRepository = cvRepository;
         }

    public Cv consulterCVparId(long cvId){
        return cvRepository.findById(cvId).orElse(null);
    }

    public Cv consulterCVparNom(String cvNom){
        return cvRepository.findByNomFichier(cvNom);
    }

    public List<Cv> listerAllCv(){
         return cvRepository.findAll();
    }

    public String uploadCv(Cv cv){
         cvRepository.save(cv);
         return "CV uploaded successfully";
    }

    public  String deleteCv(long cvId){
         cvRepository.deleteById(cvId);
         return "CV deleted successfully";
    }

    public String updateCv(Cv cv){
         cvRepository.save(cv);
         return "CV updated successfully";
    }

}
