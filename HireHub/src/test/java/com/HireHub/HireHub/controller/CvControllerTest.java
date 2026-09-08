package com.HireHub.HireHub.controller;

import com.HireHub.HireHub.entity.Cv;
import com.HireHub.HireHub.repository.CvRepository;
import com.HireHub.HireHub.repository.UserRepository;
import com.HireHub.HireHub.service.CvService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CvControllerTest {

    @Test
    void shouldCreateCvFromJson() {
        CvController controller = new CvController(new CvService(repositoryProxy(1L), userRepositoryProxy()));

        Cv cv = new Cv();
        cv.setNomFichier("myfirstcv01");
        cv.setCheminFichier("downloads/doc");

        ResponseEntity<Cv> response = controller.save(cv);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("myfirstcv01", response.getBody().getNomFichier());
        assertEquals("downloads/doc", response.getBody().getCheminFichier());
    }

    private CvRepository repositoryProxy(long generatedId) {
        InvocationHandler handler = (Object proxy, Method method, Object[] args) -> {
            if ("save".equals(method.getName())) {
                Cv cv = (Cv) args[0];
                cv.setId(generatedId);
                return cv;
            }
            if ("deleteById".equals(method.getName())) {
                return null;
            }
            if ("toString".equals(method.getName())) {
                return "CvRepositoryProxy";
            }
            throw new UnsupportedOperationException(method.getName());
        };

        return (CvRepository) Proxy.newProxyInstance(
                CvRepository.class.getClassLoader(),
                new Class<?>[]{CvRepository.class},
                handler
        );
    }

    private UserRepository userRepositoryProxy() {
        InvocationHandler handler = (Object proxy, Method method, Object[] args) -> {
            if ("toString".equals(method.getName())) {
                return "UserRepositoryProxy";
            }
            throw new UnsupportedOperationException(method.getName());
        };

        return (UserRepository) Proxy.newProxyInstance(
                UserRepository.class.getClassLoader(),
                new Class<?>[]{UserRepository.class},
                handler
        );
    }
}