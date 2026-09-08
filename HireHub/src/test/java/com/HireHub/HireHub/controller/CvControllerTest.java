package com.HireHub.HireHub.controller;

import com.HireHub.HireHub.dto.CvRequest;
import com.HireHub.HireHub.dto.CvResponse;
import com.HireHub.HireHub.entity.Cv;
import com.HireHub.HireHub.entity.User;
import com.HireHub.HireHub.repository.CvRepository;
import com.HireHub.HireHub.repository.UserRepository;
import com.HireHub.HireHub.service.CvService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CvControllerTest {

    @Test
    void shouldCreateCvFromJson() {
        CvController controller = new CvController(new CvService(repositoryProxy(1L), userRepositoryProxy(1L)));

        CvRequest request = new CvRequest(1L, "myfirstcv01", "downloads/doc");

        ResponseEntity<CvResponse> response = controller.save(request);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().id());
        assertEquals("myfirstcv01", response.getBody().nomFichier());
        assertEquals("downloads/doc", response.getBody().cheminFichier());
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

    private UserRepository userRepositoryProxy(long userId) {
        InvocationHandler handler = (Object proxy, Method method, Object[] args) -> {
            if ("findById".equals(method.getName())) {
                User user = new User();
                user.setId(userId);
                return Optional.of(user);
            }
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