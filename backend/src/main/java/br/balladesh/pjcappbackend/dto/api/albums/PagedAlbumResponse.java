package br.balladesh.pjcappbackend.dto.api.albums;

import br.balladesh.pjcappbackend.entity.AlbumEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;

public class PagedAlbumResponse {
  public final List<AlbumEntity> albums;
  public final int currentPage, totalPages;
  public final long totalItems;

  public PagedAlbumResponse(List<AlbumEntity> albums, int currentPage, long totalItems, int totalPages) {
    this.albums = albums;
    this.currentPage = currentPage;
    this.totalPages = totalPages;
    this.totalItems = totalItems;
  }

  public PagedAlbumResponse(Page<AlbumEntity> pagedAlbumEntity) {
    this.albums = pagedAlbumEntity.getContent();
    this.currentPage = pagedAlbumEntity.getNumber();
    this.totalItems = pagedAlbumEntity.getTotalElements();
    this.totalPages = pagedAlbumEntity.getTotalPages();
  }

  public List<AlbumEntity> getAlbums() {
    return albums;
  }

  public int getCurrentPage() {
    return currentPage;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public long getTotalItems() {
    return totalItems;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PagedAlbumResponse that = (PagedAlbumResponse) o;
    return currentPage == that.currentPage && totalPages == that.totalPages && totalItems == that.totalItems && albums.equals(that.albums);
  }

  @Override
  public int hashCode() {
    return Objects.hash(albums, currentPage, totalPages, totalItems);
  }

  @Override
  public String toString() {
    String str
        = "{"
        + "albums=%s,"
        + "currentPage=\"%d\","
        + "totalItems=\"%d\","
        + "totalPages=\"%d\""
        + "}";

    return String.format(
        str,
        this.albums,
        this.currentPage,
        this.totalItems,
        this.totalPages
    );
  }
}
