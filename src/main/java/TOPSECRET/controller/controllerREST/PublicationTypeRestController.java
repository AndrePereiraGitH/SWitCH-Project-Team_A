package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.PublicationTypeLinkProvider;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.PublicationType.PublicationTypeMapper;
import TOPSECRET.dto.PublicationType.request.CreatePublicationTypeDTO;
import TOPSECRET.dto.PublicationType.response.PublicationTypeResponseDTO;
import TOPSECRET.services.PublicationTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@Tag(name = "PublicationTypes", description = "Publication Types management endpoints")
@RequestMapping(path = "/publication-types")
public class PublicationTypeRestController {

    private final PublicationTypeService service;
    private final PublicationTypeLinkProvider publicationTypeLinkProvider;

    @Autowired
    public PublicationTypeRestController(
            PublicationTypeService publicationTypeService,
            PublicationTypeLinkProvider publicationTypeLinkProvider
    ) {
        this.service = publicationTypeService;
        this.publicationTypeLinkProvider = publicationTypeLinkProvider;
    }

    @PostMapping("/")
    public ResponseEntity<PublicationTypeResponseDTO> createPublicationType(
            @RequestBody CreatePublicationTypeDTO createPublicationTypeDTO,
            @RequestParam UserId userId){

        try {
            PublicationType publicationType = service.createAndSave(createPublicationTypeDTO.mediaFormat(), userId);

            PublicationTypeResponseDTO result = PublicationTypeMapper.toDTO(publicationType);

            List<Link> links = publicationTypeLinkProvider.getLinksForPublicationType(userId);
            links.forEach(result::add);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch(InstanceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<PublicationTypeResponseDTO>> getAllPublicationTypes(){

        try {
            List<PublicationType> publicationTypes = service.findAll();

            List<PublicationTypeResponseDTO> dtoList = PublicationTypeMapper.toDTOList(publicationTypes);

            return ResponseEntity.status(HttpStatus.OK).body(dtoList);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
