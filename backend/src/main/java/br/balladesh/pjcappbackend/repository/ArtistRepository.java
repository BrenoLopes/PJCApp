package br.balladesh.pjcappbackend.repository;

import br.balladesh.pjcappbackend.entity.ArtistEntity;
import br.balladesh.pjcappbackend.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ArtistRepository extends PagingAndSortingRepository<ArtistEntity, Long> {
  Page<ArtistEntity> findAllByOwner(UserEntity owner, Pageable pageable);

  Optional<ArtistEntity> findByNameAndOwner(String name, UserEntity owner);
  Optional<ArtistEntity> findByIdAndOwner(long id, UserEntity owner);

  @Query("select ats from artists ats " +
      "where (:id is null or ats.id in :id) " +
      "and (:name is null or ats.name in :name)" +
      "and ats.owner in :owner")
  Optional<ArtistEntity> findByIdAndNameAndOwner(Long id, String name, UserEntity owner);

  void deleteByIdAndOwner(long id, UserEntity owner);
}
