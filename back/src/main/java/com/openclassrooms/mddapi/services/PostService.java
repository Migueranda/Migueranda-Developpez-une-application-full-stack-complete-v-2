package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.mapper.PostMapper;
import com.openclassrooms.mddapi.model.dtos.PostDto;

import com.openclassrooms.mddapi.model.entities.PostEntity;
import com.openclassrooms.mddapi.model.entities.Subject;
import com.openclassrooms.mddapi.model.entities.Subscription;
import com.openclassrooms.mddapi.model.entities.UserEntity;
import com.openclassrooms.mddapi.repositories.PostRepository;
import com.openclassrooms.mddapi.repositories.SubjectRepository;
import com.openclassrooms.mddapi.repositories.SubscriptionRepository;
import com.openclassrooms.mddapi.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service fournissant des opérations de gestion des posts.
 * Gère la récupération, la création et la récupération par identifiant des posts.
 */

@Service
@RequiredArgsConstructor
public class PostService implements IPostService{

    private final PostRepository postRepository;
    private final SubjectRepository subjectRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PostMapper postMapper;
    private final UserRepository userRepository;


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
        List<PostEntity> posts = postRepository.findAll(sort);

        return posts.stream()
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

        Subject theme = subjectRepository.findById(postDto.getThemeId())
                .orElseThrow(() -> new RuntimeException("Theme not found"));

        UserEntity user = userRepository.findById(postDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        PostEntity postEntity = postMapper.toEntity(postDto);
        postEntity.setTheme(theme);
        postEntity.setUser(user);
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
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id " + id));

        return postMapper.toDto(postEntity);
    }

    /**
     * Récupère tous les posts associés aux sujets auxquels un utilisateur est abonné.
     *
     * @param userId l'identifiant de l'utilisateur.
     * @return une liste de PostEntity représentant les posts des sujets auxquels l'utilisateur est abonné.
     * @throws RuntimeException si l'utilisateur n'est pas trouvé.
     */
    public List<PostEntity> getPostsForUser(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<Subscription> subscriptions = subscriptionRepository.findByUser(user);
        List<Subject> subjects = subscriptions.stream()
                .map(Subscription::getSubject)
                .collect(Collectors.toList());
        return postRepository.findByThemeIn(subjects);
    }
}
