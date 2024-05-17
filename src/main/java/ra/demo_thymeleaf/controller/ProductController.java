package ra.demo_thymeleaf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ra.demo_thymeleaf.dao.ProductDAO;
import ra.demo_thymeleaf.entity.Product;
import ra.demo_thymeleaf.entity.ProductDTO;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductDAO productDAO;

    @Value("${file-upload}")
    private String fileUpload;

    @RequestMapping(value = {"/","/home"})
    public String home(Model model){
        List<Product> list = productDAO.getProducts();
        model.addAttribute("list",list);
        return "home";
    }
    @RequestMapping("/initInsertProduct")
    public String initInsert(Model model){
//        Product product = new Product();'
        ProductDTO pf = new ProductDTO();
        model.addAttribute("p",pf);
        return "insertProduct";
    }

    @RequestMapping("/insertProduct")
    public String insertProduct(@ModelAttribute("p") ProductDTO product, Model model, HttpServletRequest request){
        Product p = new Product();
        p.setProName(product.getProName());
        p.setProducer(product.getProducer());
        p.setYearMaking(product.getYearMaking());
        p.setExpireDate(product.getExpireDate());
        p.setPrice(product.getPrice());

        //Lấy đường dẫn tương đối từ thư mục của project đến thư muục images trong project
        String path = request.getServletContext().getRealPath("images");
        //Khởi 1 đối tượng File theo đường dẫn tương đối sẽ lấy được đường dẫn tuyệt đối
        File file1 = new File(path);

        MultipartFile imgFile = product.getImageFile();
        //lấy tên file file cần upload lên
        String fileName = imgFile.getOriginalFilename();
        try {
            //Khởi tạo đường dẫn của file sẽ copy lên
            File destination = new File(file1.getAbsolutePath()+"/"+fileName);
            if(!destination.exists())
                FileCopyUtils.copy(imgFile.getBytes(),destination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //Gán lại tên file vào biến imageName của Entity
        p.setImageName(fileName);
        //Save dữ liệu vào database
        boolean bl = productDAO.insertProduct(p);
        if(bl){
            return "redirect:/home";
        }else {
            model.addAttribute("p",product);
            return "insertProduct";
        }
    }

    @RequestMapping("/detailProduct/{proId}")
    public String detailProduct(@PathVariable("proId")Integer proId, Model model){
        Product product = productDAO.getProductById(proId);
        model.addAttribute("p",product);
        return "detailProduct";
    }

    @RequestMapping("/preUpdateProduct/{proId}")
    public String preUpdateProduct(@PathVariable("proId")Integer proId, Model model){
        Product product = productDAO.getProductById(proId);
        model.addAttribute("p",product);
        return "updateProduct";
    }


    @RequestMapping("/updateProduct")
    public String updateProduct(@ModelAttribute("p") Product product, @RequestParam("imageFile")MultipartFile imageFile, Model model, HttpServletRequest request){

        //Nếu chọn ảnh để update thì imageFile mới khác null và imageFile mới không rỗng
        if(imageFile!=null && !imageFile.isEmpty()){
            //Lấy đường dẫn tương đối từ thư mục của project đến thư muục images trong project
            String path = request.getServletContext().getRealPath("images");
            //Khởi 1 đối tượng File theo đường dẫn tương đối sẽ lấy được đường dẫn tuyệt đối
            File file1 = new File(path);

            //lấy tên file file cần upload lên
            String fileName = imageFile.getOriginalFilename();
            try {
                //Khởi tạo đường dẫn của file sẽ copy lên
                File destination = new File(file1.getAbsolutePath()+"/"+fileName);
                if(!destination.exists())
                    FileCopyUtils.copy(imageFile.getBytes(),destination);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //Gán lại tên file vào biến imageName của Entity
            product.setImageName(fileName);
        }

        //Save dữ liệu vào database
        boolean bl = productDAO.updateProduct(product);
        if(bl){
            return "redirect:/home";
        }else {
            model.addAttribute("p",product);
            return "updateProduct";
        }
    }
}
