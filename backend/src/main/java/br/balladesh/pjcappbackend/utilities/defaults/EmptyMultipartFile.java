package br.balladesh.pjcappbackend.utilities.defaults;

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
  public byte[] getBytes() throws IOException {
    return new byte[0];
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return new ByteArrayInputStream(this.getBytes());
  }

  @Override
  public void transferTo(File file) throws IOException, IllegalStateException {}
}
