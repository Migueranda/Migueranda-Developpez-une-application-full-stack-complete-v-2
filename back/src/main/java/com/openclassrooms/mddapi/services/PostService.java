package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.mapper.PostMapper;
import com.openclassrooms.mddapi.model.dtos.PostDto;

import com.openclassrooms.mddapi.model.entities.PostEntity;
import com.openclassrooms.mddapi.model.entities.Subject;
import com.openclassrooms.mddapi.model.entities.UserEntity;
import com.openclassrooms.mddapi.repositories.PostRepository;
import com.openclassrooms.mddapi.repositories.SubjectRepository;
import com.openclassrooms.mddapi.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * Service fournissant des opérations de gestion des posts.
 * Gère la récupération, la création et la récupération par identifiant des posts.
 */

@Data
@Service
public class PostService {

    @Autowired
    private final PostRepository postRepository;

    @Autowired
    private final SubjectRepository subjectRepository;

    @Autowired
    private PostMapper postMapper;


    @Autowired
    private UserRepository userRepository;

    /**
     * Récupère tous les posts de la base de données, triés par un champ spécifique.
     *
     * @param postDto l'objet de transfert de données contenant les critères de récupération des posts.
     * @param sortBy sortBy le champ par lequel trier les résultats.
     * @param order order l'ordre de tri (ascendant ou descendant).
     * @return une liste de PostDto représentant tous les posts triés.
     * @throws IllegalArgumentException si la valeur de l'ordre est invalide.
     */

    public List<PostDto> getAllPost(PostDto postDto, String sortBy, String order) {
        Sort.Direction direction;
        try {
            direction = Sort.Direction.valueOf(order.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid order value: " + order);
        }

        Sort sort = Sort.by(direction, sortBy);
        Iterable<PostEntity> posts = postRepository.findAll(sort);

        List<PostEntity> postEntities = StreamSupport.stream(posts.spliterator(), false)
                .collect(Collectors.toList());

        return postEntities.stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());
    }
    /**
     * Crée un nouveau post dans la base de données.
     *
     * @param postDto l'objet de transfert de données contenant les informations du post à créer
     * @return l'objet de transfert de données représentant le post créé
     * RuntimeException si le thème ou l'utilisateur associé au post n'est pas trouvé
     */
    public PostDto createPost(PostDto postDto) {
        Optional<Subject> themeOptional = subjectRepository.findById(postDto.getThemeId());
        if (!themeOptional.isPresent()) {
            throw new RuntimeException("Theme not found");
        }

        Optional<UserEntity> userOptional = userRepository.findById(postDto.getUserId());
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found");
        }

        PostEntity postEntity = postMapper.toEntity(postDto);
        postEntity.setTheme(themeOptional.get());
        postEntity.setUser(userOptional.get());
        postEntity.setDate(new Timestamp(System.currentTimeMillis()));

        PostEntity savedPostEntity = postRepository.save(postEntity);
        return postMapper.toDto(savedPostEntity);
    }

    /**
     * Récupère un post par son identifiant.
     *
     * @param id l'identifiant du post à récupérer.
     * @return l'objet de transfert de données représentant le post récupéré.
     * @throws EntityNotFoundException si le post n'est pas trouvé
     */
    public PostDto getPostById(Long id) {
        Optional<PostEntity> postEntityOptional = postRepository.findById(id);
        if (!postEntityOptional.isPresent()) {
            throw new EntityNotFoundException("Post not found with id " + id);
        }

        PostEntity postEntity = postEntityOptional.get();
        return postMapper.toDto(postEntity);
    }


}
