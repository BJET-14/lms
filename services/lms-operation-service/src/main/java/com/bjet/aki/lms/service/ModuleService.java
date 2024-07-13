package com.bjet.aki.lms.service;

import com.bjet.aki.lms.asset.ListResultBuilder;
import com.bjet.aki.lms.asset.PagedResult;
import com.bjet.aki.lms.asset.PagedResultBuilder;
import com.bjet.aki.lms.asset.ResultBuilder;
import com.bjet.aki.lms.domain.Module;
import com.bjet.aki.lms.jpa.ModuleEntity;
import com.bjet.aki.lms.mapper.ModuleMapper;
import com.bjet.aki.lms.repository.ModuleRepository;
import com.bjet.aki.lms.specification.ModuleSpecification;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuleService {

    Logger logger = LoggerFactory.getLogger(ModuleService.class);

    private final ModuleRepository moduleRepository;
    private final ModuleMapper moduleMapper;

    public void saveModule(Module module) {
        logger.info("Saving request for module");
        ModuleEntity moduleEntity = moduleMapper.toEntity().map(module);
        moduleRepository.save(moduleEntity);
    }

    public List<Module> findAllModule(String title) {
        List<ModuleEntity> modules = moduleRepository.findAll(ModuleSpecification.findModules(title));
        return ListResultBuilder.build(modules, moduleMapper.toDomain());
    }

    public PagedResult<Module> findAllModule(Pageable pageable, String title) {
        Page<ModuleEntity> modules = moduleRepository.findAll(ModuleSpecification.findModules(title), pageable);
        return PagedResultBuilder.build(modules, moduleMapper.toDomain());
    }

    public Module findModuleById(Long id) {
        ModuleEntity moduleEntity = moduleRepository.findById(id).orElseThrow(() -> new NotFoundException("Could not find module with id: " + id));
        return ResultBuilder.build(moduleEntity, moduleMapper.toDomain());
    }
}
