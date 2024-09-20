package cyou.devify.blog.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cyou.devify.blog.services.types.UploadFileService;
import cyou.devify.blog.vo.UploadFileResult;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class AmazonS3Service implements UploadFileService {
  private static final String urlBase = "https://s3.sa-east-1.amazonaws.com/[bucketName]/[filePath]";

  @Value("${aws.s3.bucket.default}")
  String defaultBucketName;

  @Autowired
  UserService userService;

  private S3Client s3 = S3Client.builder()
      .region(Region.SA_EAST_1)
      .build();

  @Override
  public UploadFileResult uploadMultipart(MultipartFile data, String fileName, String path,
      Map<String, String> metadata) {

    try {
      var user = userService.getCurrentAuthenticatedUser();

      if (metadata == null)
        metadata = new HashMap<>();

      if (!metadata.containsKey("author") && user != null) {
        metadata.put("author", user.getName());
      }

      metadata.put("Content-Length", String.valueOf(data.getSize()));
      metadata.put("Content-Type", data.getContentType());

      path = path.startsWith("/") ? path.substring(1) : path;
      path = path.endsWith("/") ? path : path + '/';
      fileName = fileName.replaceAll("/", "");

      String key = path + fileName;

      PutObjectRequest request = PutObjectRequest.builder()
          .bucket(defaultBucketName)
          .key(key)
          .metadata(metadata)
          .contentLength(data.getSize())
          .contentType(data.getContentType())
          .build();

      s3.putObject(request, RequestBody.fromInputStream(data.getInputStream(), data.getSize()));

      String uri = urlBase.replace("[bucketName]", defaultBucketName).replace("[filePath]", key) + "?serial="
          + String.valueOf(System.currentTimeMillis());
      return new UploadFileResult(uri, defaultBucketName + '/' + key);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

}
