package com.bjet.aki.lms.controller;

import com.bjet.aki.lms.asset.PagedResult;
import com.bjet.aki.lms.domain.Module;
import com.bjet.aki.lms.service.ModuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operations/modules")
@Tag(name = "Module Controller",
        description = "Module related api requests.")
@AllArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;

    @GetMapping
    public ResponseEntity<?> getModules(
            @RequestParam(name = "asPage", required = false, defaultValue = "false") Boolean asPage,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(name = "title", required = false) String title) {
        if (asPage) {
            Pageable pageable = PageRequest.of(page, size);
            PagedResult<Module> modulesAsPage = moduleService.findAllModule(pageable, title);
            return ResponseEntity.ok(modulesAsPage);
        }
        List<Module> modulesAsList = moduleService.findAllModule(title);
        return ResponseEntity.ok(modulesAsList);
    }

    @PostMapping
    public ResponseEntity<Void> saveModule(@RequestBody Module module) {
        moduleService.saveModule(module);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Module> findCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(moduleService.findModuleById(id));
    }
}
