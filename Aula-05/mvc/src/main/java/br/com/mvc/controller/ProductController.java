package br.com.mvc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import br.com.mvc.model.entity.Product;
import br.com.mvc.model.service.ProductService;
import java.util.List;
import java.math.BigInteger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

@Controller 
@RequestMapping ("/products")
public class ProductController {

    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping 
    public String read(Model model) {
        model.addAttribute("products", productService.read());
        model.addAttribute()
        return "products";
    }

    public String read(Model model) {
        model.addAllAttributes("products", p)
        return "products";
    }

    public String create(@ModelAttribute Object product){
        return "redirect:/products";
    }
}
