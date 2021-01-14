package br.balladesh.pjcappbackend.repository;

import br.balladesh.pjcappbackend.entity.AlbumEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface AlbumRepository extends PagingAndSortingRepository<AlbumEntity, Long> {
  @Query("select a from albums a join a.artist a2 " +
      "where (:id is null or a.id in :id) " +
      "and (:name is null or a.name in :name) " +
      "and (:artistName is null or a2.name in :artistName)")
  Page<AlbumEntity> findByIdAndNameAndArtist_Name(Long id, String name, String artistName, Pageable pageable);

  Optional<AlbumEntity> findByName(String name);
}
