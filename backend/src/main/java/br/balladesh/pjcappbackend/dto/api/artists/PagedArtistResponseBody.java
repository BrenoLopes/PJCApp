package br.balladesh.pjcappbackend.dto.api.artists;

import br.balladesh.pjcappbackend.entity.ArtistEntity;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;

public class PagedArtistResponseBody {
  public final ImmutableList<ArtistEntity> artists;
  public final int currentPage, totalPages;
  public final long totalItems;

  public PagedArtistResponseBody(List<ArtistEntity> artists, int currentPage, long totalItems, int totalPages) {
    this.artists = artists == null ? ImmutableList.of() : ImmutableList.copyOf(artists);
    this.currentPage = currentPage;
    this.totalPages = totalPages;
    this.totalItems = totalItems;
  }

  public PagedArtistResponseBody(Page<ArtistEntity> pagedArtistsEntity) {
    if (pagedArtistsEntity == null) {
      this.artists = ImmutableList.of();
      this.currentPage = 0;
      this.totalPages = 1;
      this.totalItems = 0;

      return;
    }

    this.artists = ImmutableList.copyOf(pagedArtistsEntity.getContent());
    this.currentPage = pagedArtistsEntity.getNumber();
    this.totalItems = pagedArtistsEntity.getTotalElements();
    this.totalPages = pagedArtistsEntity.getTotalPages();
  }

  public ImmutableList<ArtistEntity> getArtists() {
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
        && artists.equals(that.artists);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.currentPage, this.totalItems, this.totalPages, this.artists);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("artists", this.artists)
        .add("currentPage", this.currentPage)
        .add("totalItems", this.totalItems)
        .add("totalPages", this.totalPages)
        .toString();
  }
}
