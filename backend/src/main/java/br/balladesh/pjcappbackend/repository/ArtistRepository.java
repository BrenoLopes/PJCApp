package br.balladesh.pjcappbackend.repository;

import br.balladesh.pjcappbackend.entity.ArtistEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ArtistRepository extends PagingAndSortingRepository<ArtistEntity, Long> {
  Optional<ArtistEntity> findByName(String name);
  Optional<ArtistEntity> findById(long id);

  @Query("select ats from artists ats " +
      "where (:id is null or ats.id in :id) " +
      "and (:name is null or ats.name in :name)")
  Page<ArtistEntity> findByIdAndName(Long id, String name, Pageable pageable);
}
