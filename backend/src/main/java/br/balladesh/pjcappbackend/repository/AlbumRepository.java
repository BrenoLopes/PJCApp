package br.balladesh.pjcappbackend.repository;

import br.balladesh.pjcappbackend.entity.AlbumEntity;
import br.balladesh.pjcappbackend.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface AlbumRepository extends PagingAndSortingRepository<AlbumEntity, Long> {
  Page<AlbumEntity> findAllByArtistOwner(UserEntity owner, Pageable pageable);
  Page<AlbumEntity> findAllByArtistIdAndArtistOwner(long id, UserEntity owner, Pageable pageable);

  Optional<AlbumEntity> findByIdAndArtistOwner(long id, UserEntity owner);

  @Query("select a from albums a join a.artist a2 " +
      "where (:id is null or a.id in :id) " +
      "and (:name is null or a.name in :name) " +
      "and (:artistName is null or a2.name in :artistName)" +
      "and a2.owner in :owner")
  Page<AlbumEntity> findByIdAndNameAndArtist_NameAndArtistOwner(Long id, String name, String artistName, UserEntity owner, Pageable pageable);

  Optional<AlbumEntity> findByNameAndArtistOwner(String name, UserEntity owner);
}
