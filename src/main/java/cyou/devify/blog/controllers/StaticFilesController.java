package cyou.devify.blog.controllers;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cyou.devify.blog.repositories.UserRepository;
import cyou.devify.blog.services.AmazonS3Service;
import cyou.devify.blog.utils.StringUtils;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@Controller
public class StaticFilesController {
  @Autowired
  UserRepository userRepository;

  @Autowired
  AmazonS3Service s3Service;

  @GetMapping("/static/images/avatar/{userId}/avatar.webp")
  public ResponseEntity<Resource> getAvatar(@PathVariable String userId) {
    if (StringUtils.isNullOrBlank(userId)) {
      return ResponseEntity.notFound().build();
    }

    var userOpt = userRepository.findById(UUID.fromString(userId));

    if (userOpt.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    var user = userOpt.get();

    if (StringUtils.isNullOrBlank(user.getAvatarFilePath())) {
      return ResponseEntity.notFound().build();
    }

    ResponseBytes<GetObjectResponse> avatar = s3Service.getObject(user.getAvatarFilePath());
    if (avatar == null || avatar.response().sdkHttpResponse().statusCode() != 200) {
      return ResponseEntity.notFound().build();
    }

    Resource resource;
    try {
      byte[] data = avatar.asByteArray();
      resource = new ByteArrayResource(data);
      return ResponseEntity.ok()
          .contentType(MediaType.parseMediaType("image/webp"))
          .contentLength(data.length)
          .body(resource);
    } catch (Exception e) {
      return ResponseEntity.internalServerError().build();
    }
  }

  @Deprecated
  @GetMapping("/static/images/avatar/{userId}")
  public ResponseEntity<Resource> downloadAvatar(@PathVariable String userId) {
    try {
      var user = userRepository.findById(UUID.fromString(userId)).get();
      if (user == null || StringUtils.isNullOrBlank(user.getAvatarFilePath())) {
        throw new Exception();
      }

      Path filePath = Paths.get(user.getAvatarFilePath());
      Resource resource = new UrlResource(filePath.toUri());

      if (resource.exists() && resource.isReadable()) {
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
            .body(resource);
      } else
        throw new Exception();
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

}
