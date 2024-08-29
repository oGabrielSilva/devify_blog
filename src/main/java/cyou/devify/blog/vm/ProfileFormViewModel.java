package cyou.devify.blog.vm;

import org.springframework.web.multipart.MultipartFile;

public record ProfileFormViewModel(String name, String pseudonym, String bio, MultipartFile avatar) {

}
