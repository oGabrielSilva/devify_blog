package cyou.devify.blog.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import cyou.devify.blog.entities.User;

@Service
public class UploadImageService {

  // private Path uploadPath;
  private Path uploadAvatarPath;

  public UploadImageService(@Value("${devify.upload.path}") String uploadDir) {
    // uploadPath = Paths.get(System.getProperty("user.home"), uploadDir, "images");
    uploadAvatarPath = Paths.get(System.getProperty("user.home"), uploadDir, "images", "avatar");
  }

  // public UploadResult saveFile(MultipartFile file) throws IOException {
  // if (!Files.exists(uploadPath)) {
  // Files.createDirectories(uploadPath);
  // }

  // var newFileName = UUID.randomUUID().toString()
  // .concat("." + StringUtils.getFilenameExtension(file.getOriginalFilename()));
  // Path filePath = uploadPath.resolve(newFileName);

  // Files.copy(file.getInputStream(), filePath,
  // StandardCopyOption.REPLACE_EXISTING);

  // return new UploadResult("/static/image/" + newFileName, filePath.toString());
  // }

  public UploadResult saveAvatar(MultipartFile file, User user) throws IOException {
    String contentType = file.getContentType();

    if (contentType == null)
      return null;

    if (!contentType.equals("image/png") &&
        !contentType.equals("image/jpeg") &&
        !contentType.equals("image/webp")) {
      return null;
    }

    if (!Files.exists(uploadAvatarPath)) {
      Files.createDirectories(uploadAvatarPath);
    }

    if (cyou.devify.blog.utils.StringUtils.isNonNullOrBlank(user.getAvatarFilePath())) {
      Files.deleteIfExists(Path.of(user.getAvatarFilePath()));
    }

    var newFileName = user.getId().toString()
        .concat("." + StringUtils.getFilenameExtension(file.getOriginalFilename()));
    Path filePath = uploadAvatarPath.resolve(newFileName);

    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

    return new UploadResult("/static/images/avatar/" + user.getId().toString(), filePath.toString());
  }

  public static final record UploadResult(String URI, String filePath) {
  }
}
