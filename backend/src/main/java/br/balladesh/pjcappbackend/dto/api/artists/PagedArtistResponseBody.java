package br.balladesh.pjcappbackend.dto.api.artists;

import br.balladesh.pjcappbackend.entity.ArtistEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;

public class PagedArtistResponseBody {
  public final List<ArtistEntity> artists;
  public final int currentPage, totalPages;
  public final long totalItems;

  public PagedArtistResponseBody(List<ArtistEntity> artists, int currentPage, long totalItems, int totalPages) {
    this.artists = artists;
    this.currentPage = currentPage;
    this.totalPages = totalPages;
    this.totalItems = totalItems;
  }

  public PagedArtistResponseBody(Page<ArtistEntity> pagedArtistsEntity) {
    this.artists = pagedArtistsEntity.getContent();
    this.currentPage = pagedArtistsEntity.getNumber();
    this.totalItems = pagedArtistsEntity.getTotalElements();
    this.totalPages = pagedArtistsEntity.getTotalPages();
  }

  public List<ArtistEntity> getArtists() {
    return artists;
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
    PagedArtistResponseBody that = (PagedArtistResponseBody) o;
    return currentPage == that.currentPage
        && totalPages == that.totalPages
        && totalItems == that.totalItems
        && Objects.equals(artists, that.artists);
  }

  @Override
  public int hashCode() {
    return Objects.hash(artists, currentPage, totalPages, totalItems);
  }

  @Override
  public String toString() {
    String str
        = "{"
        + "artists=[%s],"
        + "currentPage=\"%d\","
        + "totalItems=\"%d\","
        + "totalPages=\"%d\""
        + "}";

    return String.format(
        str,
        this.artists,
        this.currentPage,
        this.totalItems,
        this.totalPages
    );
  }
}
