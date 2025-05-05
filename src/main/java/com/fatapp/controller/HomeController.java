package com.fatapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.fatapp.dtos.siparislistdto;
import com.fatapp.model.Siparis;
import com.fatapp.repository.SiparisRepository;

@Controller
public class HomeController {

    private final SiparisRepository siparisRepository;

    public HomeController(SiparisRepository siparisRepository) {
        this.siparisRepository = siparisRepository;
    }

    @GetMapping("/faturalar")
    public String mainPage(Model model) {
        List<Siparis> siparisler = siparisRepository.findAll();

        // DTO listesi oluştur
        List<siparislistdto> siparisListDtos = new ArrayList<>();
        for (Siparis siparis : siparisler) {
            siparislistdto dto = new siparislistdto();
            dto.setId(siparis.getId());
            if (siparis.getProduct() != null) {
                dto.setProductName(siparis.getProduct().getName());
                if (siparis.getProduct().getCategory() != null) {
                    dto.setCategoryName(siparis.getProduct().getCategory().getName());
                }
            }
            if (siparis.getUnit() != null) {
                dto.setUnitName(siparis.getUnit().getName());
            }
            dto.setQuantity(siparis.getQuantity());
            dto.setUnitPrice(siparis.getUnitprice());
            dto.setTotalPrice(siparis.getTotalprice());

            siparisListDtos.add(dto);
        }
        model.addAttribute("orders", siparisListDtos);
        return "index";
    }
}