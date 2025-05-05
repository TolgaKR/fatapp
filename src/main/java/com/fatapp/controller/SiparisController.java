package com.fatapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fatapp.dtos.SiparisEkleDto;
import com.fatapp.dtos.SiparisGuncelleDto;
import com.fatapp.model.Product;
import com.fatapp.model.Siparis;
import com.fatapp.model.Unit;
import com.fatapp.repository.ProductRepository;
import com.fatapp.repository.SiparisRepository;
import com.fatapp.repository.UnitRepository;

@Controller
@RequestMapping("/siparisler")
public class SiparisController {

    private final SiparisRepository siparisRepository;
    private final ProductRepository productRepository;
    private final UnitRepository unitRepository;

    public SiparisController(SiparisRepository siparisRepository, ProductRepository productRepository, UnitRepository unitRepository) {
        this.siparisRepository = siparisRepository;
        this.productRepository = productRepository;
        this.unitRepository = unitRepository;
    }

    @GetMapping("/ekle")
    public String gosterSiparisEkleForm(Model model) {
        model.addAttribute("siparisEkleDto", new SiparisEkleDto()); // Formda kullanmak için boş bir DTO gönderiyoruz
        return "siparis_ekle"; 
    }

    @PostMapping("/ekle")
    public String siparisEkle(@ModelAttribute SiparisEkleDto siparisEkleDto) {
        Product product = productRepository.findById(siparisEkleDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
        Unit unit = unitRepository.findById(siparisEkleDto.getUnitId())
                .orElseThrow(() -> new RuntimeException("Birim bulunamadı"));

        Siparis yeniSiparis = new Siparis();
        yeniSiparis.setProduct(product);
        yeniSiparis.setUnit(unit);
        yeniSiparis.setQuantity(siparisEkleDto.getQuantity());
        yeniSiparis.setUnitprice(siparisEkleDto.getUnitPrice());
        yeniSiparis.setTotalprice(yeniSiparis.getQuantity() * yeniSiparis.getUnitprice());

        siparisRepository.save(yeniSiparis);

        return "redirect:/faturalar"; 
    }

    @GetMapping("/guncelle/{id}")
public String gosterSiparisGuncelleForm(@PathVariable Long id, Model model) {
    Siparis siparis = siparisRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Sipariş bulunamadı"));

    SiparisGuncelleDto siparisGuncelleDto = new SiparisGuncelleDto();
    siparisGuncelleDto.setId(siparis.getId());
    siparisGuncelleDto.setProductId(siparis.getProduct().getId());
    siparisGuncelleDto.setUnitId(siparis.getUnit().getId());
    siparisGuncelleDto.setQuantity(siparis.getQuantity());
    siparisGuncelleDto.setUnitPrice(siparis.getUnitprice());

    model.addAttribute("siparisGuncelleDto", siparisGuncelleDto);
    return "siparis_guncelle";
}
@PostMapping("/guncelle")
public String siparisGuncelle(@ModelAttribute SiparisGuncelleDto siparisGuncelleDto) {
    Siparis mevcutSiparis = siparisRepository.findById(siparisGuncelleDto.getId())
            .orElseThrow(() -> new RuntimeException("Güncellenecek sipariş bulunamadı"));

    Product product = productRepository.findById(siparisGuncelleDto.getProductId())
            .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
    Unit unit = unitRepository.findById(siparisGuncelleDto.getUnitId())
            .orElseThrow(() -> new RuntimeException("Birim bulunamadı"));

    mevcutSiparis.setProduct(product);
    mevcutSiparis.setUnit(unit);
    mevcutSiparis.setQuantity(siparisGuncelleDto.getQuantity());
    mevcutSiparis.setUnitprice(siparisGuncelleDto.getUnitPrice());
    mevcutSiparis.setTotalprice(mevcutSiparis.getQuantity() * mevcutSiparis.getUnitprice());

    siparisRepository.save(mevcutSiparis);

    return "redirect:/faturalar";

}
}