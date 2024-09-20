package cyou.devify.blog.services.types;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import cyou.devify.blog.vo.UploadFileResult;

public interface UploadFileService {
  UploadFileResult uploadMultipart(MultipartFile data, String fileName, String path, Map<String, String> metadata);

}
