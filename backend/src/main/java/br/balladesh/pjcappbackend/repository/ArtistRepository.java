package br.balladesh.pjcappbackend.repository;

import br.balladesh.pjcappbackend.entity.ArtistEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ArtistRepository extends PagingAndSortingRepository<ArtistEntity, Long> {
  Optional<ArtistEntity> findByName(String name);
  Optional<ArtistEntity> findById(long id);
}
