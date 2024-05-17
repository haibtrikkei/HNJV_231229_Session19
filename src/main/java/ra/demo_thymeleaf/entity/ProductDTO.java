package ra.demo_thymeleaf.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductDTO {
    private Integer proId;
    private String proName;
    private String producer;
    private Integer yearMaking;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expireDate;
    private Double price;
    private MultipartFile imageFile;
}
