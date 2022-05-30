package ru.aamsystems.task;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class IndexController {
    final ProductRepository productRepository;

    public IndexController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/")
    public String index(@RequestParam(required = false) String name, Model model){
        if (name=="") {
            model.addAttribute("productList", productRepository.findAll());
        } else {
            Product product = new Product();
            product.setName(name);
            Example<Product> example = Example.of(product);
            model.addAttribute("productList", productRepository.findAll(example));
        }
        return "index";
    }

    @GetMapping("/addProduct")
    public String addProduct(Model model) {
        model.addAttribute("product", new Product());
        return "addProduct";
    }

    @PostMapping("/addProduct")
    public String addProduct(@ModelAttribute("product") Product product) {
        productRepository.save(product);
        return "redirect:/";
    }

    @GetMapping("/deleteProduct")
    public String deleteProduct(Model model) {
        return "deleteProduct";
    }

    @PostMapping("/deleteProduct")
    public String deleteProduct (@RequestParam("id") Long id){
        if (productRepository.existsById(id))
            productRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/editProduct/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", productRepository.findById(id).get());
        return "editProduct";
    }
    @PostMapping("/editProduct/{id}")
    public String editProduct(@PathVariable Long id, @ModelAttribute("product") Product product) {
        Product prod = productRepository.findById(id).get();
        prod.setDescription(product.getDescription());
        productRepository.save(prod);
        return "redirect:/";
    }
}

