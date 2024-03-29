package io.github.edsonisaac.jogomemoriabackend.controllers;

import io.github.edsonisaac.jogomemoriabackend.dtos.UserDTO;
import io.github.edsonisaac.jogomemoriabackend.exceptions.ValidationException;
import io.github.edsonisaac.jogomemoriabackend.models.Image;
import io.github.edsonisaac.jogomemoriabackend.models.User;
import io.github.edsonisaac.jogomemoriabackend.services.UserService;
import io.github.edsonisaac.jogomemoriabackend.utils.FileUtils;
import io.github.edsonisaac.jogomemoriabackend.utils.MessageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoints for users management")
public class UserController implements AbstractController<User, UserDTO> {

    private final UserService service;

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(OK)
    @PreAuthorize("hasAuthority('SCOPE_SUPPORT')")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @Override
    @GetMapping
    @ResponseStatus(OK)
    @PreAuthorize("hasAuthority('SCOPE_SUPPORT')")
    public Page<UserDTO> findAll(@RequestParam(required = false, defaultValue = "0") Integer page,
                                 @RequestParam(required = false, defaultValue = "10") Integer size,
                                 @RequestParam(required = false, defaultValue = "name") String sort,
                                 @RequestParam(required = false, defaultValue = "asc") String direction) {

        return service.findAll(page, size, sort, direction);
    }

    @Override
    @GetMapping("/{id}")
    @ResponseStatus(OK)
    @PreAuthorize("hasAuthority('SCOPE_SUPPORT')")
    public UserDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Override
    public UserDTO save(User user) {
        return null;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAuthority('SCOPE_SUPPORT')")
    public UserDTO save(@RequestPart User user,
                        @RequestPart(required = false) MultipartFile photo) {
        handlePhoto(user, photo);
        return service.save(user);
    }

    @GetMapping("/search")
    @ResponseStatus(OK)
    @Operation(summary = "Search", description = "Search a resource")
    public Page<UserDTO> search(@RequestParam String value,
                                @RequestParam(required = false, defaultValue = "0") Integer page,
                                @RequestParam(required = false, defaultValue = "10") Integer size,
                                @RequestParam(required = false, defaultValue = "name") String sort,
                                @RequestParam(required = false, defaultValue = "asc") String direction) {

        return service.search(value, page, size, sort, direction);
    }

    @Override
    public UserDTO update(UUID id, User user) {
        return null;
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(OK)
    @PreAuthorize("hasAuthority('SCOPE_SUPPORT')")
    public UserDTO update(@PathVariable UUID id,
                          @RequestPart @Valid User user,
                          @RequestPart(required = false) MultipartFile photo) {

        if (user.getId().equals(id)) {
            handlePhoto(user, photo);
            return service.save(user);
        }

        throw new ValidationException(MessageUtils.ARGUMENT_NOT_VALID);
    }

    private void handlePhoto(User user, MultipartFile photo) {

        if (photo != null) {
            final var image = Image.builder()
                    .name(System.currentTimeMillis() + "." + FileUtils.getExtension(Objects.requireNonNull(photo.getOriginalFilename())))
                    .build();

            user.setPhoto(image);
            FileUtils.FILES.put(image.getName(), photo);
        }
    }
}