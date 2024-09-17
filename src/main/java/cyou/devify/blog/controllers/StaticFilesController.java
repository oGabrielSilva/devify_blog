package cyou.devify.blog.controllers;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cyou.devify.blog.repositories.UserRepository;
import cyou.devify.blog.utils.StringUtils;

@Controller
public class StaticFilesController {
  @Autowired
  UserRepository userRepository;

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
