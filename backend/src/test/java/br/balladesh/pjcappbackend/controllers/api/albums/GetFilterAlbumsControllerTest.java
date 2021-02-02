//package br.balladesh.pjcappbackend.controllers.api.albums;
//
//import br.balladesh.pjcappbackend.config.minio.MinIOEndpoint;
//import br.balladesh.pjcappbackend.dto.api.albums.PagedAlbumResponseBody;
//import br.balladesh.pjcappbackend.entity.AlbumEntity;
//import br.balladesh.pjcappbackend.entity.ArtistEntity;
//import br.balladesh.pjcappbackend.entity.UserEntity;
//import br.balladesh.pjcappbackend.repository.AlbumRepository;
//import org.assertj.core.util.Lists;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.*;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@ExtendWith(MockitoExtension.class)
//class GetFilterAlbumsControllerTest {
//  @Mock
//  private AlbumRepository albumRepository;
//
//  @Autowired
//  private MinIOEndpoint endpoint;
//
//  @Test
//  void testWithNullRepository() {
//    GetFilterAlbumsController testTarget = new GetFilterAlbumsController(null, endpoint);
//    ResponseEntity<?> result = testTarget.filterAlbumBy(1L, "ohno", null, 0, 10, "asc");
//
//    assertSame(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
//  }
//
//  @Test
//  void testWithValidRepository() {
//    UserEntity robotUser = new UserEntity("robot", "robot@robot.com", "123456");
//    AlbumEntity album = new AlbumEntity(
//        1L,
//        "amongus",
//        new ArtistEntity("ohno", Lists.newArrayList(), robotUser),
//        ""
//    );
//    Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
//
//    GetFilterAlbumsController testTarget = new GetFilterAlbumsController(
//        this.albumRepository,
//        this.endpoint
//    );
//
//    Page<AlbumEntity> expectedPages = new PageImpl<>(Lists.newArrayList(album));
//    Mockito
//        .when(this.albumRepository.findByIdAndNameAndArtist_Name(1L, null, null, pageable))
//        .thenReturn(expectedPages);
//
//    ResponseEntity<?> response = testTarget.filterAlbumBy(1L, null, null, 0, 10, "asc");
//
//    if (!(response.getBody() instanceof PagedAlbumResponseBody))
//      fail();
//
//    PagedAlbumResponseBody expected = new PagedAlbumResponseBody(expectedPages);
//    PagedAlbumResponseBody received = (PagedAlbumResponseBody) response.getBody();
//
//    assertSame(HttpStatus.OK, response.getStatusCode());
//    assertEquals(expected, received);
//  }
//}