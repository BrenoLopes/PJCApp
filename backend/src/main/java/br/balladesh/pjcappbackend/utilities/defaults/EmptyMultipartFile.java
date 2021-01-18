package br.balladesh.pjcappbackend.utilities.defaults;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public final class EmptyMultipartFile implements MultipartFile {
  @Override
  public String getName() {
    return "";
  }

  @Override
  public String getOriginalFilename() {
    return "";
  }

  @Override
  public String getContentType() {
    return "";
  }

  @Override
  public boolean isEmpty() {
    return true;
  }

  @Override
  public long getSize() {
    return 0;
  }

  @Override
  public byte[] getBytes() {
    return new byte[0];
  }

  @Override
  public InputStream getInputStream() {
    return new ByteArrayInputStream(this.getBytes());
  }

  @Override
  public void transferTo(File file) {}

  @Override
  public boolean equals(Object o) {
    if (o == null) return false;
    if (o.getClass() != this.getClass()) return false;

    final EmptyMultipartFile that = (EmptyMultipartFile) o;
    return Objects.equal(this.getBytes(), that.getBytes())
        && Objects.equal(this.getName(), that.getName())
        && Objects.equal(this.getContentType(), that.getName())
        && Objects.equal(this.isEmpty(), that.isEmpty())
        && Objects.equal(this.getSize(), that.getSize())
        && Objects.equal(this.getOriginalFilename(), this.getOriginalFilename());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(
        this.getBytes(),
        this.getName(),
        this.getContentType(),
        this.getOriginalFilename(),
        this.getSize()
    );
  }
}
