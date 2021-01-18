package br.balladesh.pjcappbackend.dto.api.albums;

import br.balladesh.pjcappbackend.entity.AlbumEntity;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import org.springframework.data.domain.Page;

import java.util.List;

public class PagedAlbumResponseBody {
  public final ImmutableList<AlbumEntity> albums;
  public final int currentPage, totalPages;
  public final long totalItems;

  public PagedAlbumResponseBody(List<AlbumEntity> albums, int currentPage, long totalItems, int totalPages) {
    this.albums = ImmutableList.copyOf(albums == null ? ImmutableList.of() : albums);
    this.currentPage = currentPage;
    this.totalPages = totalPages;
    this.totalItems = totalItems;
  }

  public PagedAlbumResponseBody(Page<AlbumEntity> pagedAlbumEntity) {
    this.albums = ImmutableList.copyOf(pagedAlbumEntity.getContent());
    this.currentPage = pagedAlbumEntity.getNumber();
    this.totalItems = pagedAlbumEntity.getTotalElements();
    this.totalPages = pagedAlbumEntity.getTotalPages();
  }

  public ImmutableList<AlbumEntity> getAlbums() {
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
    PagedAlbumResponseBody that = (PagedAlbumResponseBody) o;
    return currentPage == that.currentPage
        && totalPages == that.totalPages
        && totalItems == that.totalItems
        && albums.equals(that.albums);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.albums, this.currentPage, this.totalItems, this.totalItems);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("albums", this.albums)
        .add("currentPage", this.currentPage)
        .add("totalItems", this.totalItems)
        .add("totalPages", this.totalPages)
        .toString();
  }
}
