package com.alisoft.hatim.service.impl;

import com.alisoft.hatim.config.security.jwt.JwtUser;
import com.alisoft.hatim.domain.Hatim;
import com.alisoft.hatim.domain.reference.HatimStatus;
import com.alisoft.hatim.domain.reference.HatimType;
import com.alisoft.hatim.dto.response.HatimResponseDto;
import com.alisoft.hatim.dto.response.JuzResponseDto;
import com.alisoft.hatim.exception.NotFoundException;
import com.alisoft.hatim.mapper.HatimMapper;
import com.alisoft.hatim.mapper.JuzMapper;
import com.alisoft.hatim.repository.HatimRepository;
import com.alisoft.hatim.service.HatimService;
import com.alisoft.hatim.service.JuzService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Data
@Service
@Slf4j
public class HatimServiceImpl implements HatimService {
    private final HatimRepository hatimRepository;
    private final JuzService juzService;
    private final HatimMapper hatimMapper;
    private final JuzMapper juzMapper;

    @Override
    @Transactional(readOnly = true)
    public Hatim get(UUID id) throws NotFoundException {
        return hatimRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Hatim.class, id, "id"));
    }

    @Override
    @Transactional
    public HatimResponseDto joinToHatim(JwtUser jwtUser) throws NotFoundException {
        Hatim hatim = hatimRepository.findFirstByStatusOrderByCreatedAtAsc(HatimStatus.IN_PROGRESS).orElse(null);;
        if (hatim == null || !juzService.isJuzExistsToDoPage(hatim)) {
            hatim = new Hatim();
            hatim.setStatus(HatimStatus.IN_PROGRESS);
            hatim.setType(HatimType.GENERAL);
            hatim = hatimRepository.save(hatim);
            juzService.createJuzsForHatim(hatim, jwtUser);
        }
        return hatimMapper.hatimToResponseDto(hatim);
    }

    @Override
    @Transactional
    public void setHatimStatusDone(Hatim hatim) {
        hatim.setStatus(HatimStatus.DONE);
        hatimRepository.save(hatim);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Hatim> findAll() {
        return hatimRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Hatim> findAllByStatus(HatimStatus status) {
        return hatimRepository.findAllByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JuzResponseDto> getJuzByHatim(UUID id) {
        return juzMapper.juzsToResponseDtos(juzService.getByHatim(hatimRepository.getReferenceById(id)));
    }
}
