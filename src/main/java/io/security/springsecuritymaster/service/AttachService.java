package io.security.springsecuritymaster.service;

import io.security.springsecuritymaster.domain.attach.Attach;
import io.security.springsecuritymaster.repository.AttachRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AttachService {

    private final AttachRepository attachRepository;

    @Transactional
    public void deleteAttach(String fileName, Long clothesId) {
        Attach attach = attachRepository.findByClothesIdAndFileName(clothesId, fileName);
        attachRepository.delete(attach);
    }
}
